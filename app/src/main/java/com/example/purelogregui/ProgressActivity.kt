package com.example.purelogregui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_progress.*
import java.security.KeyStore
import java.util.*
import java.util.Map
import kotlin.collections.ArrayList

class ProgressActivity : AppCompatActivity() {

    //Authenticate Firebase
    lateinit var auth: FirebaseAuth
    var databaseReference: DatabaseReference? = null
    var database: FirebaseDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_progress)

        //Access database table named "Users"
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("Users")

        progress()
    }

    private fun progress() {
        val currentUser = auth.currentUser
        val userReference = databaseReference?.child(currentUser?.uid!!.toString())
        val currentUserDb = databaseReference?.child((currentUser?.uid!!))

        userReference?.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                //Part1
                val entries = ArrayList<Entry>()

                val Weight = snapshot.child("Weight").value.toString().toFloat()
//                val Date = snapshot.child("Date").value.toString().toFloat()


                //Part2
                entries.add(Entry(0f, Weight))

                //Test
                tv_progressWeight.text = Weight.toString()
//                tv_progressDate.text = Date.toString()

                //Part3
                val vl = LineDataSet(entries, "Workout Session")

                //Part4
                vl.setDrawValues(false)
                vl.setDrawFilled(true)
                vl.lineWidth = 3f
                vl.fillColor = R.color.design_default_color_primary
                vl.fillAlpha = R.color.design_default_color_secondary

                //Part5
                lineChart.xAxis.labelRotationAngle = 0f

                //Part6
                lineChart.data = LineData(vl)

                //Part7
                lineChart.axisRight.isEnabled = false

                //Part8
                lineChart.setTouchEnabled(true)
                lineChart.setPinchZoom(true)

                //Part9
                lineChart.description.text = "Date"
                lineChart.setNoDataText("No data available!")

                //Part10
                lineChart.animateX(1800, Easing.EaseInExpo)

                //Part11
//                val markerView = CustomMarker(this@ProgressActivity, R.layout.marker_view)
//                lineChart.marker = markerView

            }

            override fun onCancelled(error: DatabaseError) {
                startActivity(Intent(this@ProgressActivity, HomeActivity::class.java))
                finish()
            }
        })


        //return to home page when button pressed
        btn_back.setOnClickListener {
            startActivity(Intent(this@ProgressActivity, HomeActivity::class.java))
            finish()
        }
    }
}
