package com.toolkitmc.tmccore.team;

import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.Map;

public class TmcTeam {
    private static final Map<String, Team> teams = new HashMap<>();

    public static void createTeam(MinecraftServer server, String name, Text displayName) {
        Team team = server.getScoreboard().addTeam(name);
        team.setDisplayName(displayName);
        teams.put(name, team);
    }

    public static Team getTeam(String name) {
        return teams.get(name);
    }
}