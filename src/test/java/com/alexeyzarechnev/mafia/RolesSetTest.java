package com.alexeyzarechnev.mafia;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class RolesSetTest {

    @ParameterizedTest
    @ValueSource(ints = {5, 6, 7, 8, 9, 10})
    public void differenceTest(int count) {
        Random rnd = new Random(System.currentTimeMillis());
        RolesSet set1 = new RolesSet(count, rnd);
        RolesSet set2 = new RolesSet(count, rnd);
        List<Role> roles1 = new ArrayList<Role>();
        List<Role> roles2 = new ArrayList<Role>();
        for (int i = 0; i < count; ++i) {
            roles1.add(set1.getRole());
            roles2.add(set2.getRole());
        }
        assertNotEquals(roles1, roles2);
    }
}
