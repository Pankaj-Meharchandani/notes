package com.example.notes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class NoteAdapter extends ArrayAdapter<Note> {

    private Context context;
    private List<Note> notes;
    private OnNoteClickListener onNoteClickListener;

    public NoteAdapter(Context context, List<Note> notes, OnNoteClickListener onNoteClickListener) {
        super(context, 0, notes);
        this.context = context;
        this.notes = notes;
        this.onNoteClickListener = onNoteClickListener;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Note note = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(note.getHeading());

        // Set click listener for editing a note
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onNoteClickListener != null) {
                    onNoteClickListener.onNoteClick(note);
                }
            }
        });

        // Set long click listener for deleting a note
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showDeleteDialog(position);
                return true;
            }
        });

        return convertView;
    }

    private void showDeleteDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Are you sure you want to delete this note?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Note noteToDelete = getItem(position);
                        deleteNoteFromDatabase(noteToDelete.getId());
                        notes.remove(noteToDelete);
                        notifyDataSetChanged();
                        showToast("Note deleted");
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User canceled the operation
                    }
                })
                .show();
    }

    private void deleteNoteFromDatabase(long noteId) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        databaseHelper.deleteNoteById(noteId);
    }

    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    // Interface for handling note clicks
    public interface OnNoteClickListener {
        void onNoteClick(Note note);
    }
}
