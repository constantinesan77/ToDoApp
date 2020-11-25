package com.example.todoapplication.screens.main;

import android.app.Activity;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SortedList;


import com.example.todoapplication.App;
import com.example.todoapplication.R;
import com.example.todoapplication.model.Note;
import com.example.todoapplication.screens.details.NoteDetailsActivity;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.NoteViewHolder> {

    private SortedList<Note> sortedList;

    public Adapter() {

        sortedList = new SortedList<>(Note.class, new SortedList.Callback<Note>() {
            // sortedList zamierzony dla automatycznych definowań zmian wewnątrz siebie I wydawanie komend których zmienił się
            @Override
            public int compare(Note o1, Note o2) {
                if (!o2.done && o1.done) {
                    return 1; // Jeżeli jeden z elementów nie jest zakonczony, a drugi tak wtedy 1 elememnt > 2 elementa
                }
                if (o2.done && !o1.done) {
                    return -1;
                }
                return (int) (o2.timestamp - o1.timestamp); // Porównywanie w czasie stworzenia
            }

            @Override
            public void onChanged(int position, int count) {
// Methoda ktora się wywoluje kiedy element zmiany jest w pozycji
                notifyItemRangeChanged(position, count);
// RecyclerView uzna się że elementy zmieniłi sie i zmieni ich nie dotykając inne elementy
            }

            @Override
            public boolean areContentsTheSame(Note oldItem, Note newItem) {
// Zwraca True jeżeli dwa elementy równy się
                return oldItem.equals(newItem);
            }

            @Override
            public boolean areItemsTheSame(Note item1, Note item2) {
// Zawartość elementów mogą być różne wewnątrz, ale ID jest taki sam
                return item1.uid == item2.uid; // Porównywanie po ID
            }

            @Override
            public void onInserted(int position, int count) { // Mówi Adapterowie o zmianach
                notifyItemRangeInserted(position, count);
            }

            @Override
            public void onRemoved(int position, int count) { // Mówi Adapterowie o zmianach
                notifyItemRangeRemoved(position, count);
            }

            @Override
            public void onMoved(int fromPosition, int toPosition) { // Mówi Adapterowie o zmianach
                notifyItemMoved(fromPosition, toPosition);
            }
        });
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        holder.bind(sortedList.get(position)); // Podpięcie do Notatku
    }

    @Override
    public int getItemCount() {
        return sortedList.size(); // Ile w sotredList jest elementów
    }

    public void setItems(List<Note> notes) {
        // Odświeżanie listy zawartości Adaptera i zamienianie ich na nowe elementy
        sortedList.replaceAll(notes);
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
// ViewHolder przechowywuję wszystkie linki na View żeby można było z nich korzystać
        TextView noteText;
        CheckBox completed;
        View delete;

        Note note; // Zachowamy Notatek który w ten sam moment wyswietła się

        boolean silentUpdate;

        public NoteViewHolder(@NonNull final View itemView) {
            super(itemView);

            noteText = itemView.findViewById(R.id.note_text);
            completed = itemView.findViewById(R.id.completed);
            delete = itemView.findViewById(R.id.delete);

            itemView.setOnClickListener(new View.OnClickListener() {
                //Po Item będziemy wywołwyać Activity dla edytowania Notatka
                @Override
                public void onClick(View view) {
                    NoteDetailsActivity.start((Activity) itemView.getContext(), note);
// Otrzymujemy link na Activity
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    App.getInstance().getNoteDao().delete(note); // Zwracamy do Dao
                }
            });

            completed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                    if (!silentUpdate) {
                        note.done = checked;
                        App.getInstance().getNoteDao().update(note);
                    }
                    updateStrokeOut();
                }
            });
// Jeżeli nie silentUpdate to odswiezamy notatek, jeżeli tak to zapisujemy zmiany Notatka
        }

        public void bind(Note note) { // Funkcja która wyswietła znaczenia pól Notatków na View
            this.note = note;

            noteText.setText(note.text); // Text notatków
            updateStrokeOut();

            silentUpdate = true;
            completed.setChecked(note.done);
            silentUpdate = false;
        }

        private void updateStrokeOut() { // Funkcja która wykresłia zakonczone działo
            if (note.done) {
                noteText.setPaintFlags(noteText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                noteText.setPaintFlags(noteText.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            }
            // Jeżeli Notatek zakończony, to wykreślamy go
        }
    }
}
