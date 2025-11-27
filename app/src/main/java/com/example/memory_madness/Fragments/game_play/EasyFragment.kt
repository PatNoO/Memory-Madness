package com.example.memory_madness.Fragments.game_play


import android.app.Activity
import android.content.Intent
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
import com.example.memory_madness.Player
import com.example.memory_madness.PlayerViewModel
import com.example.memory_madness.R
import com.example.memory_madness.StartActivity
import com.example.memory_madness.databinding.FragmentEasyBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.FileDescriptor
import java.io.PrintWriter

class EasyFragment : Fragment() {
    private lateinit var playerViewModel: PlayerViewModel
    private lateinit var gameViewModel: GameViewModel
    private lateinit var binding: FragmentEasyBinding
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

        var isBusy = false

        if (timerJob == null) {
            startTimer()
        }

        binding.btnEndgameFe.setOnClickListener {

            val intent = Intent(requireActivity(), StartActivity::class.java)
            requireActivity().startActivity(intent)
            requireActivity().finishAffinity()
        }
        // Click listener for gameplay ( Game Play here )

        for (imageViewId in containerCard) {
            imageViewId.setOnClickListener { view ->


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
                        binding.tvMovesFe.text = "Moves : \n $moves"
                    }

                    val turnedCard = gameViewModel.turnedCard.value

                    if (turnedCard!!.cardId == currentCard.cardId) {

                        Toast.makeText(requireActivity(), "Match !", Toast.LENGTH_SHORT).show()

                        turnedCard.isMatched = true
                        currentCard.isMatched = true
                        gameViewModel.turnedCard.value = null

                        gameViewModel.increaseCardPairCount()

                        if (gameViewModel.cardPairCount.value == 6) {
                            Toast.makeText(requireContext(), "You Won ", Toast.LENGTH_SHORT).show()
                            stopTimer()
                            parentFragmentManager.beginTransaction().apply{
                                replace(R.id.fv_game_plan_am,WinFragment(), "fragment_win")
                                    commit()
                            }
                        }

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

    private fun initShuffleCardList(): ArrayList<Int> {
        val shuffledCardIds = ArrayList<Int>()
        for (id in cardId) {
            shuffledCardIds.add(id)
            shuffledCardIds.add(id)
        }
        return shuffledCardIds
    }

    private fun setCardInfoOnImageView(
        shuffledCardIds: ArrayList<Int>,
        containerCard: List<ImageView>
    ) {
        for (i in shuffledCardIds.indices) {
            val imageViewId: ImageView = containerCard[i] // View binding
            val imageId: Int = shuffledCardIds[i]       // Images Drawable
            val cardInfo = CardManager(
                isFlipped = false,
                isMatched = false,
                cardId = imageId,
                containerId = imageViewId
            )

            imageViewId.tag = cardInfo
        }
    }


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
    fun stopTimer() {
        timerJob?.cancel()
    }
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






