package com.example.memory_madness.ViewModell

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.memory_madness.DataClass.Player

class PlayerViewModel : ViewModel() {


    private val _player = MutableLiveData<Player>()
    val player : LiveData<Player> = _player

    fun setName (newPlayerName : Player) {
        _player.value = newPlayerName
    }

    fun setDifficulty (difficultyChoice : Player) {
        _player.value = difficultyChoice
    }

    fun enablePause (pauseChoice : Player) {
        _player.value = pauseChoice
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