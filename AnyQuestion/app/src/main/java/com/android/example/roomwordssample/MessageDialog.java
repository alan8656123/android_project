package com.android.example.roomwordssample;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MessageDialog extends DialogFragment {
    private EditText messageField;
    private static TextView textArea ;
    private Button btnSend;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View textEntryView = inflater.inflate(R.layout.message_layout, null);
        builder.setView(textEntryView);

        messageField=(EditText) textEntryView.findViewById(R.id.messageField);
        textArea=(TextView) textEntryView.findViewById(R.id.textArea);
        btnSend=(Button) textEntryView.findViewById(R.id.btnSend);

        textArea.setText(MainActivity.total_message);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!messageField.getText().equals("")){
                    MainActivity.client.send(messageField.getText().toString());
                    messageField.setText("");

                }

            }
        });

        builder.setMessage(R.string.message_room)
                /*.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                })*/
                .setPositiveButton(R.string.leave, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

   /* public static void printToconsole(String message) {
        MainActivity.total_message+=message+"\n";
        textArea.setText(MainActivity.total_message);
    }*/


}
