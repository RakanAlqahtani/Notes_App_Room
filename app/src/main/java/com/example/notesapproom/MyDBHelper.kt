package com.example.notesapproom

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Note::class],version = 1,exportSchema = false)
abstract class MyDBHelper : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object{
        @Volatile  // writes to this field are immediately visible to other threads
        private var INSTANCE: MyDBHelper? = null

        fun getDatabase(context: Context): MyDBHelper{
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){  // protection from concurrent execution on multiple threads
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyDBHelper::class.java,
                    "students"
                ).fallbackToDestructiveMigration()  // Destroys old database on version change
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}