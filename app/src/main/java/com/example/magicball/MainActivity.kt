
package com.example.magicball

import androidx.compose.ui.graphics.drawscope.drawCircle
import androidx.compose.foundation.Canvas
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.drawLine
import kotlin.random.Random
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MagicBallGame()
        }
    }
}

@Composable
fun StarryBackground() {
    val stars = remember {
        List(200) {
            Offset(
                Random.nextFloat() * 2000f,
                Random.nextFloat() * 4000f
            )
        }
    }
            

    var shootingX by remember { mutableStateOf(-300f) }
    var shootingY by remember { mutableStateOf(250f) }

    LaunchedEffect(Unit) {
        while (true) {
            delay((3000..7000).random().toLong())

            shootingX = -200f
            shootingY = Random.nextInt(100, 900).toFloat()

            while (shootingX < 2200f) {
                shootingX += 40f
                shootingY += 18f
                delay(16)
            }
        }
    }

    Canvas(
        modifier = Modifier.fillMaxSize()
    ) {

        stars.forEach {
            drawCircle(
                color = Color.White,
                radius = Random.nextFloat() * 2f + 1f,
                center = it
            )
        }
    
        drawLine(
            color = Color.White,
            start = Offset(shootingX, shootingY),
            end = Offset(shootingX - 150f, shootingY - 75f),
            strokeWidth = 4f
        ) 
    }
}

@Composable
fun MagicBallGame() {
    var question by remember { mutableStateOf("") }
    var answer by remember { mutableStateOf("") }
    var shaking by remember { mutableStateOf(false) }

    val rotation by animateFloatAsState(
        targetValue = if (shaking) 12f else 0f,
        animationSpec = tween(120),
        label = "rotation"
    )

    val answers = listOf(
        "✨ YES, DEFINITELY!",
        "🔮 THE FUTURE IS BRIGHT!",
        "🌙 ASK AGAIN LATER.",
        "⭐ WITHOUT A DOUBT!",
        "👻 MY SOURCES SAY NO.",
        "💫 VERY LIKELY!",
        "🌌 THE UNIVERSE SAYS YES!",
        "🪄 TRUST YOUR INSTINCTS!",
        "🌠 SIGNS POINT TO YES.",
        "🌑 DON'T COUNT ON IT."
    )

    val scope = rememberCoroutineScope()

   Box(
    modifier = Modifier.fillMaxSize()
) {

    // Night sky background
    Box(
        modifier = Modifier
            .matchParentSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF02030A),
                        Color(0xFF08122E),
                        Color(0xFF000814)
                    )
                )
            )
    )

    // Stars and shooting stars
    StarryBackground()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {

       
         Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(22.dp)
        ) {
            Text(
                text = "✦ WELCOME TO CASPER'S MAGIC BALL! ✦",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE5C6FF)
            )

            Text(
                text = "Ask the universe...",
                fontSize = 17.sp,
                color = Color(0xFFD4B8F5)
            )

            // Magic Ball
            Box(
                modifier = Modifier
                    .size(230.dp)
                    .rotate(rotation)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF48445C),
                                Color(0xFF080610),
                                Color(0xFF252033)
                            )
                        ),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(125.dp)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFFB9A6FF),
                                    Color(0xFF4636A8),
                                    Color(0xFF17104C)
                                )
                            ),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (answer.isEmpty()) "8" else answer,
                        fontSize = if (answer.isEmpty()) 65.sp else 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }

            Text(
                text = "What would you like to know?",
                color = Color(0xFFE6D8FF)
            )

            BasicTextField(
                value = question,
                onValueChange = { question = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color(0xFF33204D),
                        RoundedCornerShape(14.dp)
                    )
                    .padding(18.dp),
                singleLine = true,
                textStyle = TextStyle(
                    color = Color.White,
                    fontSize = 16.sp
                ),
                decorationBox = { innerTextField ->
                    Box {
                        if (question.isEmpty()) {
                            Text(
                                "Type your question here...",
                                color = Color.LightGray
                            )
                        }
                        innerTextField()
                    }
                }
            )

            Button(
                onClick = {
                    if (question.isNotBlank() && !shaking) {
                        scope.launch {
                            shaking = true
                            answer = ""
                            delay(900)
                            answer = answers.random()
                            shaking = false
                        }
                    }
                },
                enabled = question.isNotBlank() && !shaking,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF9255D9)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
            ) {
                Text(
                    "✨ ASK THE UNIVERSE ✨",
                    fontWeight = FontWeight.Bold
                )
            }

            if (answer.isNotEmpty()) {
                Text(
                    "The universe has spoken!",
                    color = Color(0xFFE5C6FF),
                    fontSize = 17.sp
                )
            }

            TextButton(
                onClick = {
                    question = ""
                    answer = ""
                }
            ) {
                Text(
                    "↻ Ask another question",
                    color = Color(0xFFD8B5FF)
                )
            }
        }
    }
        }
    }
 } 
