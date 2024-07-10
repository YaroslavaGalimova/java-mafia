package com.alexeyzarechnev.mafia;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class GameTests {

    public static class TestPlayer implements Player {

        private int awakeCount = 0;
        private int sleepCount = 0;

        @Override
        public void awake() {
            ++awakeCount;
        }

        @Override
        public void sleep() {
            ++sleepCount;
        }
    }

    public static class TestHost implements Host {

        private static Player killed;
        private static Player checked;
        private static Player healed;
        private static Player voted;

        @Override
        public Player getPlayerForAction(Action action) {
            Player result;
            switch (action) {
                case KILL -> result = killed;
                case INVESTIGATE-> result = checked;
                case HEAL -> result = healed;
                default -> result = voted;
            }
            return result;
        }
    }

    @Test
    public void startTest() {
        List<Player> players = List.of(new TestPlayer(), new TestPlayer());
        Game game = new Game(new TestHost(), players);
        Map<Player, Role> map = game.getAlivePlayers();
        assertEquals(2, map.size());
        assertTrue(game.isEnd());
    }

    private Game game;

    private List<Player> init(int count) {
        List<Player> players = new ArrayList<Player>();
        for (int i = 0; i < count; i++) { 
            players.add(new TestPlayer()); 
        }
        game = new Game(new TestHost(), players);
        return players;
    }

    @Test
    public void nightWithoutKillTest() {
        init(5);
        Map<Player, Role> map = game.getAlivePlayers();
        assertEquals(5, map.size());
        for (Map.Entry<Player, Role> e : map.entrySet())
            if (e.getValue().equals(Role.MAFIA)) {
                TestHost.healed = TestHost.checked = TestHost.killed = e.getKey();
            }
        game.playNight();
        map = game.getAlivePlayers();
        map.forEach((p, r) -> {
            TestPlayer tp = (TestPlayer)p;
            if (r.equals(Role.CITIZEN)) {
                assertEquals(1, tp.awakeCount);
                assertEquals(1, tp.sleepCount);
            } else {
                assertEquals(2, tp.awakeCount);
                assertEquals(2, tp.sleepCount);
            }
        });
        assertEquals(5, map.size());
        assertTrue(map.containsKey(TestHost.killed));
        assertFalse(game.isEnd());
    }

    @Test
    public void nightWithKillTest() {
        init(5);
        Map<Player, Role> map = game.getAlivePlayers();
        assertEquals(5, map.size());
        for (Map.Entry<Player, Role> e : map.entrySet())
            if (e.getValue().equals(Role.MAFIA)) {
                TestHost.healed = TestHost.checked = null;
                TestHost.killed = e.getKey();
            }
        game.playNight();
        map = game.getAlivePlayers();
        map.forEach((p, r) -> {
            TestPlayer tp = (TestPlayer)p;
             if (r.equals(Role.MAFIA)) {
                assertEquals(1, tp.awakeCount);
                assertEquals(2, tp.sleepCount);
            }
        });
        assertEquals(5, map.size());
        assertFalse(map.containsKey(TestHost.killed));
        assertTrue(game.isEnd());
    }

    @Test
    public void dayTest() {
        init(6);
        Map<Player, Role> map = game.getAlivePlayers();
        assertEquals(6, map.size());
        for (Map.Entry<Player, Role> e : map.entrySet())
            if (e.getValue().equals(Role.DOCTOR))
                TestHost.voted = e.getKey();
        game.playDay();
        map = game.getAlivePlayers();
        map.forEach((p, r) -> {
            TestPlayer tp = (TestPlayer)p;
            assertEquals(0, tp.awakeCount);
            assertEquals(0, tp.sleepCount);
        });
        assertEquals(5, map.size());
        assertFalse(map.containsKey(TestHost.voted));
        assertFalse(game.isEnd());
    }

    @Test
    public void lastDayTest() {
        init(6);
        Map<Player, Role> map = game.getAlivePlayers();
        assertEquals(6, map.size());
        for (Map.Entry<Player, Role> e : map.entrySet())
            if (e.getValue().equals(Role.MAFIA))
                TestHost.voted = e.getKey();
        game.playDay();
        map = game.getAlivePlayers();
        map.forEach((p, r) -> {
            TestPlayer tp = (TestPlayer)p;
            assertEquals(0, tp.awakeCount);
            assertEquals(0, tp.sleepCount);
        });
        assertEquals(5, map.size());
        assertFalse(map.containsKey(TestHost.voted));
        assertTrue(game.isEnd());
    }

    @Test
    public void killedNotAwaking() {
        init(5);
        Map<Player, Role> map = game.getAlivePlayers();
        assertEquals(5, map.size());
        for (Map.Entry<Player, Role> e : map.entrySet())
            if (e.getValue().equals(Role.DOCTOR)) {
                TestHost.healed = TestHost.checked = null;
                TestHost.killed = e.getKey();
            }
        game.playNight();
        TestHost.killed = null;
        game.playNight();
        map = game.getAlivePlayers();
        map.forEach((p, r) -> {
            TestPlayer tp = (TestPlayer)p;
             if (r.equals(Role.DOCTOR)) {
                assertEquals(1, tp.awakeCount);
                assertEquals(2, tp.sleepCount);
            }
        });
        assertEquals(5, map.size());
        assertFalse(map.containsKey(TestHost.killed));
        assertFalse(game.isEnd());
    }

    @Test
    public void restartTest() {
        init(5);
        Map<Player, Role> map1 = game.getAlivePlayers();
        TestHost.voted = map1.keySet().iterator().next();
        game.playDay();
        game.restart();
        Map<Player, Role> map2 = game.getAlivePlayers();
        assertEquals(map1.size(), map2.size());
        assertNotEquals(map1, map2);
    }
}
