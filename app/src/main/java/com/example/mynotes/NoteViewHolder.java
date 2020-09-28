package com.example.mynotes;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class NoteViewHolder extends RecyclerView.ViewHolder {

    TextView title;
    TextView dateTime;
    TextView noteBody;

    NoteViewHolder(View view) {
        super(view);
        title = view.findViewById(R.id.noteTitleTextView);
        dateTime = view.findViewById(R.id.dateTimeTextView);
        noteBody = view.findViewById(R.id.noteBodyTextView);
    }

}
