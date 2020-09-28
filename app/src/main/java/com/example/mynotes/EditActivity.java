package com.example.mynotes;

import androidx.appcompat.app.AppCompatActivity;

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
    private final SimpleDateFormat df = new SimpleDateFormat("EEE MMM d, h:mm a");
    int pos = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        noteTitleEditText = findViewById(R.id.noteTitleEditText);
        noteBodyEditText = findViewById(R.id.noteBodyEditText);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            System.out.println("EXISTING NOTE");
            noteTitleEditText.setText(bundle.getString("SELECTED_NOTE_TITLE"));
            noteBodyEditText.setText(bundle.getString("SELECTED_NOTE_BODY"));
            pos = bundle.getInt("SELECTED_NOTE_POS");
        } else {
            System.out.println("NEW NOTE");
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
            Intent data = new Intent();
            data.putExtra("NOTE_TITLE", noteTitleEditText.getText().toString());
            data.putExtra("NOTE_BODY", noteBodyEditText.getText().toString());
            data.putExtra("NOTE_TIME", df.format(new Date()));
            if (pos > -1) {
                data.putExtra("NOTE_POS", pos);
            }
            setResult(RESULT_OK, data);
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}