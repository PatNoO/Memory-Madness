package com.example.memory_madness.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import com.example.memory_madness.Player
import com.example.memory_madness.PlayerViewModel
import com.example.memory_madness.databinding.FragmentHightScoreBinding
import com.example.memory_madness.loadPrefsScore


class HighScoreFragment : Fragment() {

    lateinit var adapter : ArrayAdapter<String>
    private lateinit var playerViewModel: PlayerViewModel
    private lateinit var binding : FragmentHightScoreBinding

    private var playersList = mutableListOf<Player>()

    private var highScoreList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        playerViewModel = ViewModelProvider(requireActivity())[PlayerViewModel::class.java]
        super.onCreate(savedInstanceState)

//        val demoPlayers = listOf<Player>(Player("Erik","easy","on",10,12),
//            Player("Johan","medium","off",15,16),
//            Player("Lollo","Hard","on",5,26))
        adapter.notifyDataSetChanged()
        adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1,highScoreList)

        playersList = loadPrefsScore(requireContext())
        for (player in playersList){
            highScoreList.add("Name : ${player.name}, Difficulty : ${player.difficulty}, pause help : ${player.pauseIsOn}, Time : ${player.time}, Moves : ${player.moves} " )
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHightScoreBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lvHighScoreFhs.adapter = adapter

    }

}