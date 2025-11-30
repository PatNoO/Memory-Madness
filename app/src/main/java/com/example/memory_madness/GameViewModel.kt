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
    // sets value from null to 0 and then start + 1
    fun startCount () {
    timerCount.value = timerCount.value?.minus(1)
//    timerCount.value?.let {
//        if (it >= 0){
//            timerCount.value?.minus(1)
//        }
//        timerCount.value = (timerCount.value ?: 0) +1
//    }
}
    fun setCountTime () {
        timerCount.value = 20
    }
    fun increaseMoves () {
        moves.value = (moves.value ?: 0) +1
    }

    fun increaseCardPairCount () {
        cardPairCount.value = (cardPairCount.value ?: 0) +1
    }

    fun resetCardPairCount () {
        cardPairCount.value = 0
    }

    fun resetMoves () {
        moves.value = 0
    }

    fun resetCount () {
        timerCount.value = 0
    }

}