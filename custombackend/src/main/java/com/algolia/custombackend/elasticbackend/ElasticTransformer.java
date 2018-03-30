
package com.algolia.custombackend.elasticbackend;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.algolia.instantsearch.model.SearchResults;
import com.algolia.instantsearch.transformer.SearchResultsHandler;
import com.algolia.instantsearch.transformer.SearchTransformer;
import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Query;
import com.algolia.search.saas.Request;
import com.algolia.search.saas.RequestOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;

public class ElasticTransformer extends SearchTransformer<JSONObject, JSONObject> {

    public static class AsyncRequest extends AsyncTask<JSONObject, Boolean, JSONObject> implements Request {

        SearchResultsHandler<JSONObject> completionHandler;

        AsyncRequest(@NonNull SearchResultsHandler<JSONObject> completionHandler) {
            this.completionHandler = completionHandler;
        }

        @Override
        public void cancel() {

        }

        @Override
        public boolean isFinished() {
            return false;
        }

        @Override
        protected JSONObject doInBackground(JSONObject... elasticSearchParameters) {
            InputStream stream = null;
            HttpURLConnection hostConnection = null;
            JSONObject searchParameters = elasticSearchParameters[0];
            try {
                // Build URL.
                String urlString = "https://tests-first-sandbox-9472672183.eu-west-1.bonsaisearch.net/concerts/_search?";
                URL hostURL = new URL(urlString);

                String basicAuth = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    basicAuth = "Basic " + new String(android.util.Base64.encode("3nmp9kz7fh:gch2ewzerx".getBytes(), android.util.Base64.NO_WRAP));
                }

                // Open connection.
                hostConnection = (HttpURLConnection) hostURL.openConnection();
                hostConnection.setRequestProperty ("Authorization", basicAuth);

                // Headers
                hostConnection.setRequestProperty("Accept-Encoding", "gzip");
                hostConnection.setRequestProperty("Content-Type","application/json");
                hostConnection.setRequestMethod("POST");

                DataOutputStream printout = new DataOutputStream(hostConnection.getOutputStream());
                printout.writeBytes(searchParameters.toString());
                printout.flush ();
                printout.close ();

                // read response
                int code = hostConnection.getResponseCode();
                final boolean codeIsError = code / 100 != 2;
                stream = codeIsError ? hostConnection.getErrorStream() : hostConnection.getInputStream();
                if (stream == null) {
                    throw new IOException(String.format("Null stream when reading connection (status %d)", code));
                }

                final byte[] rawResponse;
                String encoding = hostConnection.getContentEncoding();
                if (encoding != null && encoding.equals("gzip")) {
                    rawResponse = _toByteArray(new GZIPInputStream(stream));
                } else {
                    rawResponse = _toByteArray(stream);
                }

                // handle http errors
                if (codeIsError) {
                    if (code / 100 == 4) {
                        throw new AlgoliaException(_getJSONObject(rawResponse).getString("message"), code);
                    }
                }

                JSONObject results = _getJSONObject(rawResponse);
                this.completionHandler.requestCompleted(results, null);
                return results;

            } catch (Exception e) { // fatal
                Log.e("AsyncTask error", "doInBackground: " + e.getMessage());
                this.completionHandler.requestCompleted(null, e);
                return null;
            } finally {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        /**
         * Reads the InputStream as UTF-8
         *
         * @param stream the InputStream to read
         * @return the stream's content as a String
         * @throws IOException if the stream can't be read, decoded as UTF-8 or closed
         */
        private String _toCharArray(InputStream stream) throws IOException {
            InputStreamReader is = new InputStreamReader(stream, "UTF-8");
            StringBuilder builder = new StringBuilder();
            char[] buf = new char[1000];
            int l = 0;
            while (l >= 0) {
                builder.append(buf, 0, l);
                l = is.read(buf);
            }
            is.close();
            return builder.toString();
        }

        /**
         * Reads the InputStream into a byte array
         *
         * @param stream the InputStream to read
         * @return the stream's content as a byte[]
         * @throws AlgoliaException if the stream can't be read or flushed
         */
        private byte[] _toByteArray(InputStream stream) throws AlgoliaException {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int read;
            byte[] buffer = new byte[1024];

            try {
                while ((read = stream.read(buffer, 0, buffer.length)) != -1) {
                    out.write(buffer, 0, read);
                }

                out.flush();
                return out.toByteArray();
            } catch (IOException e) {
                throw new AlgoliaException("Error while reading stream: " + e.getMessage());
            }
        }

        protected JSONObject _getJSONObject(byte[] array) throws JSONException, UnsupportedEncodingException {
            return new JSONObject(new String(array, "UTF-8"));
        }

    }

