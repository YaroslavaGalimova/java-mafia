package com.alexeyzarechnev.mafia;

import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.security.InvalidAlgorithmParameterException;
import java.util.Collections;
import java.util.List;

import com.alexeyzarechnev.mafia.game.Answer;
import com.alexeyzarechnev.mafia.game.Game;
import com.alexeyzarechnev.mafia.game.Parser;
import com.alexeyzarechnev.mafia.game.Player;

public class MafiaConfig {
    
    public static Game game(List<Player> players) {
        return new Game(players);
    }

    public static List<Player> players(Parser parser) {
        List<String> names;
        try {
            int count = Integer.parseInt(
                parser.expect(
                    List.of(Answer.COUNT), "Enter number of players"
                ).get(0));
            names = parser.expect(Collections.nCopies(count, Answer.NAME), "Enter player's names");
            return List.of(names.stream().map(Player::new).toArray(Player[]::new));
        } catch (InvalidAlgorithmParameterException e) {
            System.err.println("fatal error occurred");
            System.exit(1);
        }

        return null;
    }

    public static Parser parser() {
        return new Parser(new InputStreamReader(System.in), new PrintWriter(System.out));
    }
}
