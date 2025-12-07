package com.example.memory_madness.fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.memory_madness.enum_class.CardTheme
import com.example.memory_madness.fragments.game_play.EasyFragment
import com.example.memory_madness.fragments.game_play.HardFragment
import com.example.memory_madness.fragments.game_play.MediumFragment
import com.example.memory_madness.view_model.PlayerViewModel
import com.example.memory_madness.R
import com.example.memory_madness.databinding.FragmentHomeMenuBinding

class HomeMenuFragment : Fragment() {
    private lateinit var binding: FragmentHomeMenuBinding
    private lateinit var playerViewModel: PlayerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playerViewModel = ViewModelProvider(requireActivity())[PlayerViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeMenuBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playerViewModel.player.observe(viewLifecycleOwner) { (_, difficulty) ->
            binding.tvDifficultyFhm.text = getString(R.string.difficulty_home_menu, difficulty)
        }


        changeNameButton()

        showIfPauseOnOff()

        pauseCheckBoxOnOff()

        showsCurrentTheme()

        changeThemeButton()

        startGameButton()

        difficultyButton()

        highScoreButton()

        endGameButton()
    }

    /**
     * If player already has set pause help to ON then box isChecked = true
     */
    private fun showIfPauseOnOff() {
        if (playerViewModel.player.value?.pauseChoice == "on") {
            binding.checkBoxEnablePauseFhm.isChecked = true
        }
    }

    /**
     * Let's Player turn pause help on/off
     */
    private fun pauseCheckBoxOnOff() {
        binding.checkBoxEnablePauseFhm.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                playerViewModel.updatePause("on")
            } else
                playerViewModel.updatePause("off")
        }
    }

    /**
     * Let's Player change their name
     */
    private fun changeNameButton() {
        binding.btnEnterNameFhm.setOnClickListener {
            playerViewModel.changeName(binding.etChangeNameFhm.text.toString())

        }
    }

    /**
     * Shows player the theme that is active
     */
    private fun showsCurrentTheme() {
        playerViewModel.player.observe(viewLifecycleOwner) {
            binding.cardThemeFhm.setImageResource(playerViewModel.player.value?.theme!!.themeSet[1])

            when (playerViewModel.player.value?.theme) {
                CardTheme.HALLOWEEN_THEME -> {
                    binding.tvThemeFhm.text = getString(R.string.halloween)
                }

                CardTheme.CHRISTMAS_THEME -> {
                    binding.tvThemeFhm.text = getString(R.string.christmas)
                }

                CardTheme.EASTER_THEME -> {
                    binding.tvThemeFhm.text = getString(R.string.easter)
                }

                CardTheme.STPATRICKSDAY_THEME -> {
                    binding.tvThemeFhm.text = getString(R.string.st_patrick_s_day)
                }

                else -> {}
            }
        }
    }

    /**
     * Let's player change themes on memory cards
     * Updates Player theme choice
     * updates theme image and theme text for player
     */
    private fun changeThemeButton() {

        var clickCount = 0
        binding.btnChangeThemeFhm.setOnClickListener {
            clickCount++
            when (clickCount) {
                1 -> {
                    playerViewModel.setTheme(playerTheme = CardTheme.HALLOWEEN_THEME)
                    binding.cardThemeFhm.setImageResource(CardTheme.HALLOWEEN_THEME.themeSet[1])
                    binding.tvThemeFhm.text = getString(R.string.halloween)

                }

                2 -> {
                    playerViewModel.setTheme(playerTheme = CardTheme.CHRISTMAS_THEME)
                    binding.cardThemeFhm.setImageResource(CardTheme.CHRISTMAS_THEME.themeSet[1])
                    binding.tvThemeFhm.text = getString(R.string.christmas)

                }

                3 -> {
                    playerViewModel.setTheme(playerTheme = CardTheme.EASTER_THEME)
                    binding.cardThemeFhm.setImageResource(CardTheme.EASTER_THEME.themeSet[1])
                    binding.tvThemeFhm.text = getString(R.string.easter)

                }

                4 -> {
                    playerViewModel.setTheme(playerTheme = CardTheme.STPATRICKSDAY_THEME)
                    binding.cardThemeFhm.setImageResource(CardTheme.STPATRICKSDAY_THEME.themeSet[1])
                    binding.tvThemeFhm.text = getString(R.string.st_patrick_s_day)
                    clickCount = 0
                }
            }
        }
    }

    /**
     * Lets player start the game over again
     * with correct player difficulty
     */
    private fun startGameButton() {
        binding.btnStartFhm.setOnClickListener {
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
            }
        }
    }

    /**
     * Player gets sent to difficulty menu fragment
     */
    private fun difficultyButton() {
        binding.btnDifficultyFhm.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.fcv_game_plan_am, DifficultyFragment())
                commit()
            }
        }
    }

    /**
     * Player gets sent to HighScore fragment
     */
    private fun highScoreButton() {
        binding.btnHighScoreFhm.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.fcv_game_plan_am, HighScoreFragment())
                commit()
            }
        }
    }

    /**
     * Player Ends Game
     */
    private fun endGameButton() {
        binding.btnEndgameFhm.setOnClickListener {
            showEndGameDialog()
        }
    }

    fun showEndGameDialog() {
        AlertDialog.Builder(requireContext()).apply {
            setTitle(R.string.close_game)
            setMessage(
                getString(
                    R.string.would_you_like_to_close_the_game,
                    playerViewModel.player.value?.name.toString()
                ))
            setPositiveButton(R.string.close_game) { a, b ->
                requireActivity().finish()
            }.setNegativeButton(getString(R.string.cancel), null).show()
        }
    }

}
