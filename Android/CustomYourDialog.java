package com.samsung.jason.booklist;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/* display book detail */
public class DisplayBookDetailFragement extends DialogFragment{
    private static final String LOG_TAG = "DisplayBookDetailFragment";
    static final String TAG = "DisplayBookDetailFragment";


    // interface to call back the caller activity
    // onDialogPositiveClick(DialogFragment dialog); onDialogNegativeClick(DialogFragment dialog)
    NoticeDialogListener mListener = null;
    private BookInformation bookInfo = null;
    private String name = null;
    private String supplements = null;
    private String bookAbs = null;


    @Override
    public Dialog onCreateDialog(Bundle savedState){
        Log.d(LOG_TAG,"create dialog");

        mListener = (NoticeDialogListener)(getActivity());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // get inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View displayBookInfoView = inflater.inflate(R.layout.display_book_dialog, null);

        name = bookInfo.getName();
        supplements = bookInfo.getAuthor() + "写于" + bookInfo.getPublishDate();
        bookAbs = bookInfo.getBookAbstract();

        TextView nameView = (TextView)displayBookInfoView.findViewById(R.id.name);
        TextView supsView = (TextView)displayBookInfoView.findViewById(R.id.supplement);
        TextView abstractView = (TextView)displayBookInfoView.findViewById(R.id.book_abstract);


        nameView.setText(name);
        supsView.setText(supplements); //supsView.setTextSize(R.dimen.other_text_size);
        abstractView.setText(bookAbs); //abstractView.setTextSize(R.dimen.other_text_size);
        //abstractView.setTextSize(16);

        builder.setView(displayBookInfoView)
               .setCancelable(false)
               .setPositiveButton(R.string.ok,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Log.v(LOG_TAG,"ok to return host activity");
                        mListener.onDialogPositiveListener(DisplayBookDetailFragement.this);
                    }
                })
                .setNegativeButton(R.string.cancel,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Log.v(LOG_TAG,"ok to return host activity");
                        mListener.onDialogNegativeListener(DisplayBookDetailFragement.this);
                    }
                });

        return builder.create();
    }

}
