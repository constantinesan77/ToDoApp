package com.example.todoapplication.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.todoapplication.model.Note;

@Database(entities = {Note.class}, version = 1, exportSchema = false) // Mechanizm wersji bazy danych

public abstract class AppDatabase extends RoomDatabase {
    public abstract NoteDao noteDao();
// Method abstrakcyjny, który pozwalia otrzymać dostęp do Dao naszej modeli

}
