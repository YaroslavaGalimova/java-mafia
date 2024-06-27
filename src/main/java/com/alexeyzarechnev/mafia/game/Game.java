package com.alexeyzarechnev.mafia.game;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.alexeyzarechnev.mafia.characters.Role;
import com.alexeyzarechnev.mafia.characters.RolesSet;

public class Game {

    private final Map<Player, Role> players; 

    public Game(List<Player> players) {
        RolesSet set = new RolesSet(players.size());
        this.players = players.stream()
               .collect(Collectors.toMap(p -> p, p -> set.getRole()));   
    }

    public void display() {
        players.forEach((p, r) -> System.out.println(p + " is " + r));
    };
}
