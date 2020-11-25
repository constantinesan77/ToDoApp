package com.example.todoapplication.screens.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.todoapplication.App;
import com.example.todoapplication.model.Note;

import java.util.List;

// Dajemy dostęp do danych o liście naszych Notatków żeby wszystko było widać na ekranie glównym
public class MainViewModel extends ViewModel {
    private LiveData<List<Note>> noteLiveData = App.getInstance().getNoteDao().getAllLiveData();

    public LiveData<List<Note>> getNoteLiveData() {
        return noteLiveData;
    }
}

