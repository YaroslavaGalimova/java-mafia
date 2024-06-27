package com.alexeyzarechnev.mafia.game;

import java.util.regex.Pattern;

public enum Answer {
    COUNT("^\\d+$", "count"),
    NAME("^\\s*\\w+\\s*$", "name"),
    KILL_COMMAND("^\\s*kill\\s*$", "kill"),
    CHECK_COMMAND("^\\s*check\\s*$", "check"),
    HEAL_COMMAND("^\\s*heal\\s*$", "heal"),;
    // TODO: add other characters commands

    private Pattern regex;
    private final String str;

    private Answer(String regex, String str) {
        this.regex = Pattern.compile(regex);
        this.str = str;
    }

    public boolean check(String text) {
        return regex.matcher(text).matches();
    }

    @Override
    public String toString() {
        return str;
    }
}
