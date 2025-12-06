package com.example.memory_madness.Fragments.game_play


import android.os.Bundle
import android.util.Log
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
import com.example.memory_madness.Activitys.EnumClass.CardTheme
import com.example.memory_madness.DataClass.CardManager
import com.example.memory_madness.Fragments.HomeMenuFragment
import com.example.memory_madness.Fragments.WinFragment
import com.example.memory_madness.ViewModell.GameViewModel
import com.example.memory_madness.ViewModell.PlayerViewModel
import com.example.memory_madness.R
import com.example.memory_madness.databinding.FragmentEasyBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class EasyFragment : Fragment() {
    private lateinit var playerViewModel: PlayerViewModel
    private lateinit var gameViewModel: GameViewModel
    private lateinit var binding: FragmentEasyBinding
    private var memoryCards = mutableListOf<Int>()
    private var timerJob: Job? = null
    private var isBusy = false
    private var loseBusy = false

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

        // sets the lose text and play again button to invisible for the player
        setVisibility()

        // sets the chosen theme drawable images on cards
        setThemeOnCard()
        // List of ImageViews
        val containerListCards = initImageViewList()

        // Double the list of cards/Drawables so player can get pair
        val shuffledMemoryCards = initShuffleCardList()

        // shuffle cards
        shuffledMemoryCards.shuffle()

        // connects the cardManager info to the imageViews
        setCardInfoOnImageView(shuffledMemoryCards, containerListCards)

        // Sets the layout xml backround to all the cards
        for (i in 0 until containerListCards.size) {
            containerListCards[i].setImageResource(R.drawable.card_backround)
        }

        //  Button for home Menu And resets all players score and time
        homeMenuButton()

        // Pause function
        enablePauseButton()

        // play game
        gamePlay(containerListCards)

    }

    private fun setVisibility() {

        binding.btnPlayAgainFe.isInvisible = true
        binding.tvLoseFe.isInvisible = true

        if (playerViewModel.player.value?.pauseChoice == "on") {
            binding.switchPauseFe.isVisible = true
        } else {
            binding.switchPauseFe.isInvisible = true
        }
    }

    private fun setThemeOnCard() {
        if (playerViewModel.player.value?.theme == CardTheme.HALLOWEEN_THEME) {
            for (i in 0 until 3) {
                memoryCards.add(CardTheme.HALLOWEEN_THEME.themeSet[i])
            }
        } else if (playerViewModel.player.value?.theme == CardTheme.CHRISTMAS_THEME) {
            for (i in 0 until 3) {
                memoryCards.add(CardTheme.CHRISTMAS_THEME.themeSet[i])
            }
        } else if (playerViewModel.player.value?.theme == CardTheme.EASTER_THEME) {
            for (i in 0 until 3) {
                memoryCards.add(CardTheme.EASTER_THEME.themeSet[i])
            }
        } else if (playerViewModel.player.value?.theme == CardTheme.STPATRICKSDAY_THEME) {
            for (i in 0 until 3) {
                memoryCards.add(CardTheme.STPATRICKSDAY_THEME.themeSet[i])
            }
        }
    }

    private fun homeMenuButton() {
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
    }

    private fun enablePauseButton() {
        if (playerViewModel.player.value?.pauseChoice == "on") {
            binding.switchPauseFe.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    val savedTime = gameViewModel.timerCount.value
                    stopTimer()
                    gameViewModel.setCountTime(savedTime)
                    isBusy = true
                } else {
                    if (gameViewModel.timerCount.value == null ){
                        timerJob = null
                        isBusy= false
                    } else {
                        isBusy = false
                        startTimer()
                    }

                }
            }
        }
    }

    /**
     * Creates a list of imagesViews
     */
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

        // Loop through all card ImageViews and add click listener
        for (imageView in containerListCards) {
                imageView.setOnClickListener { view ->

                    // if 2 card is flipped player cant click for a delay time
                    // and if times is up (lose) then player can only press play again or home menu
                    if (isBusy || loseBusy) {
                        return@setOnClickListener
                    }

                    if (timerJob == null) {
                        gameViewModel.setCountTime(20)
                        startTimer()
                    }


                    // Times up and Player loses can only click play again and home menu button
                    // Makes lose text and Play again button be seen by the player otherwise it is invisible
                    gameViewModel.timerCount.observe(viewLifecycleOwner) { timerCount ->
                        if (timerCount == 0) {
                            stopTimer()
                            loseBusy = true
                            binding.tvLoseFe.isInvisible = false
                            binding.btnPlayAgainFe.isInvisible = false
                            binding.btnPlayAgainFe.setOnClickListener {
                                loseBusy = false
                                gameViewModel.resetCardPairCount()
                                gameViewModel.resetCount()
                                gameViewModel.resetMoves()
                                parentFragmentManager.beginTransaction().apply {
                                    replace(R.id.fcv_game_plan_am, EasyFragment())
                                    commit()
                                }
                            }
                        }
                    }



                    // Get the clicked card from the ImageView's tag with all the info from CardManager
                    gameViewModel.currentCard.value = view.tag as CardManager

                    gameViewModel.currentCard.value?.let { currentCard ->

                        if (currentCard.isFlipped || currentCard.isMatched) return@setOnClickListener

                        //show image
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

                            currentCard.isMatched = true
                            turnedCard.isMatched = true
                            gameViewModel.turnedCard.value = null

                            gameViewModel.increaseCardPairCount()

                            gameViewModel.increaseTimerCount()

                            //   WIN  //

                            if (gameViewModel.cardPairCount.value == memoryCards.size) {
                                stopTimer()
                                gameViewModel.resetCardPairCount()
                                parentFragmentManager.beginTransaction().apply {
                                    replace(R.id.fcv_game_plan_am, WinFragment())
                                    commit()
                                }
                            }
                            //     NO MATCH FOUND   //
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
                    gameViewModel.startCountDown()
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






