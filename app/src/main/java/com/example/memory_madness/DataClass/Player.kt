package com.example.memory_madness.DataClass

import java.io.Serializable

data class Player(
    var name: String?, var difficulty: String?,
    var pauseChoice: String?,
    var time: Int?, var moves: Int?
) : Serializable