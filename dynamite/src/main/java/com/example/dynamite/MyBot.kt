package com.example.dynamite

import com.softwire.dynamite.bot.Bot
import com.softwire.dynamite.game.Gamestate
import com.softwire.dynamite.game.Move
import com.sun.org.apache.xpath.internal.operations.Bool
import kotlin.random.Random
import kotlin.random.Random.Default.nextBoolean

class MyBot : Bot {

    var myDynamite: Int = 100 //My bot's number of dynamite left
    var opponentsDynamite: Int = 100 //Opponent's bot's number of dynamite left
    var beatPreviousMove: Boolean = false
    var dynamiteOnDraw: Boolean = false

    override fun makeMove(gamestate: Gamestate): Move {

        if (gamestate.rounds.isNotEmpty()) if (gamestate.rounds.last().p2 == Move.D) this.opponentsDynamite--

        if (gamestate.rounds.size < 100) return getRandomMove()

        if (gamestate.rounds.size >= 100) {
            this.beatPreviousMove = getBeatPreviousMoveBool(gamestate)
            this.dynamiteOnDraw = getDynamiteOnDrawBool(gamestate)
        }

        if (this.dynamiteOnDraw) {
            val previousRound = gamestate.rounds.last()
            if (previousRound.p1 == previousRound.p2) return Move.W
        }
        if (this.beatPreviousMove) {
            val previousMove = gamestate.rounds.last().p1
            return outsmartBeatPreviousMove(previousMove)
        }
        return getRandomMove()


        /*if(myDynamite > 0 && Random.nextBoolean()) {
            this.myDynamite--
            return Move.D
        }
        else if (gamestate.rounds.isNotEmpty()){
            if (gamestate.rounds.last().p2.equals(Move.W)) this.opponentsDynamite--
            return Move.P
        }
        return Move.D*/
    }

    fun getRandomMove() : Move {
        val listOfNums = listOf<Int>(0,1,2,3,4)
        val randomNum = listOfNums.shuffled().first()
        if (randomNum == 4) {
            if (this.myDynamite <= 0) return getRandomMove()
            else {
                this.myDynamite--
                return Move.D
            }
        }
        if (randomNum == 3 && this.opponentsDynamite <= 0) return getRandomMove()
        return Move.values()[randomNum]
    }

    fun getBeatPreviousMoveBool(gamestate: Gamestate) : Boolean {
        var wouldBeatPreviousMove = 0
        for (i in 1 until gamestate.rounds.size) {
            if (doesXBeatY(gamestate.rounds[i].p2, gamestate.rounds[i-1].p1)) wouldBeatPreviousMove++
        }
        val percentage = ((wouldBeatPreviousMove / gamestate.rounds.size) * 100)
        return percentage > 80
    }

    fun getDynamiteOnDrawBool(gamestate: Gamestate) : Boolean {
        var dynamiteOnDraw = 0
        var draws = 0
        for (i in 0 until gamestate.rounds.size-1) {
            if (gamestate.rounds[i].p1 == gamestate.rounds[i].p2) {
                draws++
                if (gamestate.rounds[i+1].p2 == Move.D) dynamiteOnDraw++
            }
        }
        if (draws == 0) return false
        val percentage = (dynamiteOnDraw / draws) * 100
        return percentage > 80
    }

    fun doesXBeatY(x: Move, y: Move) : Boolean {
        return when (x) {
            Move.D -> y != Move.W && y != Move.D
            Move.W -> y == Move.D
            Move.R -> y == Move.S || y == Move.W
            Move.P -> y == Move.R || y == Move.W
            Move.S -> y == Move.P || y == Move.W
        }
    }

    fun outsmartBeatPreviousMove(previousMove: Move) : Move {
        return when (previousMove) {
            Move.W -> Move.D
            Move.R -> Move.S
            Move.D -> Move.R
            Move.S -> Move.P
            Move.P -> Move.R
        }
    }

    init {
        // Are you debugging?
        // Put a breakpoint on the line below to see when we start a new match
        println("Started new match")
    }

}

//Strategy:
// - Randomly use dynamite if it is available
// - For first 100 games choose randomly
// - Use the first 100 games to discern if they're using the "beat previous move bot" and/or "dynamite on draw"
// - If 80%+ of first 100 games are "beat previous move" and/or 80%+ of draws in first 100 games are "dynamite on draw", adjust accordingly
// - If all else fails, choose randomly for rest

//TODO: Detect if there's a single one out of RPS that bot prefers, i.e. Rock used 45%+