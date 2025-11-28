package com.example.memory_madness

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {

    val currentCard = MutableLiveData<CardManager>()
    val turnedCard = MutableLiveData<CardManager>()
    val cardPairCount = MutableLiveData<Int>()
    val moves = MutableLiveData<Int>()
    val timerCount = MutableLiveData<Int>()

//--------------------------------------------//
    fun startCount () {
        timerCount.value = (timerCount.value ?: 0) +1
    }
    fun increaseMoves () {
        moves.value = (moves.value ?: 0) +1
    }

    fun increaseCardPairCount () {
        cardPairCount.value = (cardPairCount.value ?: 0) +1
    }

    fun resetMoves () {
        moves.value = 0
    }

    fun resetCount () {
        timerCount.value = 0
    }

}