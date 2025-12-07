package com.example.memory_madness.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.memory_madness.fragments.game_play.EasyFragment
import com.example.memory_madness.fragments.game_play.HardFragment
import com.example.memory_madness.fragments.game_play.MediumFragment
import com.example.memory_madness.view_model.GameViewModel
import com.example.memory_madness.data_class.Player
import com.example.memory_madness.view_model.PlayerViewModel
import com.example.memory_madness.R
import com.example.memory_madness.utility.loadPrefsScore
import com.example.memory_madness.databinding.FragmentWinBinding
import com.example.memory_madness.enum_class.CardTheme
import com.example.memory_madness.utility.savedPrefsScore

class WinFragment : Fragment() {

    private lateinit var playerViewModel: PlayerViewModel
    private lateinit var gameViewModel: GameViewModel
    private lateinit var binding: FragmentWinBinding
    private var playersList = mutableListOf<Player>()

    override fun onCreate(savedInstanceState: Bundle?) {
        playerViewModel = ViewModelProvider(requireActivity())[PlayerViewModel::class.java]
        gameViewModel = ViewModelProvider(requireActivity())[GameViewModel::class.java]
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWinBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showWinScore()

        showsCurrentTheme()

        saveScoreButton()

        highScoreButton()

        playAgainButton()

        homeMenuButton()
    }

    private fun showsCurrentTheme() {
        playerViewModel.player.observe(viewLifecycleOwner) {
            binding.cardThemeFw.setImageResource(playerViewModel.player.value?.theme!!.themeSet[1])

            when (playerViewModel.player.value?.theme) {
                CardTheme.HALLOWEEN_THEME -> {
                    binding.tvThemeFw.text = getString(R.string.halloween)
                }

                CardTheme.CHRISTMAS_THEME -> {
                    binding.tvThemeFw.text = getString(R.string.christmas)
                }

                CardTheme.EASTER_THEME -> {
                    binding.tvThemeFw.text = getString(R.string.easter)
                }

                CardTheme.STPATRICKSDAY_THEME -> {
                    binding.tvThemeFw.text = getString(R.string.st_patrick_s_day)
                }

                else -> {}
            }
        }
    }

    private fun showWinScore() {
        binding.tvStatsFw.text = getString(R.string.score)

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
        binding.tvMovesFw.text = getString(R.string.moves_highscore, totalMoves)
        binding.tvTimeFw.text = getString(R.string.time_left_highscore, minutes, seconds)
    }

    private fun saveScoreButton() {
        var clickCount = 0
        binding.btnSaveScoreFw.setOnClickListener {
            clickCount++
            when (clickCount) {
                1 -> {
                    playersList = loadPrefsScore(requireContext())

                    playerViewModel.player.let { player ->
                        val name = player.value?.name
                        val difficulty = player.value?.difficulty
                        val pauseHelp = player.value?.pauseChoice
                        val time = player.value?.time
                        val moves = player.value?.moves
                        val theme = player.value?.theme
                        playersList.add(Player(name, difficulty, pauseHelp, time, moves, theme))

                        savedPrefsScore(requireContext(), playersList)
                    }
                    Toast.makeText(requireContext(),
                        getString(R.string.score_saved) , Toast.LENGTH_SHORT).show()
                }


            }
        }
    }

    private fun highScoreButton() {
        binding.btnHighScoreFw.setOnClickListener {
            gameViewModel.resetCount()
            gameViewModel.resetMoves()
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.fcv_game_plan_am, HighScoreFragment())
                commit()
            }
        }
    }

    private fun homeMenuButton() {
        binding.btnHomeFw.setOnClickListener {
            gameViewModel.resetCount()
            gameViewModel.resetMoves()
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.fcv_game_plan_am, HomeMenuFragment())
                commit()
            }
        }
    }

    private fun playAgainButton() {
        binding.btnPlayAgainFw.setOnClickListener {
            gameViewModel.resetCount()
            gameViewModel.resetMoves()
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

}