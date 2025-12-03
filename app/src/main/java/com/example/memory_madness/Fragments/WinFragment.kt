package com.example.memory_madness.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.memory_madness.Fragments.game_play.EasyFragment
import com.example.memory_madness.Fragments.game_play.HardFragment
import com.example.memory_madness.Fragments.game_play.MediumFragment
import com.example.memory_madness.ViewModell.GameViewModel
import com.example.memory_madness.DataClass.Player
import com.example.memory_madness.ViewModell.PlayerViewModel
import com.example.memory_madness.R
import com.example.memory_madness.Utility.loadPrefsScore
import com.example.memory_madness.databinding.FragmentWinBinding
import com.example.memory_madness.Utility.savedPrefsScore

class WinFragment : Fragment(R.layout.fragment_win) {

    private lateinit var playerViewModel: PlayerViewModel
    private lateinit var gameViewModel: GameViewModel
    private lateinit var binding: FragmentWinBinding

    //    private var highScoreList = mutableListOf<String>()
    private var playersList = mutableListOf<Player>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWinBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playerViewModel = ViewModelProvider(requireActivity())[PlayerViewModel::class.java]
        gameViewModel = ViewModelProvider(requireActivity())[GameViewModel::class.java]

        binding.tvStatsFw.text = "Score"

        val totalMoves = gameViewModel.moves.value
        val totalTime = gameViewModel.timerCount.value

        if (totalTime != null) {
            playerViewModel.player.value?.time = totalTime
        }
        if (totalMoves != null) {
            playerViewModel.player.value?.moves = totalMoves
        }


        val minutes = totalTime?.div(60)
        val seconds = totalTime?.rem(60)
        binding.tvMovesFw.text = "Moves : $totalMoves "
        binding.tvTimeFw.text = "Time : $minutes : $seconds"

        binding.btnSaveScoreFw.setOnClickListener {
            playersList = loadPrefsScore(requireContext())

            playerViewModel.player.let { player ->
                val name = player.value?.name
                val difficulty = player.value?.difficulty
                val pauseHelp = player.value?.pauseChoice
                val time = player.value?.time
                val moves = player.value?.moves
                playersList.add(Player(name, difficulty, pauseHelp, time, moves))

                savedPrefsScore(requireContext(), playersList)


            }
        }

        binding.btnHighScoreFw.setOnClickListener {
            gameViewModel.resetCount()
            gameViewModel.resetMoves()
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.fcv_game_plan_am, HighScoreFragment())
                commit()
            }
        }

        binding.btnPlayAgainFw.setOnClickListener {
            gameViewModel.resetCount()
            gameViewModel.resetMoves()
            if (playerViewModel.player.value?.difficulty == "easy") {
                parentFragmentManager.beginTransaction().apply {
                    replace(R.id.fcv_game_plan_am, EasyFragment())
                    commit()
                }
            } else if (playerViewModel.player.value?.difficulty == "medium") {
                gameViewModel.resetCount()
                gameViewModel.resetMoves()
                parentFragmentManager.beginTransaction().apply {
                    replace(R.id.fcv_game_plan_am, MediumFragment())
                    commit()
                }
            } else if (playerViewModel.player.value?.difficulty == "hard") {
                gameViewModel.resetCount()
                gameViewModel.resetMoves()
                parentFragmentManager.beginTransaction().apply {
                    replace(R.id.fcv_game_plan_am, HardFragment())
                    commit()
                }
            } else {
                gameViewModel.resetCount()
                gameViewModel.resetMoves()
                parentFragmentManager.beginTransaction().apply {
                    replace(R.id.fcv_game_plan_am, EasyFragment())
                    commit()
                }
            }
        }

        binding.btnHomeFw.setOnClickListener {
            gameViewModel.resetCount()
            gameViewModel.resetMoves()
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.fcv_game_plan_am, HomeMenuFragment())
                commit()
            }
        }
    }

}