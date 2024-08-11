package com.alexeyzarechnev.mafia;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import com.alexeyzarechnev.mafia.exceptions.CharacterSelectionException;
import com.alexeyzarechnev.mafia.exceptions.InvalidTimeException;

import java.util.Set;
import java.util.HashSet;

public class Game {

    private final List<Player> allPlayers;
    private final Random random; 
    private final Host host;
    private Map<Player, Role> alivePlayers; 
    private Set<Player> killedPlayers;
    private boolean isDay;

    public Game(Host host, List<Player> players) {
        this.host = host;
        this.allPlayers = players;
        this.killedPlayers = new HashSet<Player>();
        this.random = new Random(System.currentTimeMillis());
        this.isDay = false;
        restart();   
    }

    public void restart() {
        RolesSet set = new RolesSet(allPlayers.size(), random);
        this.alivePlayers = allPlayers.stream()
               .collect(Collectors.toMap(p -> p, p -> set.getRole()));
    }

    public boolean isEnd() {
        long countOfMafia = alivePlayers.values().stream()
                            .filter(r -> r.equals(Role.MAFIA))
                            .count();
        return countOfMafia * 2 >= alivePlayers.size() || countOfMafia == 0;
    }

    private void round(Role role) {
        Action action = role.getAction();
        awake(role);
        try {
            doAction(action, host.getPlayerForAction(action));
        } catch (CharacterSelectionException e) {
            for (Map.Entry<Player, Role> entry : alivePlayers.entrySet()) {
                if (entry.getValue().equals(role)) {
                    entry.getKey().getMessage("You can't choose this player. Please, try again");
                }
            }
        }
        sleep(role);
    }

    private void removePlayers() {
        for (Player player : killedPlayers) {
            alivePlayers.remove(player);
        }
        killedPlayers.clear();
    }

    public void playNight() throws InvalidTimeException {
        if (isDay) {
            throw new InvalidTimeException();
        }
        sleepAll();
        round(Role.MAFIA);
        round(Role.POLICEMAN);
        round(Role.DOCTOR);
        removePlayers();
        isDay = true;
        awakeAll();
    }

    public void playDay() throws InvalidTimeException {
        if (!isDay) {
            throw new InvalidTimeException();
        }
        try {
            doAction(Action.KICK, host.getPlayerForAction(Action.KICK));
        } catch (CharacterSelectionException e) {
            host.getMessage("This player isn't alive. Please, try again");
            //playDay();
        }
        removePlayers();
        isDay = false;
    }

    private void doAction(Action action, Player player) throws CharacterSelectionException {
        if (!alivePlayers.containsKey(player)) {
            throw new CharacterSelectionException();
        }
        switch(action) {
            case KILL:
                killedPlayers.add(player);
            case INVESTIGATE:
                String message = alivePlayers.get(player).isBlack() ? "This player is black" : "This player isn't black";
                for (Map.Entry<Player, Role> entry : alivePlayers.entrySet()) {
                    if (entry.getValue().equals(Role.POLICEMAN)) {
                        entry.getKey().getMessage(message);
                    }
                }
            case HEAL:
                killedPlayers.remove(player);
            case KICK:
                killedPlayers.add(player);
        }
    }

    private void sleep(Role role) {
        alivePlayers.forEach((p, r) -> {
            if (r.equals(role))
                p.sleep();
        });
    }

    private void sleepAll() {
        alivePlayers.forEach((p, r) -> { p.sleep(); });
    }

    private void awake(Role role) {
        alivePlayers.forEach((p, r) -> {
            if (r.equals(role))
                p.awake();
        });
    }

    private void awakeAll() {
        alivePlayers.forEach((p, r) -> { p.awake(); });
    }

    public Map<Player, Role> getAlivePlayers() {
        return alivePlayers;
    }
}
