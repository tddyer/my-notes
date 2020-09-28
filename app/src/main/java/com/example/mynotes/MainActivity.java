package com.example.mynotes;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity
    implements View.OnClickListener, View.OnLongClickListener {

    private static final int REQ_ID = 1;

    private final List<Note> notesList = new ArrayList<>();
    private RecyclerView recyclerView;
    private NotesAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.notesRecyclerView);
        // creates adapter using notesList + this (main activity) and assigns it to recyclerview
        mAdapter = new NotesAdapter(notesList, this);
        recyclerView.setAdapter(mAdapter);

        // add items in linear layout (i.e in order)
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // creating some notes
        for (int i = 0; i < 30; i++) {
            notesList.add(new Note("Assignment " + String.valueOf(i) + " is due soon.", "Assignment " + String.valueOf(i), new Date().toString()));
        }
    }

    // activity navigation

    void launchEditActivity() {
        Intent intent = new Intent(this, EditActivity.class);
        startActivityForResult(intent, REQ_ID);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_ID) {
            if (resultCode == RESULT_OK) {
                // data was returned successfully
                String title = data.getStringExtra("NOTE_TITLE");
                String body = data.getStringExtra("NOTE_BODY");
                Toast.makeText(this, title + "\n" + body, Toast.LENGTH_LONG).show();
            } else {
                // data wasn't returned successfully
                Log.d("MainActivity", "onActivityResult: No data returned");
            }
        }
    }


    // implementing functionality for onClick and onLongClick for the view holder

    // From OnClickListener
    @Override
    public void onClick(View v) {  // click listener called by ViewHolder clicks
        int pos = recyclerView.getChildLayoutPosition(v);
        Note m = notesList.get(pos);
        Toast.makeText(v.getContext(), "SHORT " + m.toString(), Toast.LENGTH_SHORT).show();
    }

    // From OnLongClickListener
    @Override
    public boolean onLongClick(View v) {  // long click listener called by ViewHolder long clicks
        int pos = recyclerView.getChildLayoutPosition(v);
        Note m = notesList.get(pos);
        Toast.makeText(v.getContext(), "LONG " + m.toString(), Toast.LENGTH_SHORT).show();
        return true; // true means only long click is registered, false means it passes onto other listeners (onClick, swipe, etc)
        // eg for false: you want to detect screen usage (i.e clicks) but also want to do something on long press, so on click should
        // be registered on a long click
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "The back button was pressed - Bye!", Toast.LENGTH_SHORT).show();
        super.onBackPressed();

    }

    // add/remove notes from list of notes

    public void addTop(View v) {
        notesList.add(0, new Note("Note", "Note", new Date().toString()));
        mAdapter.notifyDataSetChanged();
    }

    public void addEnd(View v) {
        notesList.add(new Note("Note", "Note", new Date().toString()));
        mAdapter.notifyDataSetChanged();
    }

    public void removeTop(View v) {
        if (!notesList.isEmpty()) {
            notesList.remove(0);
            mAdapter.notifyDataSetChanged();
        }
    }

    public void removeEnd(View v) {
        if (!notesList.isEmpty()) {
            notesList.remove(notesList.size() - 1);
            mAdapter.notifyDataSetChanged();
        }
    }

    // options menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuHelp:
                Toast.makeText(this, "You want to ask for help", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menuAddNote:
                launchEditActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}