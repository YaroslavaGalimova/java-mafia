package com.alexeyzarechnev.mafia;

import com.alexeyzarechnev.mafia.game.Game;

public class MafiaApplication {

	public static void main(String[] args) {
		Game game = MafiaConfig.game(MafiaConfig.players(MafiaConfig.parser()));
		game.display();
	}

}
