package io.github.plugintemplate.bukkittemplate.hooks;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class LuckPermsWrapper implements Wrapped {

    @NotNull
    private final LuckPerms luckPerms;

    public LuckPermsWrapper(@NotNull final LuckPerms luckPerms) {
        this.luckPerms = luckPerms;
    }

    @NotNull
    public String getGroup(@NotNull final Player player) {
        final User user = this.luckPerms.getUserManager().getUser(player.getUniqueId());
        if (user == null) {
            return "";
        }
        return user.getPrimaryGroup();
    }

    // TODO Add new LuckPerms methods as you want.

}
