package com.example.notes;

// NoteDetailActivity.java
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;

public class NoteDetailActivity extends AppCompatActivity {

    private EditText noteHeadingEditText;
    private EditText noteDetailsEditText;
    private Button saveNoteButton;

    private long noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        noteHeadingEditText = findViewById(R.id.noteHeadingEditText);
        noteDetailsEditText = findViewById(R.id.noteDetailsEditText);
        saveNoteButton = findViewById(R.id.saveNoteButton);

        // Get noteId from the intent (if available)
        noteId = getIntent().getLongExtra("noteId", -1);

        if (noteId != -1) {
            // Load existing note details for editing
            loadNoteDetails(noteId);
        }

        // Set click listener for saving a note
        saveNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNote();
            }
        });
    }

    private void loadNoteDetails(long noteId) {
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        Note note = databaseHelper.getNoteById(noteId);

        if (note != null) {
            noteHeadingEditText.setText(note.getHeading());
            noteDetailsEditText.setText(note.getDetails());
        }
    }

    private void saveNote() {
        String heading = noteHeadingEditText.getText().toString();
        String details = noteDetailsEditText.getText().toString();

        // Validate input
        if (TextUtils.isEmpty(heading)) {
            Toast.makeText(this, "Please enter a heading", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        if (noteId == -1) {
            // New note
            long newNoteId = databaseHelper.insertNote(heading, details);
            if (newNoteId != -1) {
                Toast.makeText(this, "Note saved successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error saving note", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Existing note
            boolean updated = databaseHelper.updateNote(noteId, heading, details);
            if (updated) {
                Toast.makeText(this, "Note updated successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error updating note", Toast.LENGTH_SHORT).show();
            }
        }

        // Navigate back to the main page
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}

