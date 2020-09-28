package com.example.mynotes;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
    implements View.OnClickListener, View.OnLongClickListener {

    private static final int NEW_NOTE_ID = 1;
    private static final int EXISTING_NOTE_ID = 2;
    private static final String TAG = "MainActivity";

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
    }

    // activity navigation

    void launchEmptyEditActivity() {
        Intent intent = new Intent(this, EditActivity.class);
        startActivityForResult(intent, NEW_NOTE_ID);
    }

    void launchExistingEditActivity(Note note, int pos) {
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("SELECTED_NOTE_TITLE", note.title);
        intent.putExtra("SELECTED_NOTE_BODY", note.note);
        intent.putExtra("SELECTED_NOTE_POS", pos);
        startActivityForResult(intent, EXISTING_NOTE_ID);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEW_NOTE_ID) {
            if (resultCode == RESULT_OK) {
                // data was returned successfully
                String title = data.getStringExtra("NOTE_TITLE");
                String body = data.getStringExtra("NOTE_BODY");
                String time = data.getStringExtra("NOTE_TIME");

                // add new note to top of note list
                addTop(title, body, time);
            } else {
                // data wasn't returned successfully
                Log.d(TAG, "onActivityResult: Result code: " + resultCode);
            }
        } else if (requestCode == EXISTING_NOTE_ID) {
            if (resultCode == RESULT_OK) {
                Note n;
                int pos = data.getIntExtra("NOTE_POS", -1);
                if (pos > -1) {
                    n = notesList.get(pos);
                    n.setTitle(data.getStringExtra("NOTE_TITLE"));
                    n.setNote(data.getStringExtra("NOTE_BODY"));
                    n.setLastSave(data.getStringExtra("NOTE_TIME"));
                    mAdapter.notifyDataSetChanged();
                } else {
                    Log.d(TAG, "onActivityResult: "+ "An error occurred");
                }
            } else {
                // data wasn't returned successfully
                Log.d(TAG, "onActivityResult: Result code: " + resultCode);
            }
        } else {
            Log.d(TAG, "onActivityResult: Request code: " + requestCode);
        }
    }


    // implementing functionality for onClick and onLongClick for the view holder

    // From OnClickListener
    @Override
    public void onClick(View v) {  // click listener called by ViewHolder clicks
        int pos = recyclerView.getChildLayoutPosition(v);
        Note n = notesList.get(pos);
        launchExistingEditActivity(n, pos);
    }

    // From OnLongClickListener
    @Override
    public boolean onLongClick(View v) {  // long click listener called by ViewHolder long clicks
        int pos = recyclerView.getChildLayoutPosition(v);
        deleteNote(pos);
        return true;
    }

    // TODO: Save state here when leaving app
    @Override
    public void onBackPressed() {
        Toast.makeText(this, "The back button was pressed - Bye!", Toast.LENGTH_SHORT).show();
        super.onBackPressed();
    }

    // add/remove notes from list of notes

    public void addTop(String title, String body, String time) {
        notesList.add(0, new Note(body, title, time));
        mAdapter.notifyDataSetChanged();
    }


    public void removePos(int pos) {
        if (!notesList.isEmpty()) {
            notesList.remove(pos);
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
                launchEmptyEditActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // alert dialogs

    public void deleteNote(final int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                removePos(pos);
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // do nothing
            }
        });
        builder.setMessage("Are you sure you want to delete this note?");
        builder.setTitle("Delete Note");

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}