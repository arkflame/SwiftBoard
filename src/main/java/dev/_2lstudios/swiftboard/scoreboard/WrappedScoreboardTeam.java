package dev._2lstudios.swiftboard.scoreboard;

    import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Team.OptionStatus;

import com.comphenix.protocol.reflect.accessors.Accessors;
import com.comphenix.protocol.reflect.accessors.FieldAccessor;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.AbstractWrapper;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.EnumWrappers.IndexedEnumConverter;
import com.comphenix.protocol.wrappers.WrappedChatComponent;

public class WrappedScoreboardTeam extends AbstractWrapper {
    private static final FieldAccessor DISPLAY_NAME = Accessors
        .getFieldAccessor(MinecraftReflection.getMinecraftClass("network.protocol.game.PacketPlayOutScoreboardTeam$b"), "a", true);
    private static final FieldAccessor PREFIX = Accessors
        .getFieldAccessor(MinecraftReflection.getMinecraftClass("network.protocol.game.PacketPlayOutScoreboardTeam$b"), "b", true);
    private static final FieldAccessor SUFFIX = Accessors
        .getFieldAccessor(MinecraftReflection.getMinecraftClass("network.protocol.game.PacketPlayOutScoreboardTeam$b"), "c", true);

    private static final FieldAccessor NAME_TAG_VISIBILITY = Accessors
        .getFieldAccessor(MinecraftReflection.getMinecraftClass("network.protocol.game.PacketPlayOutScoreboardTeam$b"), "d", true);
    private static final FieldAccessor COLLISION_RULE = Accessors
        .getFieldAccessor(MinecraftReflection.getMinecraftClass("network.protocol.game.PacketPlayOutScoreboardTeam$b"), "e", true);
    private static final FieldAccessor TEAM_COLOR = Accessors
        .getFieldAccessor(MinecraftReflection.getMinecraftClass("network.protocol.game.PacketPlayOutScoreboardTeam$b"), "f", true);
    private static final FieldAccessor FRIENDLY_FLAGS = Accessors
        .getFieldAccessor(MinecraftReflection.getMinecraftClass("network.protocol.game.PacketPlayOutScoreboardTeam$b"), "g", true);

    private static final Class<?> ENUM_CHAT_FORMAT_CLASS = MinecraftReflection.getMinecraftClass("EnumChatFormat");
    private static final IndexedEnumConverter<ChatColor> CHATCOLOR_CONVERTER = new EnumWrappers.IndexedEnumConverter<>(ChatColor.class,
        ENUM_CHAT_FORMAT_CLASS);

    private WrappedScoreboardTeam(Object handle) {
        super(MinecraftReflection.getMinecraftClass("network.protocol.game.PacketPlayOutScoreboardTeam$b"));
        setHandle(handle);
    }

    public static WrappedScoreboardTeam fromHandle(Object handle) {
        return new WrappedScoreboardTeam(handle);
    }

    public WrappedChatComponent getDisplayName() {
        return WrappedChatComponent.fromHandle(DISPLAY_NAME.get(this.handle));
    }

    public void setDisplayName(WrappedChatComponent displayName) {
        DISPLAY_NAME.set(this.handle, displayName.getHandle());
    }

    public WrappedChatComponent getPrefix() {
        return WrappedChatComponent.fromHandle(PREFIX.get(this.handle));
    }

    public void setPrefix(WrappedChatComponent prefix) {
        PREFIX.set(this.handle, prefix.getHandle());
    }

    public WrappedChatComponent getSuffix() {
        return WrappedChatComponent.fromHandle(SUFFIX.get(this.handle));
    }

    public void setSuffix(WrappedChatComponent suffix) {
        SUFFIX.set(this.handle, suffix.getHandle());
    }

