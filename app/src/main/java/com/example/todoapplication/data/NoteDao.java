package com.example.todoapplication.data;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Insert;
import androidx.room.Delete;
import androidx.room.Update;

import com.example.todoapplication.model.Note;

import java.util.List;

@Dao // Data Access Object
public interface NoteDao { // Tworzenie interfejsu
    @Query("SELECT * FROM Note")
        // SQL requesty
    List<Note> getAll();

    @Query("SELECT * FROM Note")
        // SQL requesty do Live Daty
    LiveData<List<Note>> getAllLiveData();

    @Query("SELECT * FROM Note WHERE uid IN (:userIds)")
        // Auto insert name
    List<Note> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM Note WHERE uid = :uid LIMIT 1")
        // Wybór według ID
    Note findById(int uid);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
// Jezeli ja chce wstawic note w bd z id ktore juz istneje to odbedzie sie zamiana starego na nowy
    void insert(Note note);

    @Update
    void update(Note note);

    @Delete
    void delete(Note note);
}

