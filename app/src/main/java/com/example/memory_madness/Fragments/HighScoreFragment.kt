package com.example.memory_madness.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import com.example.memory_madness.DataClass.Player
import com.example.memory_madness.Fragments.game_play.EasyFragment
import com.example.memory_madness.Fragments.game_play.HardFragment
import com.example.memory_madness.Fragments.game_play.MediumFragment
import com.example.memory_madness.R
import com.example.memory_madness.ViewModell.PlayerViewModel
import com.example.memory_madness.databinding.FragmentHightScoreBinding
import com.example.memory_madness.Utility.loadPrefsScore


class HighScoreFragment : Fragment() {

    lateinit var adapter: ArrayAdapter<String>
    private lateinit var playerViewModel: PlayerViewModel
    private lateinit var binding: FragmentHightScoreBinding
    private var playersList = mutableListOf<Player>()
    private var highScoreList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        playerViewModel = ViewModelProvider(requireActivity())[PlayerViewModel::class.java]
        super.onCreate(savedInstanceState)

//        val demoPlayers = listOf<Player>(Player("Erik","easy","on",10,12),
//            Player("Johan","medium","off",15,16),
//            Player("Lollo","Hard","on",5,26))


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHightScoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val minutes = playerViewModel.player.value?.time?.div(60)
        val seconds = playerViewModel.player.value?.time?.rem(60)

        playersList = loadPrefsScore(requireContext())
        for (player in playersList) {
            highScoreList.add("Name : ${player.name}, Difficulty : ${player.difficulty},\n pause help : ${player.pauseChoice},\n Time Left : $minutes : $seconds , Moves : ${player.moves} ")
        }

        adapter = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_list_item_1,
            highScoreList
        )
        adapter.notifyDataSetChanged()

//        highScoreList.sort()

        binding.lvHighScoreFhs.adapter = adapter

        binding.btnPlayAgainFsh.setOnClickListener {
            if (playerViewModel.player.value?.difficulty == "easy") {
                parentFragmentManager.beginTransaction().apply {
                    replace(R.id.fcv_game_plan_am, EasyFragment())
                    commit()
                }
            } else if (playerViewModel.player.value?.difficulty == "medium") {
                parentFragmentManager.beginTransaction().apply {
                    replace(R.id.fcv_game_plan_am, MediumFragment())
                    commit()
                }

            } else if (playerViewModel.player.value?.difficulty == "hard") {
                parentFragmentManager.beginTransaction().apply {
                    replace(R.id.fcv_game_plan_am, HardFragment())
                    commit()
                }
            } else {
                parentFragmentManager.beginTransaction().apply {
                    replace(R.id.fcv_game_plan_am, EasyFragment())
                    commit()
                }
            }
        }

        binding.btnHomeFsh.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.fcv_game_plan_am, HomeMenuFragment())
                commit()
            }
        }

    }

}