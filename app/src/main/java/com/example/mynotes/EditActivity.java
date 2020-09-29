package com.example.mynotes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditActivity extends AppCompatActivity {

    private EditText noteTitleEditText;
    private EditText noteBodyEditText;
    @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat df = new SimpleDateFormat("EEE MMM d, h:mm a");
    int pos = -1;

    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        noteTitleEditText = findViewById(R.id.noteTitleEditText);
        noteBodyEditText = findViewById(R.id.noteBodyEditText);


        bundle = getIntent().getExtras();
        if (bundle != null) {
            System.out.println("EXISTING NOTE");
            noteTitleEditText.setText(bundle.getString("SELECTED_NOTE_TITLE"));
            noteBodyEditText.setText(bundle.getString("SELECTED_NOTE_BODY"));
            pos = bundle.getInt("SELECTED_NOTE_POS");
        }
    }


    // displaying dialog for user to decide whether or not ot save current note upon
    // pressing the back button
    @Override
    public void onBackPressed() {
        if ((bundle == null) &&
                (noteTitleEditText.getText().toString().equals("")
                        && noteBodyEditText.getText().toString().equals(""))) {
            super.onBackPressed();
        } else if ((bundle != null) &&
                (noteTitleEditText.getText().toString().equals(bundle.getString("SELECTED_NOTE_TITLE"))
                        && noteBodyEditText.getText().toString().equals(bundle.getString("SELECTED_NOTE_BODY")))) {
            super.onBackPressed();
        }  else {
            // changes have been made - ask user if they'd like to save/discard changes
            saveEditedNoteDialog();
        }
    }

    public void returnNoteData() {
        Intent data = new Intent();
        if (noteTitleEditText.getText().toString().equals("")) {
            // no title was given - discard note and alert user with toast in main activity
            setResult(RESULT_CANCELED);
        } else {
            data.putExtra("NOTE_TITLE", noteTitleEditText.getText().toString());
            data.putExtra("NOTE_BODY", noteBodyEditText.getText().toString());
            data.putExtra("NOTE_TIME", df.format(new Date()));
            if (pos > -1) {
                data.putExtra("NOTE_POS", pos);
            }
            setResult(RESULT_OK, data);
        }
    }

    // options menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menuSaveNote) {
            // saving data from edit text fields for note creation
            returnNoteData();
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    // alert dialog
    public void saveEditedNoteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                returnNoteData();
                finish();
            }
        });
        builder.setNegativeButton("Discard", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        builder.setMessage("Changes have been made to the current note. Would you like to save or discard" +
                " these changes?");
        builder.setTitle("Save Changes");

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}