package com.example.notes;
// DatabaseHelper.java
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "notes.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NOTES = "notes";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_HEADING = "heading";
    public static final String COLUMN_DETAILS = "details";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NOTES + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_HEADING + " TEXT, " +
                    COLUMN_DETAILS + " TEXT);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        onCreate(db);
    }
    public void deleteNoteById(long noteId) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            int deletedRows = db.delete(TABLE_NOTES, COLUMN_ID + " = ?",
                    new String[]{String.valueOf(noteId)});
            if (deletedRows > 0) {
                // Note deleted successfully
            } else {
                // Handle deletion failure
            }
        } catch (SQLException e) {
            // Handle the exception
        } finally {
            db.close();
        }
    }
    public long insertNote(String heading, String details) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_HEADING, heading);
        values.put(COLUMN_DETAILS, details);

        long newRowId = -1;

        try {
            newRowId = db.insert(TABLE_NOTES, null, values);
        } catch (SQLException e) {
            // Handle the exception
        } finally {
            db.close();
        }

        return newRowId;
    }

    public boolean updateNote(long noteId, String heading, String details) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_HEADING, heading);
        values.put(COLUMN_DETAILS, details);

        int rowsAffected = -1;

        try {
            rowsAffected = db.update(TABLE_NOTES, values, COLUMN_ID + " = ?",
                    new String[]{String.valueOf(noteId)});
        } catch (SQLException e) {
            // Handle the exception
        } finally {
            db.close();
        }

        return rowsAffected > 0;
    }

    public List<Note> getAllNotes() {
        List<Note> notes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.query(
                    TABLE_NOTES,
                    new String[]{COLUMN_ID, COLUMN_HEADING, COLUMN_DETAILS},
                    null,
                    null,
                    null,
                    null,
                    null
            );

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    long id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID));
                    String heading = cursor.getString(cursor.getColumnIndex(COLUMN_HEADING));
                    String details = cursor.getString(cursor.getColumnIndex(COLUMN_DETAILS));

                    Note note = new Note();
                    note.setId(id);
                    note.setHeading(heading);
                    note.setDetails(details);

                    notes.add(note);
                } while (cursor.moveToNext());

                cursor.close();
            }
        } catch (SQLException e) {
            // Handle the exception
        } finally {
            db.close();
        }

        return notes;
    }

    public Note getNoteById(long noteId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Note note = null;

        try {
            Cursor cursor = db.query(
                    TABLE_NOTES,
                    new String[]{COLUMN_ID, COLUMN_HEADING, COLUMN_DETAILS},
                    COLUMN_ID + " = ?",
                    new String[]{String.valueOf(noteId)},
                    null,
                    null,
                    null
            );

            if (cursor != null && cursor.moveToFirst()) {
                long id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID));
                String heading = cursor.getString(cursor.getColumnIndex(COLUMN_HEADING));
                String details = cursor.getString(cursor.getColumnIndex(COLUMN_DETAILS));

                note = new Note();
                note.setId(id);
                note.setHeading(heading);
                note.setDetails(details);

                cursor.close();
            }
        } catch (SQLException e) {
            // Handle the exception
        } finally {
            db.close();
        }

        return note;
    }
}
