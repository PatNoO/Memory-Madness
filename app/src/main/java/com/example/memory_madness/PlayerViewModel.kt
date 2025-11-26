package com.example.memory_madness

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PlayerViewModel : ViewModel() {

     val name = MutableLiveData<String>()
     val difficulty = MutableLiveData<String>()
     val time = MutableLiveData<Int>()
     val moves = MutableLiveData<Int>()
}