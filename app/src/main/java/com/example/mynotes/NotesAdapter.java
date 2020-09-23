package com.example.mynotes;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Date;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<ViewHolder> {

    private static final String TAG = "EmployeesAdapter";
    private List<Note> noteList;
    private MainActivity mainAct;

    NotesAdapter(List<Note> list, MainActivity ma) {
        this.noteList = list;
        mainAct = ma;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: MAKING NEW MyViewHolder");

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_list_card, parent, false);

        itemView.setOnClickListener(mainAct);
        itemView.setOnLongClickListener(mainAct);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: FILLING VIEW HOLDER Employee " + position);

        Note note = noteList.get(position);

        holder.title.setText(note.getTitle());
        holder.dateTime.setText(new Date().toString());
        holder.noteBody.setText(note.getNote());
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

}