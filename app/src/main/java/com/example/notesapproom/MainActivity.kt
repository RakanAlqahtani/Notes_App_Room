package com.example.notesapproom

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

class MainActivity : AppCompatActivity() {


    private lateinit var notes : List<Note>
    private lateinit var adapter : RVAdapter

    private val databaseHelper by lazy { MyDBHelper.getDatabase(application).noteDao() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        notes = arrayListOf()
        adapter = RVAdapter(this)
        rvMain.adapter = adapter
        rvMain.layoutManager = LinearLayoutManager(this)

        btSave.setOnClickListener {
            if(etNote.text.toString().isNotEmpty()) {
                CoroutineScope(Dispatchers.IO).launch {
                    databaseHelper.addNote(Note(0,etNote.text.toString()))

                    val data = async { databaseHelper.getNotes() }.await()

                    if (data.isNotEmpty()){

                       withContext(Main) {
                           notes = data
                           adapter.update(notes)
                       }
                    }else{
//                        Toast.makeText(this, "Cant get data", Toast.LENGTH_LONG).show()
                        withContext(Main) {
                            notes = data
                            adapter.update(notes)
                        }
                    }

                }
                Toast.makeText(this, "Added successfully", Toast.LENGTH_LONG).show()


            }




        }

    }

    fun raiseDialog(id: Int){
        val dialogBuilder = AlertDialog.Builder(this)
        val updatedNote = EditText(this)
        updatedNote.hint = "Enter new text"
        dialogBuilder
            .setCancelable(false)
            .setPositiveButton("Save", DialogInterface.OnClickListener {
                    _, _ -> editNote(id, updatedNote.text.toString())
            })
            .setNegativeButton("Cancel", DialogInterface.OnClickListener {
                    dialog, _ -> dialog.cancel()
            })
        val alert = dialogBuilder.create()
        alert.setTitle("Update Note")
        alert.setView(updatedNote)
        alert.show()
    }



    private fun editNote(noteID: Int, noteText: String){

        CoroutineScope(Dispatchers.IO).launch {
            databaseHelper.updateNote(Note(noteID, noteText))

            val data = async { databaseHelper.getNotes() }.await()

            if (data.isNotEmpty()){

                withContext(Main) {
                    notes = data
                    adapter.update(notes)
                }
            }else{
//                        Toast.makeText(this, "Cant get data", Toast.LENGTH_LONG).show()

            }

        }

    }

    fun deleteNote(noteID: Int){

        CoroutineScope(Dispatchers.IO).launch {
            databaseHelper.deleteNote(Note(noteID, ""))

            val data = async { databaseHelper.getNotes() }.await()

            if (data.isNotEmpty()){

                withContext(Main) {
                    notes = data
                    adapter.update(notes)
                }
            }else{
//                        Toast.makeText(this, "Cant get data", Toast.LENGTH_LONG).show()

            }

        }


    }
}