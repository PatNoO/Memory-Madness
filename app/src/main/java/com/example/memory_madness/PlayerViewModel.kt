package com.example.memory_madness

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PlayerViewModel : ViewModel() {


    private val _player = MutableLiveData<Player>()
    val player : LiveData<Player> = _player

    fun setName (newPlayerName : Player) {
        _player.value = newPlayerName
    }

//    private var _name = MutableLiveData<String>()
//     var name : LiveData<String> = _name
//
//    private val _difficulty = MutableLiveData<String>()
//     val difficulty : LiveData<String> = _difficulty
//
//    private val _time = MutableLiveData<Int>()
//     val time : LiveData<Int> = _time
//
//    private val _moves = MutableLiveData<Int>()
//     val moves : LiveData<Int> = _moves
//
//   //----------------------------------------------//
//
//    fun setMoves (totalMoves : Int){
//        _moves.value = totalMoves
//    }
//
//    fun setName (playerName : String){
//        _name.value = playerName
//    }
//
//    fun setTime (newTime : Int) {
//        _time.value = newTime
//    }
}