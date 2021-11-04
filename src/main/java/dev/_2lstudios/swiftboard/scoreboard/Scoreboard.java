package dev._2lstudios.swiftboard.scoreboard;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.EnumWrappers.ScoreboardAction;
import com.comphenix.protocol.wrappers.WrappedChatComponent;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import dev._2lstudios.swiftboard.scoreboard.wrappers.WrappedScoreboardTeam;

public class Scoreboard {
    private final ProtocolManager protocolManager;
    private final Player player;
    private final Map<String, Objective> objectives = new ConcurrentHashMap<>();
    private final Map<String, Team> teams = new ConcurrentHashMap<>();

    public Scoreboard(final Player player) {
        this.protocolManager = ProtocolLibrary.getProtocolManager();
        this.player = player;
    }

    public void displayObjective(final int position, final String name) throws InvocationTargetException {
        if (name.length() > 16) {
            return;
        }

        final Objective objective = getObjective(name);

        if (objective != null) {
            final PacketContainer packet = protocolManager
                    .createPacket(PacketType.Play.Server.SCOREBOARD_DISPLAY_OBJECTIVE);

            packet.getIntegers().writeSafely(0, position);
            packet.getStrings().writeSafely(0, name);
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

    public void createObjective(final String name, final String displayName, final HealthDisplay healthDisplay)
            throws InvocationTargetException {
        if (name.length() > 16) {
            return;
        }

        if (!objectives.containsKey(name)) {
            final PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.SCOREBOARD_OBJECTIVE);

            packet.getStrings().writeSafely(0, name);
            packet.getIntegers().writeSafely(0, 0);
            packet.getStrings().writeSafely(1, displayName);
            packet.getChatComponents().writeSafely(0, WrappedChatComponent.fromText(displayName));
            packet.getEnumModifier(HealthDisplay.class, 2).writeSafely(0, healthDisplay);
            protocolManager.sendServerPacket(player, packet);

            objectives.put(name, new Objective(name, displayName));
        }
    }

    public void removeObjective(final String name) throws InvocationTargetException {
        if (name.length() > 16) {
            return;
        }

        if (objectives.containsKey(name)) {
            final PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.SCOREBOARD_OBJECTIVE);

            packet.getStrings().writeSafely(0, name);
            packet.getIntegers().writeSafely(0, 1);
            protocolManager.sendServerPacket(player, packet);
            objectives.remove(name);
        }
    }

    public void clearObjectives() throws InvocationTargetException {
        for (final String objectiveName : objectives.keySet()) {
            removeObjective(objectiveName);
        }
    }

