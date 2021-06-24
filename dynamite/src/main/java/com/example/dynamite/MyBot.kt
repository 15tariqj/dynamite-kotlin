package com.example.dynamite

import com.softwire.dynamite.bot.Bot
import com.softwire.dynamite.game.Gamestate
import com.softwire.dynamite.game.Move

class MyBot : Bot {
    var myDynamite: Int = 100
    var opponentsDynamite: Int = 100
    override fun makeMove(gamestate: Gamestate): Move {
        // Are you debugging?
        // Put a breakpoint in this method to see when we make a move
        if (gamestate.rounds.isNotEmpty()){
            if (gamestate.rounds.last().p2.equals(Move.W)) this.opponentsDynamite--
            return Move.R
        }
        return Move.D
    }

    init {
        // Are you debugging?
        // Put a breakpoint on the line below to see when we start a new match
        println("Started new match")


    }

}

//Strategy:
//