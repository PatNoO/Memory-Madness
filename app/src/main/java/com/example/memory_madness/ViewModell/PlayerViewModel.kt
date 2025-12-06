package com.example.memory_madness.ViewModell

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.memory_madness.Activitys.EnumClass.CardTheme
import com.example.memory_madness.DataClass.Player

class PlayerViewModel : ViewModel() {


    private val _player = MutableLiveData<Player>()
    val player: LiveData<Player> = _player

    fun setTheme (playerTheme: CardTheme) {
        _player.value?.theme = playerTheme
    }

    fun setName(playerName: Player) {
        _player.value = playerName
    }

    fun changeName (newPlayerName : String) {
        _player.value?.name = newPlayerName
    }

    fun setDifficulty(difficultyChoice: Player) {
        _player.value = difficultyChoice
    }

    fun enablePause(pauseChoice: Player) {
        _player.value = pauseChoice
    }

    fun updatePause(pauseChoice : String) {
        _player.value?.pauseChoice = pauseChoice
    }
}