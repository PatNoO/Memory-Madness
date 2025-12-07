package com.example.memory_madness.fragments.game_play

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.memory_madness.activitys.enum_class.CardTheme
import com.example.memory_madness.data_class.CardManager
import com.example.memory_madness.fragments.HomeMenuFragment
import com.example.memory_madness.fragments.WinFragment
import com.example.memory_madness.view_model.GameViewModel
import com.example.memory_madness.view_model.PlayerViewModel
import com.example.memory_madness.R
import com.example.memory_madness.databinding.FragmentMediumBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MediumFragment : Fragment() {
    private lateinit var binding: FragmentMediumBinding
    private lateinit var gameViewModel: GameViewModel
    private lateinit var playerViewModel: PlayerViewModel
    private var timerJob: Job? = null
    private var isBusy = false
    private var loseBusy = false
    private val memoryCards = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playerViewModel = ViewModelProvider(requireActivity())[PlayerViewModel::class.java]
        gameViewModel = ViewModelProvider(requireActivity())[GameViewModel::class.java]

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMediumBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setVisibility()

        setThemeOnCard()

        val containerListCards = initImageViewList()

        val shuffledMemoryCards = initShuffleCardList()

        shuffledMemoryCards.shuffle()

        setCardInfoOnImageView(shuffledMemoryCards, containerListCards)

        // Sets the layout xml backround to all the cards
        for (i in 0 until containerListCards.size) {
            containerListCards[i].setImageResource(R.drawable.card_backround)
        }

        homeMenuButton()

        enablePauseButton()

        gamePlay(containerListCards)
    }

    private fun homeMenuButton() {
        binding.btnHomeMenuFm.setOnClickListener {
            gameViewModel.resetCount()
            gameViewModel.resetMoves()
            gameViewModel.resetCardPairCount()
            stopTimer()
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.fcv_game_plan_am, HomeMenuFragment())
                commit()
            }
        }
    }

    private fun setVisibility() {
        binding.tvLoseFm.isInvisible = true
        binding.btnPlayAgainFm.isInvisible = true

        if (playerViewModel.player.value?.pauseChoice == "on") {
            binding.switchPauseFm.isVisible = true
        } else {
            binding.switchPauseFm.isInvisible = true
        }
    }

    private fun setThemeOnCard() {
        when (playerViewModel.player.value?.theme) {
            CardTheme.HALLOWEEN_THEME -> {
                for (i in 0 until 6) {
                    memoryCards.add(CardTheme.HALLOWEEN_THEME.themeSet[i])
                }
            }

            CardTheme.CHRISTMAS_THEME -> {
                for (i in 0 until 6) {
                    memoryCards.add(CardTheme.CHRISTMAS_THEME.themeSet[i])
                }
            }

            CardTheme.EASTER_THEME -> {
                for (i in 0 until 6) {
                    memoryCards.add(CardTheme.EASTER_THEME.themeSet[i])
                }
            }

            CardTheme.STPATRICKSDAY_THEME -> {
                for (i in 0 until 6) {
                    memoryCards.add(CardTheme.STPATRICKSDAY_THEME.themeSet[i])
                }
            }

            else -> {
                for (i in 0 until 6) {
                    memoryCards.add(CardTheme.HALLOWEEN_THEME.themeSet[i])
                }
            }
        }
    }

    private fun enablePauseButton() {
        if (playerViewModel.player.value?.pauseChoice == "on") {
            binding.switchPauseFm.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    val savedTime = gameViewModel.timerCount.value
                    stopTimer()
                    gameViewModel.setCountTime(savedTime)
                    isBusy = true
                } else {
                    if (gameViewModel.timerCount.value == null) {
                        timerJob = null
                        isBusy = false
                    } else {
                        isBusy = false
                        startTimer()
                    }
                }
            }
        }
    }


    private fun gamePlay(containerListCards: List<ImageView>) {

        for (imageViewId in containerListCards) {
            imageViewId.setOnClickListener { view ->

                if (isBusy || loseBusy) {
                    return@setOnClickListener
                }

                if (timerJob == null) {
                    gameViewModel.setCountTime(20)
                    startTimer()
                }

                gameViewModel.timerCount.observe(viewLifecycleOwner) { timerCount ->
                    if (timerCount == 0) {
                        stopTimer()
                        loseBusy = true
                        binding.tvLoseFm.isInvisible = false
                        binding.btnPlayAgainFm.isInvisible = false
                        binding.btnPlayAgainFm.setOnClickListener {
                            loseBusy = false
                            gameViewModel.resetCardPairCount()
                            gameViewModel.resetCount()
                            gameViewModel.resetMoves()
                            parentFragmentManager.beginTransaction().apply {
                                replace(R.id.fcv_game_plan_am, MediumFragment())
                                commit()
                            }
                        }
                    }
                }



                gameViewModel.currentCard.value = view.tag as CardManager

                gameViewModel.currentCard.value?.let { currentCard ->

                    if (currentCard.isFlipped || currentCard.isMatched) return@setOnClickListener

                    currentCard.containerId.setImageResource(currentCard.cardId)
                    currentCard.isFlipped = true

                    if (gameViewModel.turnedCard.value == null) {
                        gameViewModel.turnedCard.value = currentCard
                        return@setOnClickListener
                    }

                    gameViewModel.increaseMoves()

                    gameViewModel.moves.observe(viewLifecycleOwner) { moves ->
                        binding.tvMovesFm.text = getString(R.string.moves, moves)
                    }

                    val turnedCard = gameViewModel.turnedCard.value

                    if (turnedCard!!.cardId == currentCard.cardId) {

                        currentCard.isMatched = true
                        turnedCard.isMatched = true
                        gameViewModel.turnedCard.value = null

                        gameViewModel.increaseCardPairCount()

                        gameViewModel.increaseTimerCount()

                        // WIN !!
                        if (gameViewModel.cardPairCount.value == memoryCards.size) {
                            stopTimer()
                            gameViewModel.resetCardPairCount()
                            parentFragmentManager.beginTransaction().apply {
                                replace(R.id.fcv_game_plan_am, WinFragment())
                                commit()
                            }
                        }

                    } else {
                        isBusy = true
                        currentCard.containerId.postDelayed({

                            turnedCard.containerId.setImageResource(R.drawable.card_backround)
                            currentCard.containerId.setImageResource(R.drawable.card_backround)

                            currentCard.isFlipped = false
                            turnedCard.isFlipped = false
                            gameViewModel.turnedCard.value = null
                            isBusy = false
                        }, 500)

                    }


                }

            }

        }
    }

    private fun initImageViewList(): List<ImageView> {
        val containerListCards = listOf(
            binding.card1Fm,
            binding.card2Fm,
            binding.card3Fm,
            binding.card4Fm,
            binding.card5Fm,
            binding.card6Fm,
            binding.card7Fm,
            binding.card8Fm,
            binding.card9Fm,
            binding.card10Fm,
            binding.card11Fm,
            binding.card12Fm,

            )
        return containerListCards
    }

    private fun initShuffleCardList(): ArrayList<Int> {
        val shuffledMemoryCards = ArrayList<Int>()
        for (i in memoryCards) {
            shuffledMemoryCards.add(i)
            shuffledMemoryCards.add(i)
        }
        return shuffledMemoryCards
    }

    private fun setCardInfoOnImageView(
        shuffledMemoryCards: ArrayList<Int>,
        containerListCards: List<ImageView>
    ) {
        for (i in shuffledMemoryCards.indices) {
            val imageViewId: ImageView = containerListCards[i]
            val memoryImageId: Int = shuffledMemoryCards[i]
            val cardInfo = CardManager(
                isFlipped = false,
                isMatched = false,
                containerId = imageViewId,
                cardId = memoryImageId
            )
            imageViewId.tag = cardInfo
        }
    }

    fun startTimer() {
        timerJob = viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                while (true) {
                    delay(1000)
                    gameViewModel.startCountDown()
                    updateTimerText()
                }
            }
        }
    }

    fun updateTimerText() {
        val minutes = gameViewModel.timerCount.value?.div(60)
        val seconds = gameViewModel.timerCount.value?.rem(60)
        binding.tvTimeFm.text = getString(R.string.time, minutes, seconds)
    }

    fun stopTimer() {
        timerJob?.cancel()
    }

    override fun onDetach() {
        super.onDetach()
        stopTimer()
    }

}