    @Override
    public Request searchAsync(@Nullable Query query, @Nullable RequestOptions requestOptions, @Nullable final CompletionHandler completionHandler) {
        JSONObject params = map(query);
        AsyncRequest request = new AsyncRequest(new SearchResultsHandler<JSONObject>() {
            @Override
            public void requestCompleted(final JSONObject content, final Exception error) {

                Handler h = new Handler(Looper.getMainLooper());
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        if (error != null) {
                            completionHandler.requestCompleted(null, new AlgoliaException((error.getMessage())));
                        } else {
                            JSONObject obj = ElasticTransformer.this.map(content);
                            completionHandler.requestCompleted(obj, null);
                        }
                    }
                };
                h.post(r);

            }
        });
        request.execute(params);
        return request;
    }

    @Override
    public JSONObject map(@NonNull Query query) {
        JSONObject rootObj = new JSONObject();
        try {

            JSONObject queryObj = new JSONObject();
            JSONArray fieldsArr = new JSONArray();
            fieldsArr.put("location");
            fieldsArr.put("name");

            queryObj.putOpt("fields", fieldsArr);
            queryObj.putOpt("type", "phrase_prefix");
            queryObj.putOpt("query", query.getQuery() !=null ? query.getQuery() : "");

            JSONObject multiMatchObj = new JSONObject();
            multiMatchObj.putOpt("multi_match", queryObj);

            JSONArray mustArrObj = new JSONArray();
            mustArrObj.put(multiMatchObj);

            JSONObject mustObj = new JSONObject();
            mustObj.putOpt("must", mustArrObj);

            JSONObject boolObj = new JSONObject();
            boolObj.putOpt("bool", mustObj);

            rootObj.putOpt("query", multiMatchObj);
            rootObj.putOpt("size", query.getHitsPerPage());
            rootObj.putOpt("from", query.getPage() * query.getHitsPerPage());

            if (query.getAttributesToHighlight().length != 0) {
                JSONObject highlightFields = new JSONObject();
                for(String field: query.getAttributesToHighlight()) {
                    highlightFields.putOpt(field, new JSONObject());
                }
                JSONObject highlightObject = new JSONObject();
                highlightObject.putOpt("fields", highlightFields);

                if (query.getHighlightPreTag() != null && query.getHighlightPostTag() != null) {
                    highlightObject.putOpt("pre_tags", query.getHighlightPreTag());
                    highlightObject.putOpt("post_tags", query.getHighlightPostTag());
                }
                rootObj.putOpt("highlight", highlightObject);
            }
        } catch (Exception e) {}
        return rootObj;
    }

    @Override
    public JSONObject map(@NonNull JSONObject elasticSearchResults) {
        if (elasticSearchResults == null) {
            return null;
        }
        JSONObject hitsContent = elasticSearchResults.optJSONObject("hits");
        if (hitsContent == null) {
            return null;
        }
//        Map<String, Object> map = new HashMap<>();
        JSONObject obj = new JSONObject();
        try {
            obj.putOpt("hits", hitsContent.optJSONArray("hits"));
            obj.putOpt("nbHits", hitsContent.optInt("total"));
            obj.putOpt("query", "test");
            obj.putOpt("params", "testparams");
            obj.putOpt("processingTimeMS", elasticSearchResults.optInt("took"));
        } catch (Exception e) {
            return null;
        }
        return obj;
    }
}
