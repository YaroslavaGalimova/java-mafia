package com.alexeyzarechnev.mafia.game;

public class Player {

    private final String name;

    public Player(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
