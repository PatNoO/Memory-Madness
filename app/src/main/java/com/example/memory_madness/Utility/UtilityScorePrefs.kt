package com.example.memory_madness

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

fun savedPrefsScore (context: Context, players : MutableList<Player>){
    val scorePrefs = context.getSharedPreferences("score_prefs", Context.MODE_PRIVATE)
    val editor = scorePrefs.edit()

    val gson = Gson()
    val json = gson.toJson(players)

    editor.putString("player_score_key", json)
    editor.apply()

}

fun loadPrefsScore (context: Context) : MutableList<Player> {
    val scorePrefs = context.getSharedPreferences("score_prefs", Context.MODE_PRIVATE)
    val gson = Gson()

    val json = scorePrefs.getString("player_score_key", null)
    val type = object : TypeToken<MutableList<Player>>() {}.type

    val highScoreList : MutableList<Player> = gson.fromJson(json, type)

    return highScoreList

}