    public void updateObjective(final String name, final String displayName, final HealthDisplay healthDisplay)
            throws InvocationTargetException {
        if (name.length() > 16) {
            return;
        }

        final Objective objective = getObjective(name);

        if (objective != null) {
            final PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.SCOREBOARD_OBJECTIVE);

            packet.getStrings().writeSafely(0, name);
            packet.getIntegers().writeSafely(0, 2);
            packet.getStrings().writeSafely(1, displayName);
            packet.getChatComponents().writeSafely(0, WrappedChatComponent.fromText(displayName));
            packet.getEnumModifier(HealthDisplay.class, 2).writeSafely(0, healthDisplay);
            protocolManager.sendServerPacket(player, packet);
            objective.setDisplayName(displayName);
        }
    }

    public void updateScore(final String name, final String entity, final Integer score)
            throws InvocationTargetException {
        if (name.length() > 16) {
            return;
        }

        final Objective objective = getObjective(name);

        if (objective != null && (!objective.hasScore(entity) || objective.getScore(entity) != score)) {
            final PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.SCOREBOARD_SCORE);

            packet.getStrings().writeSafely(0, entity);
            packet.getScoreboardActions().writeSafely(0, ScoreboardAction.CHANGE);
            packet.getStrings().writeSafely(1, name);
            packet.getIntegers().writeSafely(0, score);
            protocolManager.sendServerPacket(player, packet);

            objective.updateScore(entity, score);
        }
    }

    public void removeScore(final String name, final String entity) throws InvocationTargetException {
        if (name.length() > 16) {
            return;
        }

        final Objective objective = getObjective(name);

        if (objective != null && objective.hasScore(entity)) {
            final PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.SCOREBOARD_SCORE);

            packet.getStrings().writeSafely(0, entity);
            packet.getScoreboardActions().writeSafely(0, ScoreboardAction.REMOVE);
            packet.getStrings().writeSafely(1, name);
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

    private PacketContainer generateTeamUpdatePacket(final int mode, String name, String displayName,
            final String prefix, final String suffix) {
        final PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.SCOREBOARD_TEAM);
        final StructureModifier<Integer> integers = packet.getIntegers();
        final StructureModifier<String> strings = packet.getStrings();
        final StructureModifier<WrappedChatComponent> chatComponents = packet.getChatComponents();
        final StructureModifier<Optional<?>> optionals = packet.getModifier().withType(Optional.class);

        if (integers.size() > 1) {
            integers.write(1, mode); // mode
        } else if (integers.size() > 0) {
            integers.write(0, mode); // mode
        }

        strings.writeSafely(0, name); // team name
        strings.writeSafely(1, displayName); // team display name
        if (!prefix.isEmpty()) strings.writeSafely(2, prefix); // prefix
        if (!suffix.isEmpty()) strings.writeSafely(3, suffix); // suffix

        chatComponents.writeSafely(0, WrappedChatComponent.fromText(displayName)); // team display name
        chatComponents.writeSafely(1, WrappedChatComponent.fromText(prefix)); // prefix
        chatComponents.writeSafely(2, WrappedChatComponent.fromText(suffix)); // suffix

        if (optionals.size() > 0) {
            final WrappedScoreboardTeam team = WrappedScoreboardTeam.fromHandle(optionals.read(0).get());

            team.setDisplayName(WrappedChatComponent.fromText(displayName)); // team display name
            team.setPrefix(WrappedChatComponent.fromText(prefix)); // prefix
            team.setSuffix(WrappedChatComponent.fromText(suffix)); // suffix
            team.setTeamColor(ChatColor.RESET); // TODO: We want to keep the prefix last color
            optionals.write(0, Optional.of(team.getHandle()));
        }

        return packet;
    }

    public void createTeam(final String name, final String displayName, final String prefix, final String suffix,
            final List<String> entities) throws InvocationTargetException {
        if (displayName.length() > 40) {
            return;
        }

        if (name.length() > 16) {
            return;
        }

        if (!teams.containsKey(name)) {
            final Team team = new Team(displayName, prefix, suffix, entities);
            final PacketContainer packet = generateTeamUpdatePacket(0, name, displayName, prefix, suffix);

            packet.getSpecificModifier(Collection.class).writeSafely(0, entities); // players

            protocolManager.sendServerPacket(player, packet);
            teams.put(name, team);
        }
    }

    public void updateTeam(final String name, final String displayName, final String prefix, final String suffix)
            throws InvocationTargetException {
        if (displayName.length() > 40) {
            return;
        }

        if (name.length() > 16) {
            return;
        }

        if (teams.containsKey(name)) {
            final Team team = getTeam(name);

            if (team.hasChanges(displayName, prefix, suffix)) {
                final PacketContainer packet = generateTeamUpdatePacket(2, name, displayName, prefix, suffix);

                protocolManager.sendServerPacket(player, packet);
                team.update(displayName, prefix, suffix);
            }
        }
    }

    public void createTeam(final String teamName, final String teamDisplayName, final String prefix,
            final String suffix, final String... entities) throws InvocationTargetException {
        createTeam(teamName, teamDisplayName, prefix, suffix, Arrays.asList(entities));
    }

    public void removeTeam(final String name) throws InvocationTargetException {
        if (name.length() > 16) {
            return;
        }

        if (teams.containsKey(name)) {
            final PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.SCOREBOARD_TEAM);

            packet.getIntegers().writeSafely(0, 1); // mode
            packet.getIntegers().writeSafely(1, 1); // mode
            packet.getStrings().writeSafely(0, name); // team name

            protocolManager.sendServerPacket(player, packet);
            teams.remove(name);
        }
    }

    public void clearTeams() throws InvocationTargetException {
        for (final String teamName : teams.keySet()) {
            removeTeam(teamName);
        }
    }

    public void addTeamEntity(final String name) throws InvocationTargetException {
        if (name.length() > 16) {
            return;
        }

        final PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.SCOREBOARD_TEAM);

        protocolManager.sendServerPacket(player, packet);
    }

    public void removeTeamEntity(final String name) throws InvocationTargetException {
        if (name.length() > 16) {
            return;
        }

        final PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.SCOREBOARD_TEAM);

        protocolManager.sendServerPacket(player, packet);
    }

    public Player getPlayer() {
        return player;
    }
}
