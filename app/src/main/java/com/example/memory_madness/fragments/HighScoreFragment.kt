package com.example.memory_madness.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.memory_madness.data_class.Player
import com.example.memory_madness.fragments.game_play.EasyFragment
import com.example.memory_madness.fragments.game_play.HardFragment
import com.example.memory_madness.fragments.game_play.MediumFragment
import com.example.memory_madness.adapter.HighScoreRecyclerAdapter
import com.example.memory_madness.R
import com.example.memory_madness.view_model.PlayerViewModel
import com.example.memory_madness.databinding.FragmentHightScoreBinding
import com.example.memory_madness.utility.loadPrefsScore


class HighScoreFragment : Fragment() {

    private lateinit var adapter: HighScoreRecyclerAdapter
    private lateinit var playerViewModel: PlayerViewModel
    private lateinit var binding: FragmentHightScoreBinding
    private var playersList = mutableListOf<Player>()
    private var highScoreListEasy = mutableListOf<Player>()
    private var highScoreListMedium = mutableListOf<Player>()
    private var highScoreListHard = mutableListOf<Player>()

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

        initHighScoreList()

        initAdapter()

        showChosenHighScoreListButtons()

        playAgainButton()

        homeMenuButton()
    }

    /**
     * initiates Adapter
     * sets default adapter to highScoreEasy
     */
    private fun initAdapter() {
        adapter = HighScoreRecyclerAdapter(highScoreListEasy)
        binding.rvHighScoreFhs.layoutManager = LinearLayoutManager(requireContext())
        binding.rvHighScoreFhs.adapter = adapter
    }

    /**
     * Buttons to show Easy Medium or Hard HighScore List
     */
    private fun showChosenHighScoreListButtons() {
        binding.btnShowEasyFsh.setOnClickListener {
            adapter.updateList(highScoreListEasy)
        }
        binding.btnShowMediumFsh.setOnClickListener {
            adapter.updateList(highScoreListMedium)
        }
        binding.btnShowHardFsh.setOnClickListener {
            adapter.updateList(highScoreListHard)
        }
    }

    /**
     * Player Plays again
     * Checks player difficulty
     */
    private fun playAgainButton() {
        binding.btnPlayAgainFsh.setOnClickListener {
            when (playerViewModel.player.value?.difficulty) {
                "easy" -> {
                    parentFragmentManager.beginTransaction().apply {
                        replace(R.id.fcv_game_plan_am, EasyFragment())
                        commit()
                    }
                }
                "medium" -> {
                    parentFragmentManager.beginTransaction().apply {
                        replace(R.id.fcv_game_plan_am, MediumFragment())
                        commit()
                    }

                }
                "hard" -> {
                    parentFragmentManager.beginTransaction().apply {
                        replace(R.id.fcv_game_plan_am, HardFragment())
                        commit()
                    }
                }
                else -> {
                    parentFragmentManager.beginTransaction().apply {
                        replace(R.id.fcv_game_plan_am, EasyFragment())
                        commit()
                    }
                }
            }
        }
    }

    /**
     * Player goes to HomeMenu
     */
    private fun homeMenuButton() {
        binding.btnHomeFsh.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.fcv_game_plan_am, HomeMenuFragment())
                commit()
            }
        }
    }

    /**
     * Initiates the list with correct score from Player difficulty
     */
    fun initHighScoreList() {
        playersList = loadPrefsScore(requireContext())
        playersList.sortBy { it.moves }
        for (player in playersList) {
            when (player.difficulty) {
                "easy" -> highScoreListEasy.add(player)
                "medium" -> highScoreListMedium.add(player)
                "hard" -> highScoreListHard.add(player)
            }
        }
    }

}