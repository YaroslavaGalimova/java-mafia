package com.alexeyzarechnev.mafia;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Game {

    private final List<Player> allPlayers;
    private final Host host;
    private Map<Player, Role> alivePlayers; 

    public Game(Host host, List<Player> players) {
        this.host = host;
        this.allPlayers = players;
        restart();   
    }

    public void restart() {
        RolesSet set = new RolesSet(allPlayers.size());
        this.alivePlayers = allPlayers.stream()
               .collect(Collectors.toMap(p -> p, p -> set.getRole()));
    }

    public boolean isEnd() {
        // TODO: implement logic for game end condition
        return false;
    }

    private void round(Role role) {
        Action action = role.getAction();
        awake(role);
        doAction(action, host.getPlayerForAction(action));
        sleep(role);
    }

    public void playNight() {
        sleepAll();
        round(Role.MAFIA);
        round(Role.POLICEMAN);
        round(Role.DOCTOR);
        awakeAll();
    }

    public void playDay() {
        doAction(Action.KICK, host.getPlayerForAction(Action.KICK));
    }

    private void doAction(Action action, Player player) {
        // TODO: implement logic for killing someone
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
