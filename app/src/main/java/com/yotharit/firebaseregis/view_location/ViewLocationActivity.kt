package com.yotharit.firebaseregis.view_location

import android.Manifest
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.yotharit.firebaseregis.R
import kotlinx.android.synthetic.main.view_location_layout.*
import androidx.core.os.HandlerCompat.postDelayed
import android.widget.Toast

class ViewLocationActivity : AppCompatActivity(), View.OnClickListener {

    private val REQUEST_CODE_PERMISSION = 2
    var mPermission = Manifest.permission.ACCESS_FINE_LOCATION

    var gps: GPSTracker? = null
    private var counter = 1

    // Thread
    var h = Handler()
    var task: Thread? = null
    private var startTime: Long = 0
    private var timeString: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_location_layout)
        setUpView()
    }

    override fun onClick(v: View?) {
        when (v) {
            startBtn -> {
                start()
                counter = 1
            }
            stopBtn -> stop()
        }
    }

    fun stop() {
        h.removeCallbacks(task);
        counterTv.setText("Location service is stopped.");
    }

    fun setUpView() {
        startBtn.setOnClickListener(this)
        stopBtn.setOnClickListener(this)
    }

    fun start() {
        startTime = System.currentTimeMillis()
        task = object : Thread() {
            override fun run() {
                val mills = System.currentTimeMillis() - startTime
                val secs = mills / 1000 % 60

                timeString = String.format("%02d", secs)
                runOnUiThread {
                    counterTv.text = timeString
                    if (secs % 5 == 0L) {
                        gps = GPSTracker(this@ViewLocationActivity)
                        if (gps!!.isCanGetLocation) {
                            val latitude = gps!!.getLatitude()
                            val longitude = gps!!.getLongitude()

                            Toast.makeText(
                                applicationContext, counter.toString() + "\nCurrent location is \n Lat: " + latitude +
                                        "\nLong: " + longitude, Toast.LENGTH_LONG
                            ).show()
                        } else {
                            gps!!.showSettingsAlert()
                        }
                        counter++
                    }
                }
                h.postDelayed(task, 1000)
            }
        }
        h.postDelayed(task, 1000)
    }
}