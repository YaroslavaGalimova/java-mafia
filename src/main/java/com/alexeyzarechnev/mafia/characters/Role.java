package com.alexeyzarechnev.mafia.characters;

import java.util.ArrayList;
import java.util.List;

public enum Role {
    MAFIA("mafia"),
    CITIZEN("citizen"),
    POLICEMAN("policeman"),
    DOCTOR("doctor");
    // TODO: SHERIFF, HARLOT, WEREWOLF, KAMIKAZE, MANIAC

    private String str;

    private Role(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return str;
    }

    private static List<Role> rolesSet = List.of(
        CITIZEN,
        CITIZEN,
        CITIZEN,
        MAFIA,
        POLICEMAN,
        DOCTOR,
        CITIZEN,
        CITIZEN,
        MAFIA,
        CITIZEN,
        CITIZEN
        //TODO: add more roles for more players
    );
    
    public static List<Role> getRoles(int count) {
        return new ArrayList<Role>(rolesSet.subList(0, count));
    }
}
