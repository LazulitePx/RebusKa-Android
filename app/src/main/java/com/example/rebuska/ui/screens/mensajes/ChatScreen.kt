package com.example.rebuska.ui.screens.mensajes

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Send
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.statusBars
import com.example.rebuska.R

@Composable
fun ChatScreen(nombre: String, onBack: () -> Unit) {

    var message by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEFEFEF))
    ) {


        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
                .background(Color(0xFF1976D2))
        )


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1976D2))
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null,
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.width(6.dp))

            Image(
                painter = painterResource(id = R.drawable.logo_carpinteria),
                contentDescription = null,
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = nombre,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
        }

        Text(
            text = "3 de marzo",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(8.dp),
            color = Color.Gray,
            fontSize = 12.sp
        )


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(12.dp)
        ) {


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .background(Color(0xFF1976D2), RoundedCornerShape(12.dp))
                    .padding(14.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("✔", color = Color.White, fontSize = 20.sp)

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        "Carpintería López ha aceptado tu solicitud de servicio",
                        color = Color.White,
                        fontSize = 13.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))


            UserMsg("Buenas tardes", "10:30")

            BusinessMsg("Buenas tardes Luis, en qué puedo colaborarle", "10:31")

            UserMsg("Quiero encargar un armario", "10:32")

            BusinessMsg("Claro con gusto, ¿qué medidas necesita y qué tipo de madera prefiere?", "10:33")
        }


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF5F5F5))
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            TextField(
                value = message,
                onValueChange = { message = it },
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(50)),
                placeholder = { Text("Escribe un mensaje") },
                shape = RoundedCornerShape(50),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(onClick = { message = "" }) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = null,
                    tint = Color(0xFF1976D2)
                )
            }
        }
    }
}

@Composable
fun UserMsg(text: String, time: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.End
    ) {
        Box(
            modifier = Modifier
                .background(Color(0xFF1976D2), RoundedCornerShape(12.dp))
                .padding(10.dp)
        ) {
            Text(
                text,
                color = Color.White
            )
        }

        Text(time, fontSize = 10.sp, color = Color.Gray)
    }

    Spacer(modifier = Modifier.height(6.dp))
}

@Composable
fun BusinessMsg(text: String, time: String) {
    Row(
        verticalAlignment = Alignment.Top
    ) {

        Image(
            painter = painterResource(id = R.drawable.logo_carpinteria),
            contentDescription = null,
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Box(
                modifier = Modifier
                    .background(Color.White, RoundedCornerShape(12.dp))
                    .padding(10.dp)
            ) {
                Text(text)
            }

            Spacer(modifier = Modifier.height(2.dp))

            Text(time, fontSize = 10.sp, color = Color.Gray)
        }
    }

    Spacer(modifier = Modifier.height(6.dp))
}