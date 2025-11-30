package com.example.memory_madness.Fragments.game_play

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.memory_madness.CardManager
import com.example.memory_madness.Fragments.WinFragment
import com.example.memory_madness.GameViewModel
import com.example.memory_madness.PlayerViewModel
import com.example.memory_madness.R
import com.example.memory_madness.databinding.FragmentMediumBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MediumFragment : Fragment() {

    private lateinit var binding: FragmentMediumBinding
    private lateinit var gameViewModel: GameViewModel
    private lateinit var playerViewModel: PlayerViewModel
    private var timerJob : Job? = null
    private val memoryCards: MutableList<Int> = mutableListOf(
        R.drawable.card1, R.drawable.card2, R.drawable.card3, R.drawable.card4, R.drawable.card5,
        R.drawable.card6 )
//    , R.drawable.card7, R.drawable.card8, R.drawable.card9

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

        val containerListCards = initImageViewList()

        val shuffledMemoryCards = initShuffleCardList()

        shuffledMemoryCards.shuffle()

        setCardInfoOnImageView(shuffledMemoryCards, containerListCards)

        // Sets the layout xml backround to all the cards
        for (i in 0 until containerListCards.size){
            containerListCards[i].setImageResource(R.drawable.card_backround)
        }

        gamePlay(containerListCards)
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

    private fun gamePlay(containerListCards: List<ImageView>) {

        var isBusy = false

        for (imageViewId in containerListCards) {
            imageViewId.setOnClickListener { view ->

                if (timerJob == null) {
                    startTimer()
                }

                if (isBusy) {
                    return@setOnClickListener
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
                        binding.tvMovesFm.text = "Moves : \n $moves"
                    }

                    val turnedCard = gameViewModel.turnedCard.value

                    if (turnedCard!!.cardId == currentCard.cardId) {
                        Toast.makeText(requireActivity(), " MATCH ! ", Toast.LENGTH_SHORT).show()

                        currentCard.isMatched = true
                        turnedCard.isMatched = true
                        gameViewModel.turnedCard.value = null

                        gameViewModel.increaseCardPairCount()

                        if (gameViewModel.cardPairCount.value == memoryCards.size) {
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

    fun startTimer () {
        timerJob = viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                while (true){
                    delay(1000)
                    gameViewModel.startCount()
                    updateTimerText()
                }
            }
        }
    }

    fun updateTimerText () {
        val minutes = gameViewModel.timerCount.value?.div(60)
        val seconds = gameViewModel.timerCount.value?.rem(60)
        binding.tvTimeFm.text = "Time : $minutes : $seconds "
    }

    fun timerStop () {
        timerJob?.cancel()
    }

    override fun onDetach() {
        super.onDetach()
        timerStop()
    }

}
