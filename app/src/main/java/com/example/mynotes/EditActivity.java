package com.example.mynotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Date;

public class EditActivity extends AppCompatActivity {

    private EditText noteTitleEditText;
    private EditText noteBodyEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        noteTitleEditText = findViewById(R.id.noteTitleEditText);
        noteBodyEditText = findViewById(R.id.noteBodyEditText);
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
            data.putExtra("NOTE_TIME", DateFormat.getInstance().format(new Date()));
//            Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK, data);
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}