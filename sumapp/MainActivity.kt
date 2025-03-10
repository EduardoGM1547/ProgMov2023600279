package com.example.Sumapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SumaApp()
        }
    }
}

@Composable
fun SumaApp() {
    var cadTxtOp1 by remember { mutableStateOf("") }
    var cadTxtOp2 by remember { mutableStateOf("") }
    var cadTxtRes by remember { mutableStateOf("") }

    fun btnLimpiar_click() {
        cadTxtOp1 = ""
        cadTxtOp2 = ""
        cadTxtRes = ""
    }

    fun btnSumar_click(){
        val op1 = cadTxtOp1.toIntOrNull() ?: 0
        val op2 = cadTxtOp2.toIntOrNull() ?: 0
        cadTxtRes = (op1 + op2).toString()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            Text("Op1", Modifier.weight(1f))
            Text("Op2", Modifier.weight(1f))
            Text("Res", Modifier.weight(1f))
        }
        Row(Modifier.fillMaxWidth(), Arrangement.SpaceAround) {
            TextField(value = cadTxtOp1, onValueChange = { cadTxtOp1 = it }, Modifier.weight(1f))
            TextField(value = cadTxtOp2, onValueChange = { cadTxtOp2 = it }, Modifier.weight(1f))
            TextField(value = cadTxtRes, onValueChange = {}, Modifier.weight(1f), readOnly = true)
        }
        Row(Modifier.fillMaxWidth(), Arrangement.SpaceAround) {
            Button(onClick = { btnLimpiar_click() }, Modifier.weight(1.5f)) {
                Text("Limpiar")
            }
            Button(onClick = { btnSumar_click() }, Modifier.weight(1.5f)) {
                Text("Sumar")
            }
        }
    }
}