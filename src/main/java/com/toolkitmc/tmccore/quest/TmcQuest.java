package com.toolkitmc.tmccore.quest;

import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TmcQuest {

    private static final Map<UUID, Map<String, QuestData>> playerQuests = new HashMap<>();

    public static void startQuest(ServerPlayerEntity player, String questId) {
        playerQuests.computeIfAbsent(player.getUuid(), k -> new HashMap<>())
                    .put(questId, new QuestData(false, 0));
    }

    public static void completeQuest(ServerPlayerEntity player, String questId) {
        var quests = playerQuests.computeIfAbsent(player.getUuid(), k -> new HashMap<>());
        quests.put(questId, new QuestData(true, quests.getOrDefault(questId, new QuestData(false, 0)).progress));
    }

    public static void setProgress(ServerPlayerEntity player, String questId, int progress) {
        var quests = playerQuests.computeIfAbsent(player.getUuid(), k -> new HashMap<>());
        boolean completed = quests.getOrDefault(questId, new QuestData(false, 0)).completed;
        quests.put(questId, new QuestData(completed, progress));
    }

    public static boolean isCompleted(ServerPlayerEntity player, String questId) {
        return playerQuests.getOrDefault(player.getUuid(), Map.of())
                           .getOrDefault(questId, new QuestData(false, 0)).completed;
    }

    public static int getProgress(ServerPlayerEntity player, String questId) {
        return playerQuests.getOrDefault(player.getUuid(), Map.of())
                           .getOrDefault(questId, new QuestData(false, 0)).progress;
    }

    private record QuestData(boolean completed, int progress) {}
}