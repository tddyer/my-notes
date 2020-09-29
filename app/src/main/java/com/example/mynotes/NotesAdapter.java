package com.example.mynotes;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NoteViewHolder> {

    private List<Note> noteList;
    private MainActivity mainAct;

    SimpleDateFormat df = new SimpleDateFormat("EEE MMM d, h:mm a");

    public NotesAdapter(List<Note> list, MainActivity ma) {
        this.noteList = list;
        mainAct = ma;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        // create inflater from parent's context, then inflate it using the defined layout
        // for each list card
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_list_card, parent, false);

        // calls appropriate functions from main activity when a click/long click is detected
        itemView.setOnClickListener(mainAct);
        itemView.setOnLongClickListener(mainAct);

        return new NoteViewHolder(itemView);
    }

    // sets data into view holder to match a Note object
    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = noteList.get(position);
        holder.title.setText(note.getTitle());
        holder.dateTime.setText(note.getLastSave());
        if (note.getNote().length() < 80) {
            holder.noteBody.setText(note.getNote());
        } else {
            String display = note.getNote().substring(0, 80) + "...";
            holder.noteBody.setText(display);
        }
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

}