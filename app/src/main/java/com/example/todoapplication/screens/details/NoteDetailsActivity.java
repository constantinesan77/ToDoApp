package com.example.todoapplication.screens.details;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.todoapplication.App;
import com.example.todoapplication.R;
import com.example.todoapplication.model.Note;

public class NoteDetailsActivity extends AppCompatActivity {
    private static final String EXTRA_NOTE = "NoteDetailsActivity.EXTRA_NOTE";
// Przeniesienie calego notatka przez Bundle

    Note note;

    private EditText editText;

    public static void start(Activity caller, Note note) {
        Intent intent = new Intent(caller, NoteDetailsActivity.class);
        if (note != null) {
            intent.putExtra(EXTRA_NOTE, note); // Jeżeli note nie rowno się 0 to dodamy note k Intentu
        }
        caller.startActivity(intent);
    }

// Wywolanie Activity
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);
        // Aplikacja czyta plik znacznikow i tworzy klasy z opisu
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); // Zrobilismy action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Back Button
        getSupportActionBar().setHomeButtonEnabled(true);

        setTitle(getString(R.string.note_details_title)); // Title Text

        editText = findViewById(R.id.text); // Wyciągamy EditText po ID

        if (getIntent().hasExtra(EXTRA_NOTE)) { // getIntent - zwraca Intent ktory byl użuwany przy startowaniu w Activity
            note = getIntent().getParcelableExtra(EXTRA_NOTE);
            editText.setText(note.text);
        } else {
            note = new Note();
        }
// Intent jest Parceble, wtedy możemy go wyciągnąc takim , jakim on jest
// Jezeli Intent jest, wtedy ustawiamy text w srodku note`a , jak nie ma wtedy tworzymy nowy Note

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_details, menu);
        // Wyciągamy menu używając getMenuInflater
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_save:
                if (editText.getText().length() > 0) {
                    note.text = editText.getText().toString();
                    note.done = false;
                    note.timestamp = System.currentTimeMillis();
                    if (getIntent().hasExtra(EXTRA_NOTE)) {
                        App.getInstance().getNoteDao().update(note);
                    } else {
                        App.getInstance().getNoteDao().insert(note);
                    }
                    finish();
                }
                break;
        }
// Patrzymy na ID przeciska ktory jest naciskany jezeli to przecisk Home, to zakończymy Activity
// Jeżeli ActionSave, wtedy save note
// Sprawdzamy czy user wpisal text, jeżeli nic nie wpisal, to przycisk nic nie robi
// Jeżeli tworzymy nowy note wtedy trzeba ją wstawic, jeżeli stara to odswieżyc
// Jeżeli jakis note przekazywal wtedy edytujemy note`a, jeżeli nie to tworzymy nowy notatek
        return super.onOptionsItemSelected(item);
    }
}