package com.kaushal.japacountercompose.ui

import androidx.annotation.StringRes
import com.kaushal.japacountercompose.R

enum class JapaAppScreens(@StringRes val title: Int) {
    welcome(title = R.string.app_name),
    addJapa(title = R.string.app_name),
    japaList(title = R.string.app_name),
    japDetails(title = R.string.app_name)
}