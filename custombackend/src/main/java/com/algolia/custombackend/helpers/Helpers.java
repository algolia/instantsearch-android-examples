package com.algolia.custombackend.helpers;

import com.algolia.search.saas.AlgoliaException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class Helpers {
    /**
     * Reads the InputStream as UTF-8
     *
     * @param stream the InputStream to read
     * @return the stream's content as a String
     * @throws IOException if the stream can't be read, decoded as UTF-8 or closed
     */
    public static String _toCharArray(InputStream stream) throws IOException {
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
    public static byte[] _toByteArray(InputStream stream) throws AlgoliaException {
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

    public static JSONObject _getJSONObject(byte[] array) throws JSONException, UnsupportedEncodingException {
        return new JSONObject(new String(array, "UTF-8"));
    }
}
