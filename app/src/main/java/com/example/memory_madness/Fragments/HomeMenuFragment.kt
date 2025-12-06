package com.example.memory_madness.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.memory_madness.Activitys.EnumClass.CardTheme
import com.example.memory_madness.Fragments.game_play.EasyFragment
import com.example.memory_madness.Fragments.game_play.HardFragment
import com.example.memory_madness.Fragments.game_play.MediumFragment
import com.example.memory_madness.ViewModell.PlayerViewModel
import com.example.memory_madness.R
import com.example.memory_madness.databinding.FragmentHomeMenuBinding

class HomeMenuFragment : Fragment() {
    private lateinit var binding : FragmentHomeMenuBinding
    private lateinit var playerViewModel: PlayerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playerViewModel = ViewModelProvider(requireActivity())[PlayerViewModel::class.java]

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeMenuBinding.inflate(inflater,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // lets player change their name
        binding.btnEnterNameFhm.setOnClickListener {
            playerViewModel.changeName(binding.etChangeNameFhm.text.toString())

        }

        if (playerViewModel.player.value?.pauseChoice == "on"){
            binding.checkBoxEnablePauseFhm.isChecked = true
        }

        binding.checkBoxEnablePauseFhm.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                playerViewModel.updatePause("on")
            } else
                playerViewModel.updatePause("off")
        }

        playerViewModel.player.observe(this) { theme ->
            binding.cardThemeFhm.setImageResource(playerViewModel.player.value?.theme!!.themeSet[1])
        }

        var clickCount = 0

        binding.btnChangeThemeFhm.setOnClickListener {
            clickCount++
            when (clickCount) {
                1 -> {
                    playerViewModel.setTheme(playerTheme = CardTheme.HALLOWEEN_THEME)
                    binding.cardThemeFhm.setImageResource(CardTheme.HALLOWEEN_THEME.themeSet[1])
//                    player.theme = CardTheme.HALLOWEEN_THEME
                    binding.tvThemeFhm.text = "🎃HALLOWEEN🎃"
//                    Log.i("!!!", "$player")

                }

                2 -> {
                    playerViewModel.setTheme(playerTheme = CardTheme.CHRISTMAS_THEME)
                    binding.cardThemeFhm.setImageResource(CardTheme.CHRISTMAS_THEME.themeSet[1])
//                    player.theme = CardTheme.CHRISTMAS_THEME
                    binding.tvThemeFhm.text = "🎄CHRISTMAS🎄"
//                    Log.i("!!!", "$player")


                }

                3 -> {
                    playerViewModel.setTheme(playerTheme = CardTheme.EASTER_THEME)
                    binding.cardThemeFhm.setImageResource(CardTheme.EASTER_THEME.themeSet[1])
//                    player.theme = CardTheme.EASTER_THEME
                    binding.tvThemeFhm.text = "🐣EASTER🐣"
//                    Log.i("!!!", "$player")

                }

                4 -> {
                    playerViewModel.setTheme(playerTheme = CardTheme.STPATRICKSDAY_THEME)
                    binding.cardThemeFhm.setImageResource(CardTheme.STPATRICKSDAY_THEME.themeSet[1])
//                    player.theme = CardTheme.STPATRICKSDAY_THEME
                    binding.tvThemeFhm.text = "☘️ST,PATRICK'S DAY☘️"
//                    Log.i("!!!", "${player}")
                }


                5 -> {
                    playerViewModel.setTheme(playerTheme = CardTheme.HALLOWEEN_THEME)
//                    player.theme = CardTheme.HALLOWEEN_THEME
                    binding.cardThemeFhm.setImageResource(CardTheme.HALLOWEEN_THEME.themeSet[1])
                    binding.tvThemeFhm.text = "🎃HALLOWEEN🎃"
                    clickCount = 1
//                    Log.i("!!!", "$player")
                }
            }
        }



        /**
         * Lets player start the game over again
         */
        binding.btnStartFhm.setOnClickListener {
           if (playerViewModel.player.value?.difficulty == "easy"){
               parentFragmentManager.beginTransaction().apply {
                   replace(R.id.fcv_game_plan_am, EasyFragment())
                   commit()
               }
           }else if (playerViewModel.player.value?.difficulty == "medium"){
               parentFragmentManager.beginTransaction().apply {
                   replace(R.id.fcv_game_plan_am, MediumFragment())
                   commit()
               }
           }else if (playerViewModel.player.value?.difficulty == "hard"){
               parentFragmentManager.beginTransaction().apply {
                   replace(R.id.fcv_game_plan_am, HardFragment())
                   commit()
               }
           }
        }
        /**
         * Player gets sent to difficulty menu fragment
         */
        binding.btnDifficultyFhm.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.fcv_game_plan_am, DifficultyFragment())
                commit()
            }
        }
        /**
         * Player gets sent to HighScore fragment
         */
        binding.btnHighScoreFhm.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.fcv_game_plan_am, HighScoreFragment())
                commit()
            }
        }

        /**
         * Player Ends Game
         */
        binding.btnEndgameFhm.setOnClickListener {
            requireActivity().finish()
        }
    }

}