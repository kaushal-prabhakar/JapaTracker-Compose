package com.kaushal.japacountercompose.ui

import androidx.annotation.StringRes
import com.kaushal.japacountercompose.R

enum class JapaAppScreens(@StringRes val title: Int) {
    welcome(title = R.string.app_name),
    addJapa(title = R.string.app_name),
    japaList(title = R.string.app_name),
    japaDetails(title = R.string.app_name);

    companion object {
        const val JAPA_DETAILS_ROUTE = "japaDetails/{japaId}"
        const val JAPA_ID_ARG = "japaId"
    }
}
