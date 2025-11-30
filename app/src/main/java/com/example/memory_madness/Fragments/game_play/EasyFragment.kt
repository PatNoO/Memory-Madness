package com.example.memory_madness.Fragments.game_play


import android.os.Bundle
import android.util.Log
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
import com.example.memory_madness.Fragments.HomeMenuFragment
import com.example.memory_madness.Fragments.WinFragment
import com.example.memory_madness.GameViewModel
import com.example.memory_madness.PlayerViewModel
import com.example.memory_madness.R
import com.example.memory_madness.databinding.FragmentEasyBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class EasyFragment : Fragment() {
    private lateinit var playerViewModel: PlayerViewModel
    private lateinit var gameViewModel: GameViewModel
    private lateinit var binding: FragmentEasyBinding

    //List of images from drawable
    private val memoryCards: MutableList<Int> = mutableListOf(
        R.drawable.card1, R.drawable.card2, R.drawable.card3
    )
    private var timerJob: Job? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playerViewModel = ViewModelProvider(requireActivity())[PlayerViewModel::class.java]

        gameViewModel = ViewModelProvider(requireActivity())[GameViewModel::class.java]
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEasyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // List of ImageViews
        val containerListCards = initImageViewList()

        val shuffledMemoryCards = initShuffleCardList()

        shuffledMemoryCards.shuffle()

        setCardInfoOnImageView(shuffledMemoryCards, containerListCards)

        // Sets the layout xml backround to all the cards
        for (i in 0 until containerListCards.size) {
            containerListCards[i].setImageResource(R.drawable.card_backround)
        }

        binding.btnHomeMenuFe.setOnClickListener {
            gameViewModel.resetCount()
            gameViewModel.resetMoves()
            gameViewModel.resetCardPairCount()
            stopTimer()
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.fcv_game_plan_am, HomeMenuFragment())
                commit()
            }
        }


        gamePlay(containerListCards)

    }

    private fun initImageViewList(): List<ImageView> {
        val containerListCards = listOf(
            binding.card1Fe,
            binding.card2Fe,
            binding.card3Fe,
            binding.card4Fe,
            binding.card5Fe,
            binding.card6Fe,
        )
        return containerListCards
    }

    /**
     * Main gameplay logic.
     *
     * Handles:
     *  - Card flipping
     *  - Preventing double–clicks during animations ("busy state")
     *  - Tracking the first and second selected card
     *  - Checking for matches
     *  - Flipping unmatched cards back
     *  - Increasing move counter
     *  - Detecting win condition
     *  - Navigating to the Win screen when all pairs are found
     *
     * This function attaches click listeners to every card (ImageView)
     * and controls the entire flow of the memory game.
     */
    private fun gamePlay(containerListCards: List<ImageView>) {

        /** todo prova om man kan lägga en klick listener över game lestenern som en paus knapp gör samma logik som med currentcard och turnedcard
         * todo så om 1 klick är null isåfall är klick 1  = klick 2 och om klick 2 = true då returnerToListener i vår game listener
         **/

        // Click listener for gameplay ( Game Play here )

        var isBusy = false

        if (timerJob == null) {
            gameViewModel.setCountTime()
            startTimer()
        }

        gameViewModel.timerCount.observe(viewLifecycleOwner) { timerCount ->
            if (timerCount == 0){
                Toast.makeText(requireActivity(),"Times Up !!" , Toast.LENGTH_SHORT).show()
            }
        }


        // Loop through all card ImageViews and add click listeners
        for (imageView in containerListCards) {
            imageView.setOnClickListener { view ->


                if (isBusy) {
                    return@setOnClickListener
                }

                // Get the clicked card from the ImageView's tag
                gameViewModel.currentCard.value = view.tag as CardManager

                gameViewModel.currentCard.value?.let { currentCard ->

                    if (currentCard.isFlipped || currentCard.isMatched) return@setOnClickListener

                    currentCard.containerId.setImageResource(currentCard.cardId)
                    currentCard.isFlipped = true

                    // Store as first card in pair
                    if (gameViewModel.turnedCard.value == null) {
                        gameViewModel.turnedCard.value = currentCard
                        return@setOnClickListener
                    }

                    gameViewModel.increaseMoves()

                    gameViewModel.moves.observe(viewLifecycleOwner) { moves ->
                        binding.tvMovesFe.text = "Moves : \n $moves"
                    }

                    val turnedCard = gameViewModel.turnedCard.value

                    //     MATCH FOUND

                    if (turnedCard!!.cardId == currentCard.cardId) {

                        Toast.makeText(requireActivity(), "Match !", Toast.LENGTH_SHORT).show()

                        currentCard.isMatched = true
                        turnedCard.isMatched = true
                        gameViewModel.turnedCard.value = null

                        gameViewModel.increaseCardPairCount()

                        if (gameViewModel.cardPairCount.value == memoryCards.size) {
                            Toast.makeText(requireContext(), "You Won ", Toast.LENGTH_SHORT).show()
                            stopTimer()
                            gameViewModel.resetCardPairCount()
                            parentFragmentManager.beginTransaction().apply {
                                replace(R.id.fcv_game_plan_am, WinFragment())
                                commit()
                            }
                        }
                        //     NO MATCH FOUND
                    } else {
                        isBusy = true
                        currentCard.containerId.postDelayed(
                            {
                                turnedCard.containerId.setImageResource(R.drawable.card_backround)
                                currentCard.containerId.setImageResource(R.drawable.card_backround)

                                currentCard.isFlipped = false
                                turnedCard.isFlipped = false
                                gameViewModel.turnedCard.value = null
                                isBusy = false
                            }, 500
                        )

                    }
                }


            }

        }
    }

    /**
     * initiates The card id Arraylist and duplicates the play cards to get pairs
     */
    private fun initShuffleCardList(): ArrayList<Int> {
        val shuffledMemoryCards = ArrayList<Int>()
        for (i in memoryCards) {
            shuffledMemoryCards.add(i)
            shuffledMemoryCards.add(i)
        }
        return shuffledMemoryCards
    }

    /**
     * Connects every imageView to a CardManager object
     * Every play card gets info or state "isFlipped, isMatched, cardId (for pair control) ..."
     */
    private fun setCardInfoOnImageView(
        shuffledMemoryCards: ArrayList<Int>,
        containerCard: List<ImageView>
    ) {

        for (i in shuffledMemoryCards.indices) {
            val imageViewId: ImageView = containerCard[i] // View binding
            val memoryImageId: Int = shuffledMemoryCards[i]       // Images Drawable
            // sets cardManager
            val cardInfo = CardManager(
                isFlipped = false,
                isMatched = false,
                cardId = memoryImageId,
                containerId = imageViewId
            )
            // sets the cardinfo from cardManager as a tag on the imageView
            imageViewId.tag = cardInfo
            Log.i("!!!", "Card info : ${imageViewId.tag}")
        }
    }

    /**
     * initiates the timer for the player
     */
    fun startTimer() {
        timerJob = viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                while (true) {
                    delay(1000)
                    gameViewModel.startCount()
                    updateTimerText()
                }
            }
        }
    }

    /**
     * Stops the timer
     */
    fun stopTimer() {
        timerJob?.cancel()
    }

    /**
     * Updates the timer for the player to see while playing
     */
    fun updateTimerText() {
        val minutes = gameViewModel.timerCount.value?.div(60)
        val seconds = gameViewModel.timerCount.value?.rem(60)
        binding.tvTimeFe.text = "Time : \n $minutes : $seconds"

    }

    override fun onDetach() {
        super.onDetach()
        stopTimer()
    }


}






