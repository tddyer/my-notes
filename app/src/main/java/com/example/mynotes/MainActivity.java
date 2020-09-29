package com.example.mynotes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
    implements View.OnClickListener, View.OnLongClickListener {

    private static final int NEW_NOTE_ID = 1;
    private static final int EXISTING_NOTE_ID = 2;
    private static final String TAG = "MainActivity";

    private List<Note> notesList = new ArrayList<>();
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

        // loading saved notes from JSON file (if any have been saved)
        loadNotes(notesList);

        if (notesList.size() > 0) {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onPause() {
        saveNotes(notesList);
        super.onPause();
    }

    // takes in the arraylist of notes and saves to json file
    public void saveNotes(List<Note> notes) {
        try {
            // get output stream from Notes.json file
            FileOutputStream fos = getApplicationContext().
                    openFileOutput("Notes.json", Context.MODE_PRIVATE);
            JsonWriter writer = new JsonWriter(new OutputStreamWriter(fos, StandardCharsets.UTF_8));
            writer.setIndent("  ");

            // start parsing array of notes from input arraylist
            writer.beginArray();
            for (Note n : notes) {
                // for each note in array of notes, save the individual note
                //      see saveNote() below
                saveNote(writer, n);
            }
            writer.endArray();
            writer.close();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    // writes individual note to json file
    public void saveNote(JsonWriter writer, Note note) {
        try {
            writer.beginObject();
            writer.name("title").value(note.getTitle());
            writer.name("note").value(note.getNote());
            writer.name("lastSave").value(note.getLastSave());
            writer.endObject();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    // load saved notes from Notes.json file stored in device file system
    private void loadNotes(List<Note> notes) {
        try {
            // create input stream from Notes.json file
            InputStream is = getApplicationContext().openFileInput("Notes.json");
            JsonReader reader = new JsonReader(new InputStreamReader(is, StandardCharsets.UTF_8));

            // begin the array of notes from Notes.json file
            reader.beginArray();
            while (reader.hasNext()) { // while there are still notes left in array...
                // load each individual note into notes arraylist
                //      see loadNote() below
                notes.add(loadNote(reader));
            }
            reader.endArray();
        } catch (FileNotFoundException e) {
            System.out.println("NO EXISTING NOTES FOUND");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // loads individual notes from json file
    private Note loadNote(JsonReader reader) {
        Note note = new Note();

        try {
            // begin the individual note object
            reader.beginObject();
            while (reader.hasNext()) { // while there are still items in the note...
                // read token name and save to the local Note object
                String tokenName = reader.nextName();
                switch (tokenName) {
                    case "title":
                        note.setTitle(reader.nextString());
                        break;
                    case "note":
                        note.setNote(reader.nextString());
                        break;
                    case "lastSave":
                        note.setLastSave(reader.nextString());
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }
            reader.endObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return note;
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
                // use the notes position in notesList and update its values instead of creating a
                // new note
                Note n;
                int pos = data.getIntExtra("NOTE_POS", -1);
                if (pos > -1) {
                    n = notesList.get(pos);
                    n.setTitle(data.getStringExtra("NOTE_TITLE"));
                    n.setNote(data.getStringExtra("NOTE_BODY"));
                    n.setLastSave(data.getStringExtra("NOTE_TIME"));
                    moveToTop(n, pos);
                } else {
                    Toast.makeText(this, "An error occurred while editing your note", Toast.LENGTH_LONG).show();
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
        Note n = new Note();
        n.setTitle(title);
        n.setNote(body);
        n.setLastSave(time);
        notesList.add(0, n);
        mAdapter.notifyDataSetChanged();
    }

    public void removePos(int pos) {
        if (!notesList.isEmpty()) {
            notesList.remove(pos);
            mAdapter.notifyDataSetChanged();
        }
    }

    public void moveToTop(Note n, int pos) {
        notesList.remove(pos);
        addTop(n.title, n.note, n.lastSave);
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