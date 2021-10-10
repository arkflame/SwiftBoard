package dev._2lstudios.swiftboard.scoreboard;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers.ScoreboardAction;
import com.comphenix.protocol.wrappers.WrappedChatComponent;

import org.bukkit.entity.Player;

public class Scoreboard {
    private final ProtocolManager protocolManager;
    private final Player player;
    private final Map<String, Objective> objectives = new ConcurrentHashMap<>();
    private final Map<String, Team> teams = new ConcurrentHashMap<>();

    public Scoreboard(final Player player) {
        this.protocolManager = ProtocolLibrary.getProtocolManager();
        this.player = player;
    }

    public void displayObjective(final int position, final String objectiveName) throws InvocationTargetException {
        final Objective objective = getObjective(objectiveName);

        if (objective != null) {
            final PacketContainer packet = protocolManager
                    .createPacket(PacketType.Play.Server.SCOREBOARD_DISPLAY_OBJECTIVE);

            packet.getIntegers().write(0, position);
            packet.getStrings().write(0, objectiveName);

            protocolManager.sendServerPacket(player, packet);
            objective.setPosition(position);
        }
    }

    public Objective getObjective(final String objectiveName) {
        return objectives.getOrDefault(objectiveName, null);
    }

    public boolean containsObjective(final String objectiveName) {
        return objectives.containsKey(objectiveName);
    }

