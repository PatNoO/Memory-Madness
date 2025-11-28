package com.example.memory_madness.Fragments.game_play


import android.content.Intent
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
import com.example.memory_madness.Fragments.DifficultyFragment
import com.example.memory_madness.Fragments.HomeMenuFragment
import com.example.memory_madness.Fragments.WinFragment
import com.example.memory_madness.GameViewModel
import com.example.memory_madness.PlayerViewModel
import com.example.memory_madness.R
import com.example.memory_madness.StartActivity
import com.example.memory_madness.databinding.FragmentEasyBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class EasyFragment : Fragment() {
    private lateinit var playerViewModel: PlayerViewModel
    private lateinit var gameViewModel: GameViewModel
    private lateinit var binding: FragmentEasyBinding
    
    //List of images from drawable
    private val cardId: MutableList<Int> = mutableListOf(
        R.drawable.card1, R.drawable.card2, R.drawable.card3, R.drawable.card4, R.drawable.card5,
        R.drawable.card6)
    private var timerJob: Job? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playerViewModel = ViewModelProvider(requireActivity()) [PlayerViewModel::class.java]

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
        val containerCard = listOf(
            binding.card1Fe,
            binding.card2Fe,
            binding.card3Fe,
            binding.card4Fe,
            binding.card5Fe,
            binding.card6Fe,
            binding.card7Fe,
            binding.card8Fe,
            binding.card9Fe,
            binding.card10Fe,
            binding.card11Fe,
            binding.card12Fe
        )

        val shuffledCardIds = initShuffleCardList()

        shuffledCardIds.shuffle()

        setCardInfoOnImageView(shuffledCardIds, containerCard)


        binding.btnEndgameFe.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.fcv_game_plan_am, HomeMenuFragment(), "fragment_home_menu")
                commit()
            }
        }


        gamePlay(containerCard)

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
    private fun gamePlay(containerCard: List<ImageView>) {

        /** todo prova om man kan lägga en klick listener över game lestenern som en paus knapp gör samma logik som med currentcard och turnedcard
         * todo så om 1 klick är null isåfall är klick 1  = klick 2 och om klick 2 = true då returnerToListener i vår game listener
         **/

        // Click listener for gameplay ( Game Play here )

        var isBusy = false

        if (timerJob == null) {
            startTimer()
        }
        // Loop through all card ImageViews and add click listeners
        for (imageViewId in containerCard) {
            imageViewId.setOnClickListener { view ->


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

                        turnedCard.isMatched = true
                        currentCard.isMatched = true
                        gameViewModel.turnedCard.value = null

                        gameViewModel.increaseCardPairCount()

                        if (gameViewModel.cardPairCount.value == 6) {
                            Toast.makeText(requireContext(), "You Won ", Toast.LENGTH_SHORT).show()
                            stopTimer()
                            parentFragmentManager.beginTransaction().apply {
                                replace(R.id.fcv_game_plan_am, WinFragment(), "fragment_win")
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
        val shuffledCardIds = ArrayList<Int>()
        for (id in cardId) {
            shuffledCardIds.add(id)
            shuffledCardIds.add(id)
        }
        return shuffledCardIds
    }

    /**
     * Connects every imageView to a CardManager object
     * Every play card gets info or state "isFlipped, isMatched, cardId (for pair control) ..."
     */
    private fun setCardInfoOnImageView(shuffledCardIds: ArrayList<Int>, containerCard: List<ImageView>) {

        for (i in shuffledCardIds.indices) {
            val imageViewId: ImageView = containerCard[i] // View binding
            val imageId: Int = shuffledCardIds[i]       // Images Drawable
            // sets cardManager
            val cardInfo = CardManager(
                isFlipped = false,
                isMatched = false,
                cardId = imageId,
                containerId = imageViewId
            )
            // sets the cardinfo from cardManager as a tag on the imageView
            imageViewId.tag = cardInfo
            Log.i("!!!","Card info : ${imageViewId.tag}")
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






