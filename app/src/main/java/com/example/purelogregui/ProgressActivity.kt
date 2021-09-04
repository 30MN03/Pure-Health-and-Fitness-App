package com.example.purelogregui

import CustomMarker
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_progress.*
import java.security.KeyStore
import java.util.*
import java.util.Map
import kotlin.collections.ArrayList
import kotlin.math.E
import com.github.mikephil.charting.charts.Chart
import com.github.mikephil.charting.components.XAxis

class ProgressActivity : AppCompatActivity() {

    //Authenticate Firebase
    lateinit var auth: FirebaseAuth
    var databaseReference: DatabaseReference? = null
    var database: FirebaseDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_progress)

        //Access database table named "Records"
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("Records")

        progress()
    }

    private fun progress() {
        val currentUser = auth.currentUser
        val userReference = databaseReference?.child(currentUser?.uid!!.toString())
        val currentUserDb = databaseReference?.child((currentUser?.uid!!))

        // access the items of the list
        val charts = resources.getStringArray(R.array.Chart)

        // access the spinner
        val spinner = findViewById<Spinner>(R.id.sp_Chart)

        if (spinner != null) {
            val adapter = ArrayAdapter(this,
                    android.R.layout.simple_spinner_item, charts)
            spinner.adapter = adapter


            spinner.onItemSelectedListener = object :
                    AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {

                    (parent.getChildAt(0) as TextView).setTextColor(Color.GRAY)
                    (parent.getChildAt(0) as TextView).textSize = 14f

                    if(position==0){
                        lineChart.visibility = View.VISIBLE
                        tv_progressTime.visibility =View.GONE
                        tv_progressCalories.visibility = View.GONE
                    }
                    else if(position==1){
                        lineChart.visibility = View.GONE
                        tv_progressTime.visibility =View.VISIBLE
                        tv_progressCalories.visibility = View.GONE
                    }
                    else if(position==2){
                        lineChart.visibility = View.GONE
                        tv_progressTime.visibility =View.GONE
                        tv_progressCalories.visibility = View.VISIBLE
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    Toast.makeText(this@ProgressActivity, "Select a graph to display", Toast.LENGTH_LONG).show()
                }
            }
        }

        userReference?.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                //Part1
                val entries = ArrayList<Entry>()

//                val startingWeight = snapshot.child("StartingWeight").value.toString().toFloat()
//                val startingDate = snapshot.child("StartingDate").value.toString().toFloat()
                val Weight = snapshot.child("Weight").value.toString().toFloat()
                val Date = snapshot.child("Date").value.toString().toFloat()

                //Part2
//                entries.add(Entry( 2092021f, 124f ))
//                entries.add(Entry( 3092021f, 125f ))
                entries.add(Entry(Date, Weight))
//                entries.add(Entry( 5092021f, 138f ))
//                entries.add(Entry( 6092021f, 140f ))


                //UPDATE
                tv_progressTime.text = snapshot.child("Time").value.toString()
                tv_progressCalories.text = snapshot.child("Calories").value.toString()

                //Part3
                val vl = LineDataSet(entries, "Workout Session")

                //Part4
                vl.setDrawValues(false)
                vl.setDrawFilled(true)
                vl.setDrawCircleHole(false)
                vl.circleRadius = 4f
                vl.lineWidth = 2f
//                vl.color = R.color.white
//                vl.fillColor = R.color.design_default_color_background
//                vl.fillAlpha = R.color.design_default_color_secondary

                //Part5
                lineChart.xAxis.labelRotationAngle = 0f
//                lineChart.xAxis.textColor = R.color.design_default_color_on_primary

                //Part6
                lineChart.data = LineData(vl)

                //Part7
                lineChart.axisRight.isEnabled = false

                //Part8
                lineChart.setTouchEnabled(true)
                lineChart.setPinchZoom(true)

                //Part9
//                lineChart.axisLeft.textColor = R.color.design_default_color_secondary
                lineChart.description.text = "Progression"
                lineChart.setNoDataText("No data available!")

                //Part10
                lineChart.animateX(1800, Easing.EaseInExpo)

                //Part11
//                val markerView = CustomMarker(this@ProgressActivity, R.layout.oval_marker)
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
