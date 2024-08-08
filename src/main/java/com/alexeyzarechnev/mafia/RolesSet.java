package com.alexeyzarechnev.mafia;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RolesSet {

    private List<Role> roles;

    public RolesSet(int count, Random rand) {
        roles = Role.getRoles(count);
        int times = rand.nextInt(1000000);
        Random rnd = new Random(System.currentTimeMillis() + times);
        for (int i = 0; i < times; i++)
            Collections.shuffle(roles, rnd);
    }

    public Role getRole() {
        return roles.remove(0);
    }
}
