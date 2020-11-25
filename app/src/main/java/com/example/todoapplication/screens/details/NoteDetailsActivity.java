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
// Przeniesienie całego Notatka przez Bundle

    Note note;

    private EditText editText;

    public static void start(Activity caller, Note note) {
        Intent intent = new Intent(caller, NoteDetailsActivity.class);
        if (note != null) {
            intent.putExtra(EXTRA_NOTE, note); // Jeżeli Note nie równo się 0, to dodamy Note k Intentu
        }
        caller.startActivity(intent);
    }

// Wywołanie Activity
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);
        // Aplikacja czyta plik znaczników i tworzy klasy z opisu
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); // Zrobiliśmy Action Bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Back Button
        getSupportActionBar().setHomeButtonEnabled(true);

        setTitle(getString(R.string.note_details_title)); // Title Text

        editText = findViewById(R.id.text); // Wyciągamy EditText po ID

        if (getIntent().hasExtra(EXTRA_NOTE)) { // getIntent - Zwraca Intent który był używany przy startowaniu w Activity
            note = getIntent().getParcelableExtra(EXTRA_NOTE);
            editText.setText(note.text);
        } else {
            note = new Note();
        }
// Intent jest Parceble, wtedy możemy go wyciągnąc takim , jakim on jest
// Jeżeli Intent jest, wtedy ustawiamy text w srodku Note`a , jak nie ma, wtedy tworzymy nowy Note

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
// Patrzymy na ID przyciska który jest naciskany, jezeli to przycisk Home, to zakończymy Activity
// Jeżeli ActionSave, wtedy przechowywamy Notatek
// Sprawdzamy czy user wpisal text, jeżeli nic nie wpisał, to przycisk nic nie robi
// Jeżeli tworzymy nowy Notatek, wtedy trzeba go wstawic, jeżeli stary to odświeżyc
// Jeżeli jakiś Notatek przekazywał wtedy edytujemy go, jeżeli nie to tworzymy nowy Notatek
        return super.onOptionsItemSelected(item);
    }
}