    public OptionStatus getNameTagVisibility() {
        final String value = (String) NAME_TAG_VISIBILITY.get(this.handle);
        switch (value) {
            case "always":
                return OptionStatus.ALWAYS;
            case "never":
                return OptionStatus.NEVER;
            case "hideForOtherTeams":
                return OptionStatus.FOR_OTHER_TEAMS;
            case "hideForOwnTeam":
                return OptionStatus.FOR_OWN_TEAM;
            default:
                throw new IllegalArgumentException("Unexpected value: " + value);
        }
    }

    public void setNameTagVisibility(OptionStatus value) {
        switch (value) {
            case ALWAYS:
                NAME_TAG_VISIBILITY.set(this.handle, "always");
                break;
            case NEVER:
                NAME_TAG_VISIBILITY.set(this.handle, "never");
                break;
            case FOR_OTHER_TEAMS:
                NAME_TAG_VISIBILITY.set(this.handle, "hideForOtherTeams");
                break;
            case FOR_OWN_TEAM:
                NAME_TAG_VISIBILITY.set(this.handle, "hideForOwnTeam");
                break;
            default:
                throw new IllegalArgumentException("Unexpected value: " + value);
        }
    }

    public OptionStatus getCollisionRule() {
        final String value = (String) COLLISION_RULE.get(this.handle);
        switch (value) {
            case "always":
                return OptionStatus.ALWAYS;
            case "never":
                return OptionStatus.NEVER;
            case "pushOtherTeams":
                return OptionStatus.FOR_OTHER_TEAMS;
            case "pushOwnTeam":
                return OptionStatus.FOR_OWN_TEAM;
            default:
                throw new IllegalArgumentException("Unexpected value: " + value);
        }
    }

    public void setCollisionRule(OptionStatus value) {
        switch (value) {
            case ALWAYS:
                COLLISION_RULE.set(this.handle, "always");
                break;
            case NEVER:
                COLLISION_RULE.set(this.handle, "never");
                break;
            case FOR_OTHER_TEAMS:
                COLLISION_RULE.set(this.handle, "pushOtherTeams");
                break;
            case FOR_OWN_TEAM:
                COLLISION_RULE.set(this.handle, "pushOwnTeam");
                break;
            default:
                throw new IllegalArgumentException("Unexpected value: " + value);
        }
    }

    public ChatColor getTeamColor() {
        return ChatColor.getByChar(TEAM_COLOR.get(this.handle).toString().charAt(1));
    }

    public void setTeamColor(ChatColor value) {
        if (value.isColor() || value == ChatColor.RESET) {
            TEAM_COLOR.set(this.handle, CHATCOLOR_CONVERTER.getGeneric(value));
        }
    }

    public boolean getFriendlyFire() {
        int intValue = ((Integer) FRIENDLY_FLAGS.get(this.handle)).intValue();
        return (intValue & 0x01) == 1;
    }

    public void setFriendlyFire(boolean value) {
        int currentValue = ((Integer) FRIENDLY_FLAGS.get(this.handle)).intValue();
        if (getFriendlyFire() && !value) {
            // is already friendly fire but should not be
            FRIENDLY_FLAGS.set(this.handle, Integer.valueOf(currentValue ^ 0x01));
        }
        else if (value) {
            // is already friendly fire but should not be
            FRIENDLY_FLAGS.set(this.handle, Integer.valueOf(currentValue | 0x01));
        }
    }

    public boolean getFriendlySeeInvisible() {
        int intValue = ((Integer) FRIENDLY_FLAGS.get(this.handle)).intValue();
        return (intValue & 0x02) == 2;
    }

    public void setFriendlySeeInvisible(boolean value) {
        int currentValue = ((Integer) FRIENDLY_FLAGS.get(this.handle)).intValue();
        if (getFriendlyFire() && !value) {
            // is already friendly fire but should not be
            FRIENDLY_FLAGS.set(this.handle, Integer.valueOf(currentValue ^ 0x02));
        }
        else if (value) {
            // is already friendly fire but should not be
            FRIENDLY_FLAGS.set(this.handle, Integer.valueOf(currentValue | 0x02));
        }
    }
}
