package com.example.mynotes;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {

    public TextView title;
    TextView dateTime;
    TextView noteBody;

    ViewHolder(View view) {
        super(view);
        title = view.findViewById(R.id.noteTitleTextView);
        dateTime = view.findViewById(R.id.dateTimeTextView);
        noteBody = view.findViewById(R.id.noteBodyTextView);
    }

}
