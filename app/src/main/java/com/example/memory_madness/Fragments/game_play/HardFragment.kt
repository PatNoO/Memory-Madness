package com.example.memory_madness.Fragments.game_play

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.memory_madness.CardManager
import com.example.memory_madness.Fragments.HomeMenuFragment
import com.example.memory_madness.Fragments.WinFragment
import com.example.memory_madness.GameViewModel
import com.example.memory_madness.PlayerViewModel
import com.example.memory_madness.R
import com.example.memory_madness.databinding.FragmentHardBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class HardFragment : Fragment() {

    private lateinit var binding: FragmentHardBinding
    private lateinit var playerViewModel: PlayerViewModel
    private lateinit var gameViewModel: GameViewModel

    private var timerJob: Job? = null

    private val memoryCards: MutableList<Int> = mutableListOf(
        R.drawable.card1, R.drawable.card2, R.drawable.card3, R.drawable.card4, R.drawable.card5,
        R.drawable.card6, R.drawable.card7, R.drawable.card8, R.drawable.card9
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playerViewModel = ViewModelProvider(requireActivity())[PlayerViewModel::class.java]
        gameViewModel = ViewModelProvider(requireActivity())[GameViewModel::class.java]
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val containerListCards = initImageViewList()

        val shuffledMemoryCards = initShuffleCardList()

        shuffledMemoryCards.shuffle()

        setCardInfoOnImageView(shuffledMemoryCards, containerListCards)

        // Sets the layout xml backround to all the cards
        for (i in 0 until containerListCards.size) {
            containerListCards[i].setImageResource(R.drawable.card_backround)
        }

        binding.btnHomeMenuFh.setOnClickListener {
            gameViewModel.resetCount()
            gameViewModel.resetMoves()
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.fcv_game_plan_am, HomeMenuFragment())
                commit()
            }
        }

        gamePlay(containerListCards)


    }

    private fun initImageViewList(): List<ImageView> {
        val containerListCards = listOf(
            binding.card1Fh,
            binding.card2Fh,
            binding.card3Fh,
            binding.card4Fh,
            binding.card5Fh,
            binding.card6Fh,
            binding.card7Fh,
            binding.card8Fh,
            binding.card9Fh,
            binding.card10Fh,
            binding.card11Fh,
            binding.card12Fh,
            binding.card13Fh,
            binding.card14Fh,
            binding.card15Fh,
            binding.card16Fh,
            binding.card17Fh,
            binding.card18Fh
        )
        return containerListCards
    }

    private fun gamePlay(containerListCards: List<ImageView>) {

        var isBusy = false

        for (imageView in containerListCards) {
            imageView.setOnClickListener { view ->

                if (timerJob == null) {
                    startTimer()
                }

                if (isBusy) {
                    return@setOnClickListener
                }

                //the Object stored in this view as a tag
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
                        binding.tvMovesFh.text = "Moves : \n $moves"
                    }

                    val turnedCard = gameViewModel.turnedCard.value

                    if (currentCard.cardId == turnedCard!!.cardId) {
                        Toast.makeText(requireActivity(), "Match!", Toast.LENGTH_SHORT).show()

                        currentCard.isMatched = true
                        turnedCard.isMatched = true
                        gameViewModel.turnedCard.value = null

                        gameViewModel.increaseCardPairCount()

                        if (gameViewModel.cardPairCount.value == memoryCards.size) {
                            stopTimer()
                            parentFragmentManager.beginTransaction().apply {
                                replace(R.id.fcv_game_plan_am, WinFragment())
                                commit()
                            }
                        }


                    } else {
                        isBusy = true
                        currentCard.containerId.postDelayed(
                            {
                                currentCard.containerId.setImageResource(R.drawable.card_backround)
                                turnedCard.containerId.setImageResource(R.drawable.card_backround)

                                currentCard.isFlipped = false
                                turnedCard.isFlipped = false
                                currentCard.containerId.setImageResource(R.drawable.card_backround)

                                gameViewModel.turnedCard.value = null
                            }, 500
                        )
                        isBusy = false
                    }

                }
            }
        }
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
            val imageViewId = containerListCards[i]
            val memoryImageId = shuffledMemoryCards[i]
            val cardInfo = CardManager(
                isFlipped = false,
                isMatched = false,
                containerId = imageViewId,
                cardId = memoryImageId
            )
            //Sets the tag associated with this view. A tag can be used to mark a view in its hierarchy
            // and does not have to be unique within the hierarchy.
            // Tags can also be used to store data within a view without resorting to another data structure
            imageViewId.tag = cardInfo
        }
    }

    fun startTimer() {
        timerJob = viewLifecycleOwner.lifecycleScope.launch {
            while (true) {
                delay(1000)
                gameViewModel.startCount()
                updateTimer()
            }

        }
    }

    fun updateTimer() {
        val minutes = gameViewModel.timerCount.value?.div(60)
        val seconds = gameViewModel.timerCount.value?.rem(60)
        binding.tvTimeFh.text = "Time : \n $minutes : $seconds"
    }

    fun stopTimer() {
        timerJob?.cancel()
    }

    override fun onDetach() {
        super.onDetach()
        stopTimer()
    }


}