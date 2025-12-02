package com.example.memory_madness.Utility

import android.content.Context
import com.example.memory_madness.DataClass.Player
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import androidx.core.content.edit

fun savedPrefsScore (context: Context, players : List<Player>){
    val scorePrefs = context.getSharedPreferences("score_prefs", Context.MODE_PRIVATE)
    scorePrefs.edit {

        val gson = Gson()
        val json = gson.toJson(players)

        putString("player_score_key", json)
    }

}

fun loadPrefsScore (context: Context) : MutableList<Player> {
    val scorePrefs = context.getSharedPreferences("score_prefs", Context.MODE_PRIVATE)
    val gson = Gson()

    val json = scorePrefs.getString("player_score_key", null)
    val type = object : TypeToken<MutableList<Player>>() {}.type

    val highScoreList : MutableList<Player> = gson.fromJson(json, type)

    return highScoreList

}