    public void createObjective(final String objectiveName, final String displayName, final HealthDisplay healthDisplay)
            throws InvocationTargetException {
        if (!objectives.containsKey(objectiveName)) {
            final PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.SCOREBOARD_OBJECTIVE);

            packet.getStrings().write(0, objectiveName);
            packet.getIntegers().write(0, 0);

            if (packet.getStrings().size() > 1) {
                packet.getStrings().write(1, displayName);
            }

            if (packet.getChatComponents().size() > 0) {
                packet.getChatComponents().write(0, WrappedChatComponent.fromText(displayName));
            }

            packet.getEnumModifier(HealthDisplay.class, 2).write(0, healthDisplay);

            protocolManager.sendServerPacket(player, packet);

            objectives.put(objectiveName, new Objective(objectiveName, displayName));
        }
    }

    public void removeObjective(final String objectiveName) throws InvocationTargetException {
        if (objectives.containsKey(objectiveName)) {
            final PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.SCOREBOARD_OBJECTIVE);

            packet.getStrings().write(0, objectiveName);
            packet.getIntegers().write(0, 1);

            protocolManager.sendServerPacket(player, packet);
            objectives.remove(objectiveName);
        }
    }

    public void clearObjectives() throws InvocationTargetException {
        for (final String objectiveName : objectives.keySet()) {
            removeObjective(objectiveName);
        }
    }

    public void updateObjective(final String objectiveName, final String displayName,
            final HealthDisplay healthDisplay) throws InvocationTargetException {
        final Objective objective = getObjective(objectiveName);

        if (objective != null) {
            final PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.SCOREBOARD_OBJECTIVE);

            packet.getStrings().write(0, objectiveName);
            packet.getIntegers().write(0, 2);

            if (packet.getStrings().size() > 1) {
                packet.getStrings().write(1, displayName);
            }

            if (packet.getChatComponents().size() > 0) {
                packet.getChatComponents().write(0, WrappedChatComponent.fromText(displayName));
            }

            packet.getEnumModifier(HealthDisplay.class, 2).write(0, healthDisplay);

            protocolManager.sendServerPacket(player, packet);
            objective.setDisplayName(displayName);
        }
    }

    public void updateScore(final String objectiveName, final String entity, final Integer score)
            throws InvocationTargetException {
        final Objective objective = getObjective(objectiveName);

        if (objective != null && (!objective.hasScore(entity) || objective.getScore(entity) != score)) {
            final PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.SCOREBOARD_SCORE);

            packet.getStrings().write(0, entity);
            packet.getScoreboardActions().write(0, ScoreboardAction.CHANGE);
            packet.getStrings().write(1, objectiveName);
            packet.getIntegers().write(0, score);

            protocolManager.sendServerPacket(player, packet);

            objective.updateScore(entity, score);
        }
    }

    public void removeScore(final String objectiveName, final String entity) throws InvocationTargetException {
        final Objective objective = getObjective(objectiveName);

        if (objective != null && objective.hasScore(entity)) {
            final PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.SCOREBOARD_SCORE);

            packet.getStrings().write(0, entity);
            packet.getScoreboardActions().write(0, ScoreboardAction.REMOVE);
            packet.getStrings().write(1, objectiveName);

            protocolManager.sendServerPacket(player, packet);

            objective.updateScore(entity, -1);
        }
    }

    public Team getTeam(final String teamName) {
        return teams.getOrDefault(teamName, null);
    }

    public boolean containsTeam(final String name) {
        return teams.containsKey(name);
    }

    public void createTeam(final String teamName, final String teamDisplayName, final String prefix,
            final String suffix, final List<String> entities) throws InvocationTargetException {
        if (!teams.containsKey(teamName)) {
            final Team team = new Team(teamDisplayName, prefix, suffix, entities);
            final PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.SCOREBOARD_TEAM);

            packet.getStrings().writeSafely(0, teamName); // team name
            packet.getIntegers().writeSafely(1, 0); // mode

            packet.getStrings().writeSafely(1, teamDisplayName); // team display name
            packet.getStrings().writeSafely(2, prefix); // prefix
            packet.getStrings().writeSafely(3, suffix); // suffix

            packet.getChatComponents().writeSafely(0, WrappedChatComponent.fromText(teamDisplayName)); // team display
            packet.getChatComponents().writeSafely(1, WrappedChatComponent.fromText(prefix)); // prefix
            packet.getChatComponents().writeSafely(2, WrappedChatComponent.fromText(suffix)); // suffix

            packet.getIntegers().writeSafely(0, entities.size()); // player count
            packet.getSpecificModifier(Collection.class).writeSafely(0, entities); // players

            protocolManager.sendServerPacket(player, packet);
            teams.put(teamName, team);
        }
    }

    public void createTeam(final String teamName, final String teamDisplayName, final String prefix,
            final String suffix, final String... entities) throws InvocationTargetException {
        createTeam(teamName, teamDisplayName, prefix, suffix, Arrays.asList(entities));
    }

    public void removeTeam(final String teamName) throws InvocationTargetException {
        if (teams.containsKey(teamName)) {
            final PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.SCOREBOARD_TEAM);

            packet.getStrings().write(0, teamName); // team name
            packet.getIntegers().write(1, 1); // mode

            protocolManager.sendServerPacket(player, packet);
            teams.remove(teamName);
        }
    }

    public void clearTeams() throws InvocationTargetException {
        for (final String teamName : teams.keySet()) {
            removeTeam(teamName);
        }
    }

    public void updateTeam(final String teamName, final String teamDisplayName, final String prefix,
            final String suffix) throws InvocationTargetException {
        if (teams.containsKey(teamName)) {
            final Team team = getTeam(teamName);

            if (team.hasChanges(teamDisplayName, prefix, suffix)) {
                final PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.SCOREBOARD_TEAM);

                packet.getStrings().writeSafely(0, teamName);
                packet.getIntegers().writeSafely(1, 2);
                packet.getStrings().writeSafely(1, teamDisplayName);
                packet.getStrings().writeSafely(2, prefix);
                packet.getStrings().writeSafely(3, suffix);

                protocolManager.sendServerPacket(player, packet);
                team.update(teamDisplayName, prefix, suffix);
            }
        }
    }

    public void addTeamEntity() throws InvocationTargetException {
        final PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.SCOREBOARD_TEAM);

        protocolManager.sendServerPacket(player, packet);
    }

    public void removeTeamEntity() throws InvocationTargetException {
        final PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.SCOREBOARD_TEAM);

        protocolManager.sendServerPacket(player, packet);
    }

    public Player getPlayer() {
        return player;
    }
}
