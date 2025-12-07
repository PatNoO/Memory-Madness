package com.example.memory_madness.data_class

import com.example.memory_madness.enum_class.CardTheme
import java.io.Serializable

data class Player(
    var name: String?, var difficulty: String?,
    var pauseChoice: String?,
    var time: Int?, var moves: Int?,
    var theme: CardTheme?
) : Serializable