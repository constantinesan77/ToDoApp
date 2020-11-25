package com.example.todoapplication;

import android.app.Application;

import androidx.room.Room;

import com.example.todoapplication.data.AppDatabase;
import com.example.todoapplication.data.NoteDao;

public class App extends Application {

    private AppDatabase database;
    private NoteDao noteDao;
    private static App instance;

    public static App getInstance() { // Singleton
        return instance;
    }
// Otrzymujemy dostęp do aplikacji i modelów z jakiego kołwiek miejsca w aplikacji

    @Override // Method onCreate który wywołuje przed startem aplikajci
    public void onCreate() {
        super.onCreate();

        instance = this;

        database = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "app-db-name")
                .allowMainThreadQueries()
                .build();
// Tworzenie Bazy Danych za pomocą Room
        noteDao = database.noteDao(); // Otrzymujemy Dao po wywołaniu noteDao
    }

    public void setDatabase(AppDatabase database) {
        this.database = database;
    }

    public NoteDao getNoteDao() {
        return noteDao;
    }

}
