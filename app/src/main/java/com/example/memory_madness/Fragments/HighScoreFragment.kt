package com.example.memory_madness.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
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
    private var highScoreListEasy = mutableListOf<String>()
    private var highScoreListMedium = mutableListOf<String>()
    private var highScoreListHard = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        playerViewModel = ViewModelProvider(requireActivity())[PlayerViewModel::class.java]
        super.onCreate(savedInstanceState)
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



        playersList = loadPrefsScore(requireContext())

        playersList.sortBy { it.moves }

        showHighScoreList()

        binding.btnShowEasyFsh.setOnClickListener {
            adapter = adapterEasy()
            binding.lvHighScoreFhs.adapter = adapterEasy()
        }

        binding.btnShowMediumFsh.setOnClickListener {
            adapter = adapterMedium()
            binding.lvHighScoreFhs.adapter = adapter
        }

        binding.btnShowHardFsh.setOnClickListener {

            adapter = adapterHard()
            binding.lvHighScoreFhs.adapter = adapter
        }

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

        // default adapter
        adapter = adapterEasy()
        binding.lvHighScoreFhs.adapter = adapter

    }

    private fun adapterEasy(): ArrayAdapter<String> {
        adapter = ArrayAdapter<String>(
            requireContext(),
            R.layout.simple_list_black_text,
            highScoreListEasy
        )
        adapter.notifyDataSetChanged()

        return adapter
    }

    private fun adapterMedium(): ArrayAdapter<String> {
        adapter = ArrayAdapter<String>(
            requireContext(),
            R.layout.simple_list_black_text,
            highScoreListMedium
        )
        adapter.notifyDataSetChanged()

        return adapter
    }

    private fun adapterHard(): ArrayAdapter<String> {
        adapter = ArrayAdapter<String>(
            requireContext(),
            R.layout.simple_list_black_text,
            highScoreListHard
        )
        adapter.notifyDataSetChanged()

        return adapter
    }

    fun showHighScoreList() {

        for (player in playersList) {
            if (player.difficulty == "easy") {
                val minutes = player.time?.div(60)
                val seconds = player.time?.rem(60)

                highScoreListEasy.add(
                    "Name : ${player.name}\n" +
                            "Difficulty : ${player.difficulty}\n" +
                            "Pause help : ${player.pauseChoice}\n" +
                            "Time Left : $minutes : $seconds\n" +
                            "Moves : ${player.moves}"
                )
            } else if (player.difficulty == "medium") {
                val minutes = player.time?.div(60)
                val seconds = player.time?.rem(60)

                highScoreListMedium.add(
                    "Name : ${player.name}\n" +
                            "Difficulty : ${player.difficulty}\n" +
                            "Pause help : ${player.pauseChoice}\n" +
                            "Time Left : $minutes : $seconds\n" +
                            "Moves : ${player.moves}"
                )
            } else if (player.difficulty == "hard") {
                val minutes = player.time?.div(60)
                val seconds = player.time?.rem(60)

                highScoreListHard.add(
                    "Name : ${player.name}\n" +
                            "Difficulty : ${player.difficulty}\n" +
                            "Pause help : ${player.pauseChoice}\n" +
                            "Time Left : $minutes : $seconds\n" +
                            "Moves : ${player.moves}"
                )
            }
        }
    }

}