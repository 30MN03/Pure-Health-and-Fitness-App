package com.example.purelogregui

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
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
        databaseReference2 = database?.reference!!.child("Records")

        record()
    }


    private fun record() {

        val currentUser = auth.currentUser
//        val userReference = databaseReference?.child(currentUser?.uid!!.toString())
        val currentUserDb = databaseReference2?.child((currentUser?.uid!!))

        val date: TextView = findViewById(R.id.tv_recordDate)
        date.text = SimpleDateFormat("ddMMyyyy").format(System.currentTimeMillis()).toString()


        btnRecord.setOnClickListener {
             if (TextUtils.isEmpty(et_recordWeight.text.toString())) {
                    et_recordWeight.error = "Please enter your weight"
                    return@setOnClickListener
                } else if (TextUtils.isEmpty(et_recordTime.text.toString())) {
                    et_recordTime.error = "Please enter session time"
                    return@setOnClickListener
                } else if (TextUtils.isEmpty(et_recordCalories.text.toString())) {
                    et_recordCalories.error = "Please enter number of calories burnt"
                    return@setOnClickListener
                }

                currentUserDb?.child("Date")?.setValue(date.text.toString())
                currentUserDb?.child("Weight")?.setValue(et_recordWeight.text.toString())
                currentUserDb?.child("Time")?.setValue(et_recordTime.text.toString())
                currentUserDb?.child("Calories")?.setValue(et_recordCalories.text.toString())

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