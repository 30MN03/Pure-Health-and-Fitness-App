package com.example.purelogregui

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_review.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.*
import kotlinx.android.synthetic.main.activity_review.btnSaveInfo as btnSaveInfo1

class ProfileActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    var databaseReference: DatabaseReference? = null
    var database: FirebaseDatabase? = null

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("Users")

        profile()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun profile() {
        val currentUser = auth.currentUser
        val userReference = databaseReference?.child(currentUser?.uid!!.toString())
        val currentUserDb = databaseReference?.child((currentUser?.uid!!))

        userReference?.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                tv_profileName.text = snapshot.child("Name").value.toString()
                tv_profileDOB_show.text = snapshot.child("DateOfBirth").value.toString()
                tv_Gender.text = snapshot.child("Gender").value.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                startActivity(Intent(this@ProfileActivity, HomeActivity::class.java))
                finish()
            }
        })

        val textView: TextView = findViewById(R.id.tv_profileDOB)
        textView.text = SimpleDateFormat("dd.MM.yyyy").format(System.currentTimeMillis())

        var cal = Calendar.getInstance()

        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "dd.MM.yyyy" // required format
            val sdf = SimpleDateFormat(myFormat, Locale.UK)
            textView.text = sdf.format(cal.time)

            currentUserDb?.child("DateOfBirth")?.setValue(textView.text.toString())

        }

        textView.setOnClickListener {
            DatePickerDialog(this@ProfileActivity, dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        // access the items of the list
        val genders = resources.getStringArray(R.array.Gender)

        // access the spinner
        val spinner = findViewById<Spinner>(R.id.sp_Gender)

        if (spinner != null) {
            val adapter = ArrayAdapter(this,
                    android.R.layout.simple_spinner_item, genders)
            spinner.adapter = adapter


            spinner.onItemSelectedListener = object :
                    AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {

                    (parent.getChildAt(0) as TextView).setTextColor(Color.GRAY)
                    (parent.getChildAt(0) as TextView).textSize = 14f

                    currentUserDb?.child("Gender")?.setValue(sp_Gender.selectedItem.toString())
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    Toast.makeText(this@ProfileActivity, "Set your gender", Toast.LENGTH_LONG).show()
                }
            }
        }

        btnProfileEdit.setOnClickListener {
            btnProfileEdit.visibility = View.GONE

            tv_profileName.visibility = View.GONE
            et_profileName.visibility = View.VISIBLE

            tv_profileDOB_show.visibility = View.GONE
            tv_profileDOB.visibility = View.VISIBLE

            tv_Gender.visibility = View.GONE
            ll_Gender.visibility = View.VISIBLE

            tv_profileWeight.visibility = View.GONE
            et_profileWeight.visibility = View.VISIBLE

            tv_profileHeight.visibility = View.GONE
            et_profileHeight.visibility = View.VISIBLE
        }

        btnSaveInfo.setOnClickListener {
            if (TextUtils.isEmpty(et_profileName.text.toString())) {
                et_profileName.error = "Please enter your name"
                return@setOnClickListener
            } else if (TextUtils.isEmpty(et_profileWeight.text.toString())) {
                et_profileWeight.error = "Please enter your current weight"
                return@setOnClickListener
            } else if (TextUtils.isEmpty(et_profileHeight.text.toString())) {
                et_profileHeight.error = "Please enter your current height"
                return@setOnClickListener
            }

            saveUserInfo()

            startActivity(Intent(this@ProfileActivity, HomeActivity::class.java))
            finish()
        }
    }

    private fun setUserInfo() {
        et_profileName.setText(auth.currentUser?.displayName)
    }

    private fun saveUserInfo() {
        auth.currentUser?.let {
            val name = et_profileName.text.toString()

            val currentUser = auth.currentUser

            val currentUserDb = databaseReference?.child((currentUser?.uid!!))

            currentUserDb?.child("Name")?.setValue(et_profileName.text.toString())
            currentUserDb?.child("Weight")?.setValue(et_profileWeight.text.toString())
            currentUserDb?.child("Height")?.setValue(et_profileHeight.text.toString())

            val update = UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build()
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    it.updateProfile(update).await()

                    withContext(Dispatchers.Main) {
                        setUserInfo()

                        Toast.makeText(this@ProfileActivity, "Profile successfully updated.", Toast.LENGTH_SHORT).show()
                    }

                }catch (e:Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@ProfileActivity, e.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}


