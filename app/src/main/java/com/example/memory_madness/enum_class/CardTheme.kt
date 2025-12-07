package com.example.memory_madness.enum_class

import com.example.memory_madness.R

enum class CardTheme (val themeSet : List<Int>) {

    HALLOWEEN_THEME (
        themeSet = listOf(
            R.drawable.card1, R.drawable.card2, R.drawable.card3,
            R.drawable.card4, R.drawable.card5, R.drawable.card6,
            R.drawable.card7, R.drawable.card8, R.drawable.card9
        )
    ),

    CHRISTMAS_THEME (
        themeSet = listOf(
            R.drawable.xmas1, R.drawable.xmas2, R.drawable.xmas3,
            R.drawable.xmas4, R.drawable.xmas5, R.drawable.xmas6,
            R.drawable.xmas7, R.drawable.xmas8, R.drawable.xmas9
        )
    ),

    EASTER_THEME (
        themeSet = listOf(
            R.drawable.easter1, R.drawable.easter2, R.drawable.easter3,
            R.drawable.easter4, R.drawable.easter5, R.drawable.easter6,
            R.drawable.easter7, R.drawable.easter8, R.drawable.easter9
        )
    ),

    STPATRICKSDAY_THEME (
        themeSet = listOf(
            R.drawable.stpday1, R.drawable.stpday2, R.drawable.stpday3,
            R.drawable.stpday4, R.drawable.stpday5, R.drawable.stpday6,
            R.drawable.stpday7, R.drawable.stpday8, R.drawable.stpday9
        )
    )

}