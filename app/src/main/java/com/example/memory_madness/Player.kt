package com.example.memory_madness

import java.io.Serializable

data class Player (var name : String, var difficulty : String ,var pauseIsOn : String ,var time: Int, var moves : Int) : Serializable {
}