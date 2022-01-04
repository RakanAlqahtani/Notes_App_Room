package com.example.notesapproom

import androidx.room.*


@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addNote(note : Note)

    @Query("SELECT * FROM NOTES ORDER BY ID ASC")
    fun getNotes(): List<Note>

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)
}