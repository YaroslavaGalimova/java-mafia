package com.alexeyzarechnev.mafia.characters;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RolesSet {

    private List<Role> roles;

    public RolesSet(int count) {
        roles = Role.getRoles(count);
        Collections.shuffle(roles, new Random(System.currentTimeMillis()));
    }

    public Role getRole() {
        return roles.remove(0);
    }
}
