package com.example.purelogregui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_progress.*
import kotlinx.android.synthetic.main.activity_record.*
import kotlinx.android.synthetic.main.activity_record.btn_back
import java.text.SimpleDateFormat
import java.util.*

class RecordActivity : AppCompatActivity() {

    //Authenticate Firebase
    lateinit var auth: FirebaseAuth
    var databaseReference2: DatabaseReference? = null
    var databaseReference: DatabaseReference? = null
    var database: FirebaseDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)

        //Access database table named "Users"
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("Users")
        databaseReference2 =database?.reference!!.child("Records")

        record()
    }


    private fun record() {

        val currentUser = auth.currentUser
//        val userReference = databaseReference?.child(currentUser?.uid!!.toString())
        val currentUserDb = databaseReference2?.child((currentUser?.uid!!))

        val date: TextView = findViewById(R.id.tv_recordDate)
        date.text = SimpleDateFormat("dd.MM.yyyy").format(System.currentTimeMillis()).toString()


        btnRecord.setOnClickListener {
            currentUserDb?.child("Sessions")?.push()?.setValue(date.text.toString())
            currentUserDb?.child("Sessions")?.push()?.setValue(et_recordWeight.text.toString())
            currentUserDb?.child("Sessions")?.push()?.setValue(et_recordTime.text.toString())
            currentUserDb?.child("Sessions")?.push()?.setValue(et_recordCalories.text.toString())

            startActivity(Intent(this@RecordActivity, HomeActivity::class.java))
            finish()
        }

        //return to home page when button pressed
        btn_back.setOnClickListener {
            startActivity(Intent(this@RecordActivity, HomeActivity::class.java))
            finish()
        }
    }
}