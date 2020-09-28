package com.example.mynotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

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
            Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show();
            return true;
        } else {             
            return super.onOptionsItemSelected(item);
        }
    }
}