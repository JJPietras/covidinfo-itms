package com.a03.zadanie4

import android.graphics.drawable.Drawable
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.Image
import android.opengl.Visibility
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlin.math.sqrt
import kotlin.random.Random


class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometer: Sensor

    private lateinit var oneBtn: Button
    private lateinit var twoBtn: Button
    private lateinit var threeBtn: Button

    private lateinit var firstImg: ImageView
    private lateinit var secondImg: ImageView
    private lateinit var thirdImg: ImageView

    private lateinit var dices: Array<Drawable?>

    private val SHAKE_THRESHOLD = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        oneBtn = findViewById(R.id.one)
        twoBtn = findViewById(R.id.two)
        threeBtn = findViewById(R.id.three)

        oneBtn.setOnClickListener { diceCount(1) }
        twoBtn.setOnClickListener { diceCount(2) }
        threeBtn.setOnClickListener { diceCount(3) }

        firstImg = findViewById(R.id.first)
        secondImg = findViewById(R.id.second)
        thirdImg = findViewById(R.id.third)

        dices = arrayOf(
            ContextCompat.getDrawable(this, R.drawable.onedice),
            ContextCompat.getDrawable(this, R.drawable.twodice),
            ContextCompat.getDrawable(this, R.drawable.threedice),
            ContextCompat.getDrawable(this, R.drawable.fourdice),
            ContextCompat.getDrawable(this, R.drawable.fivedice),
            ContextCompat.getDrawable(this, R.drawable.sixdice)
        )

        rollAllDices()
    }

    override fun onSensorChanged(event: SensorEvent?) {
        val x = event?.values?.get(0)
        val y = event?.values?.get(1)
        val z = event?.values?.get(2)

        if (x != null && y != null && z != null) {
            val acceleration = sqrt(x * x + y * y + z * z) - SensorManager.GRAVITY_EARTH
            if (acceleration > SHAKE_THRESHOLD) {
                rollAllDices()
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    private fun rollAllDices() {
        generateRandomNumber(firstImg)
        generateRandomNumber(secondImg)
        generateRandomNumber(thirdImg)
    }

    private fun generateRandomNumber(dice: ImageView) {
        val randomGenerator = Random
        val randomNum = randomGenerator.nextInt(6)
        dice.setImageDrawable(dices[randomNum])
    }

    private fun diceCount(count: Int) {
        when (count) {
            1 -> {
                firstImg.visibility = View.VISIBLE
                secondImg.visibility = View.INVISIBLE
                thirdImg.visibility = View.INVISIBLE
            }
            2 -> {
                firstImg.visibility = View.VISIBLE
                secondImg.visibility = View.VISIBLE
                thirdImg.visibility = View.INVISIBLE
            }
            3 -> {
                firstImg.visibility = View.VISIBLE
                secondImg.visibility = View.VISIBLE
                thirdImg.visibility = View.VISIBLE
            }
        }

    }
}