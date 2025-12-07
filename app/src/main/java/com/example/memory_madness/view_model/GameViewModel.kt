package com.example.memory_madness.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.memory_madness.data_class.CardManager

class GameViewModel : ViewModel() {

    val currentCard = MutableLiveData<CardManager>()
    val turnedCard = MutableLiveData<CardManager>()
    val cardPairCount = MutableLiveData<Int>()

    val moves = MutableLiveData<Int>()
    val timerCount = MutableLiveData<Int?>()

    fun startCountDown() {
        timerCount.value?.let {
            if (it >= 0) {
                timerCount.value = timerCount.value?.minus(1)
            }
        }
    }

    fun increaseTimerCount() {
        timerCount.value = (timerCount.value ?: 0) + 5
    }

    fun setCountTime(startCountNumber: Int?) {
        timerCount.value = startCountNumber
    }

    fun increaseMoves() {
        moves.value = (moves.value ?: 0) + 1
    }

    fun increaseCardPairCount() {
        cardPairCount.value = (cardPairCount.value ?: 0) + 1
    }

    fun resetCardPairCount() {
        cardPairCount.value = 0
    }

    fun resetMoves() {
        moves.value = 0
    }

    fun resetCount() {
        timerCount.value = 0
    }

}