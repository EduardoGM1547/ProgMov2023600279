package com.example.prollectomantekilla

import android.Manifest
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.sqrt
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.graphics.Color



class MainActivity : ComponentActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private var lastAcceleration = 0f
    private var currentAcceleration = 0f
    private var shakeThreshold = 15f
    private var screenState by mutableIntStateOf(0)
    private var isShakeEnabled by mutableStateOf(true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)

        setContent {
            Prollectomantekilla(screenState) { newScreenState ->
                screenState = newScreenState
            }
        }
    }

    @RequiresPermission(Manifest.permission.VIBRATE)
    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER && isShakeEnabled) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            lastAcceleration = currentAcceleration
            currentAcceleration = sqrt((x * x + y * y + z * z).toDouble()).toFloat()
            val delta = currentAcceleration - lastAcceleration

            if (delta > shakeThreshold) {
                isShakeEnabled = false
                when (screenState) {
                    0 -> {
                        screenState = 1
                        playSoundAndVibrate(R.raw.mantekilla)
                    }
                    2 -> {
                        screenState = 3
                        playSoundAndVibrate(R.raw.falleze) // Sonido para el cambio a pantalla 3
                    }
                }
                CoroutineScope(Dispatchers.Main).launch {
                    delay(2000) // Retraso antes de habilitar el sensor nuevamente
                    isShakeEnabled = true
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    private fun playSound(soundRes: Int) {
        val mediaPlayer = MediaPlayer.create(this, soundRes)
        mediaPlayer.start()
    }

    @RequiresPermission(Manifest.permission.VIBRATE)
    private fun playSoundAndVibrate(soundRes: Int) {
        playSound(soundRes)
        val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
    }
}

@Composable
fun Prollectomantekilla(screenState: Int, onScreenChange: (Int) -> Unit) {
    val context = LocalContext.current
    var shotCount by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (screenState) {
            0 -> {
                Text(
                    text = "Presiona al zombie",
                    fontSize = 28.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
                Image(
                    painter = painterResource(id = R.drawable.karakono),
                    contentDescription = "Zombie con cono",
                    modifier = Modifier
                        .size(200.dp)
                        .clickable {
                            val mediaPlayer = MediaPlayer.create(context, R.raw.karakono)
                            mediaPlayer.start()
                        }
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "y luego agita tu teléfono",
                    fontSize = 20.sp
                )
            }
            1 -> {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.lanzamaisss),
                        contentDescription = "Lanza maíz",
                        modifier = Modifier.size(150.dp)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.enmantekillado),
                        contentDescription = "Zombie con mantequilla",
                        modifier = Modifier.size(150.dp)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = {
                        shotCount++

                        // Reproducir sonido "mantekilla"
                        val mediaPlayerMantekilla = MediaPlayer.create(context, R.raw.mantekilla)
                        mediaPlayerMantekilla.start()

                        mediaPlayerMantekilla.setOnCompletionListener {
                            // Solo ejecutar esta lógica cuando termine "mantekilla"
                            if (shotCount >= 2) {
                                // Reproducir sonido "mantekillacono" al mismo tiempo que cambiamos de pantalla
                                val mediaPlayerMakKono =
                                    MediaPlayer.create(context, R.raw.mantekillakono)
                                mediaPlayerMakKono.start()

                                // Cambiar pantalla después de iniciar el sonido "mantekillacono"
                                onScreenChange(2)
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE66C02), // Fondo del botón en tono naranja
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "Disparar ($shotCount/2)") // Actualiza el texto también
                }
            }
            2 -> {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.konomantekilla),
                        contentDescription = "Zombie con cono dañado",
                        modifier = Modifier.size(150.dp)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.konoroto),
                        contentDescription = "Zombie con cono roto",
                        modifier = Modifier.size(150.dp)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp)) // Mueve el Spacer afuera del Row
                Text(
                    text = "Agita una vez más",
                    fontSize = 20.sp
                )
            }

            3 -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.fallezido),
                        contentDescription = "Zombie muerto",
                        modifier = Modifier.size(200.dp)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // Botón "Finalizar"
                        Button(
                            onClick = {
                                (context as? MainActivity)?.finish() // Finaliza la actividad
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFE66C02), // Fondo con el tono e66c02
                                contentColor = Color.White          // Texto blanco
                            )
                        ) {
                            Text(text = "Finalizar")
                        }


                        // Botón "Repetir"
                        Button(
                            onClick = {
                                shotCount = 0 // Reinicia el contador a 0
                                onScreenChange(0) // Vuelve al estado inicial
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFE66C02), // Fondo en tono naranja
                                contentColor = Color.White          // Texto en blanco
                            )
                        ) {
                            Text(text = "Repetir")
                        }
                    }
                }
            }

        }

    }
}

private fun playSound(context: Context, soundRes: Int) {
    val mediaPlayer = MediaPlayer.create(context, soundRes)
    mediaPlayer.start()
}