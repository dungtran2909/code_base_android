package com.dungtran.codebase.ui.features.welcome

import androidx.annotation.DrawableRes
import com.dungtran.codebase.R

data class OnboardingPage(
    /*@DrawableRes */val image: Int, 
    val title: String,
    val description: String
)

val onboardingPages = listOf(
    OnboardingPage(
        image = R.drawable.img_welcome_step_1, 
        title = "All your favorites",
        description = "Get all your loved foods in one once place, you just place the order we do the rest"
    ),
    OnboardingPage(
        image = R.drawable.img_welcome_step_2,
        title = "Order from choosen chef",
        description = "Get all your loved foods in one once place, you just place the order we do the rest"
    ),
    OnboardingPage(
        image = R.drawable.img_welcome_step_3,
        title = "Free delivery offers",
        description = "Get all your loved foods in one once place, you just place the order we do the rest"
    )
)