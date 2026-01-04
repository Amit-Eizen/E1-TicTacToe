package com.example.e1_tictactoe

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var gameStatusTextView: android.widget.TextView
    private lateinit var playAgainButton: android.widget.Button
    private val boardButtons = mutableListOf<android.widget.Button>()
    private var currentPlayer = "X"
    private var turnCount = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        gameStatusTextView = findViewById(R.id.gameStatusTextView)
        playAgainButton = findViewById(R.id.playAgainButton)

        for (i in 1..9) {
            val buttonIdName = "button$i"
            val buttonId = resources.getIdentifier(buttonIdName, "id", packageName)
            val button = findViewById<android.widget.Button>(buttonId)
            boardButtons.add(button)
        }

        // Loop through all board buttons and set a click listener for each
        for (button in boardButtons) {
            button.setOnClickListener {
                onBoardButtonClick(it as android.widget.Button)
            }
        }

        playAgainButton.setOnClickListener {
            resetGame()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun onBoardButtonClick(button: android.widget.Button) {
        // 1. Check if the button is already played
        if (button.text.isNotEmpty()) {
            return // Do nothing if the cell is not empty
        }

        // 2. Set the button text to the current player's symbol
        button.text = currentPlayer
        turnCount++

        // 3. Check for a win
        val winnerFound = checkForWin()
        if (winnerFound) {
            // Game is over, so don't continue to the next steps
            return
        }

        // 4. If no winner, switch the player for the next turn
        if (turnCount == 9) {
            gameStatusTextView.text = "It's a draw!"
            disableBoard()
            playAgainButton.visibility = View.VISIBLE
            return
        }

        if (currentPlayer == "X") {
            currentPlayer = "O"
            gameStatusTextView.text = "Player O's turn"
        } else {
            currentPlayer = "X"
            gameStatusTextView.text = "Player X's turn"
        }
    }

    private fun resetGame() {
        // 1. Clear all button texts and re-enable them
        for (button in boardButtons) {
            button.text = ""
            button.isEnabled = true
        }

        // 2. Reset the turn counter and current player
        turnCount = 0
        currentPlayer = "X"

        // 3. Reset the status text
        gameStatusTextView.text = "Player X's turn"

        // 4. Hide the "Play Again" button
        playAgainButton.visibility = View.GONE
    }

    private fun checkForWin(): Boolean {
        // List of all winning combinations (indices of the buttons in boardButtons list)
        val winningCombos = listOf(
            // Rows
            listOf(0, 1, 2), listOf(3, 4, 5), listOf(6, 7, 8),
            // Columns
            listOf(0, 3, 6), listOf(1, 4, 7), listOf(2, 5, 8),
            // Diagonals
            listOf(0, 4, 8), listOf(2, 4, 6)
        )

        for (combo in winningCombos) {
            val firstButton = boardButtons[combo[0]]
            val secondButton = boardButtons[combo[1]]
            val thirdButton = boardButtons[combo[2]]

            val firstSymbol = firstButton.text.toString()
            val secondSymbol = secondButton.text.toString()
            val thirdSymbol = thirdButton.text.toString()

            // Check if the three buttons have the same, non-empty symbol
            if (firstSymbol.isNotEmpty() && firstSymbol == secondSymbol && firstSymbol == thirdSymbol) {
                gameStatusTextView.text = "Player $firstSymbol wins!"
                disableBoard()
                playAgainButton.visibility = View.VISIBLE
                return true // A winner was found
            }
        }
        return false // No winner was found
    }

    private fun disableBoard() {
        for (button in boardButtons) {
            button.isEnabled = false
        }
    }
}