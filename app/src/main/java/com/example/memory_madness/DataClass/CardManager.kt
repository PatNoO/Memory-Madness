package com.example.memory_madness.DataClass

import android.widget.ImageView

data class CardManager(
    var isFlipped: Boolean = false,
    var isMatched: Boolean = false,
    var cardId: Int,
    var containerId: ImageView
)
