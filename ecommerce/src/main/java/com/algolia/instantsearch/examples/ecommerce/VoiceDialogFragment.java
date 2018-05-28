package com.algolia.instantsearch.examples.ecommerce;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.TextView;

public class VoiceDialogFragment extends DialogFragment {
    public static final String SEPARATOR = "• ";

    private String[] suggestions;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final View view = getActivity().getLayoutInflater().inflate(R.layout.layout_voice_overlay, null);

        view.findViewById(R.id.closeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        updateSuggestions(view);

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
    }

    public void setSuggestions(String... suggestions) {
        this.suggestions = suggestions;
        final View view = getView();
        if (view != null) {
            updateSuggestions(view);
        }
    }

    private void updateSuggestions(View view) {
        StringBuilder b = new StringBuilder();
        for (String s : suggestions) {
            b.append(SEPARATOR).append(s).append("\n");
        }
        ((TextView) view.findViewById(R.id.suggestions)).setText(b.toString());
    }
}
