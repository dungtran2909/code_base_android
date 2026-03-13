package com.dungtran.codebase.ui.features.welcome

import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dungtran.codebase.ui.theme.Code_base_androidTheme
import kotlinx.coroutines.launch

@Composable
fun WelcomeScreen(
    onFinish: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { onboardingPages.size })
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)
        .padding(start = 24.dp, end = 24.dp, bottom = 24.dp, top = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            state = pagerState,
            verticalAlignment = Alignment.Top
        ) { position ->
            OnboardingContent(page = onboardingPages[position])
        }

        Row(
            modifier = Modifier.padding(vertical = 40.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(onboardingPages.size) { iteration ->
                val color = if (pagerState.currentPage == iteration) Color(0xFFFFC107) else Color(0xFFFFE082)
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(color)
                )
            }
        }

        Button(
            onClick = {
                if (pagerState.currentPage < onboardingPages.size - 1) {
                    scope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                } else {
                    onFinish()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC107)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = if (pagerState.currentPage == onboardingPages.size - 1) "GET STARTED" else "NEXT",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = onFinish,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Skip", color = Color.Gray)
        }
    }
}

@Composable
fun OnboardingContent(page: OnboardingPage) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = painterResource(id = page.image),
            contentDescription = null,
            modifier = Modifier.size(300.dp),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.height(39.dp))
        Text(
            text = page.title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(39.dp))
        Text(
            text = page.description,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun WelcomeScreenPreview() {
    Code_base_androidTheme {
        // Chúng ta gọi WelcomeScreen với một lambda trống cho onFinish
        WelcomeScreen(onFinish = {})
    }
}
