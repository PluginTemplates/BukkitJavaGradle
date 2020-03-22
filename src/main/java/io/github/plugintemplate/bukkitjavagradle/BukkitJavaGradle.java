package io.github.plugintemplate.bukkitjavagradle;

import co.aikar.commands.BukkitCommandManager;
import co.aikar.commands.ConditionFailedException;
import io.github.plugintemplate.bukkitjavagradle.commands.BukkitJavaGradleCommand;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

// TODO Change the class name as you want.
public final class BukkitJavaGradle extends JavaPlugin {

    private static BukkitJavaGradleAPI api;

    private static BukkitJavaGradle instance;

    @NotNull
    public static BukkitJavaGradle getInstance() {
        if (BukkitJavaGradle.instance == null) {
            // TODO Change the BukkitJavaGradle case as you want.
            throw new IllegalStateException("You cannot be used BukkitJavaGradle plugin before its start!");
        }

        return BukkitJavaGradle.instance;
    }

    @NotNull
    public static BukkitJavaGradleAPI getAPI() {
        if (BukkitJavaGradle.api == null) {
            // TODO Change the BukkitJavaGradle case as you want.
            throw new IllegalStateException("You cannot be used BukkitJavaGradle plugin before its start!");
        }

        return BukkitJavaGradle.api;
    }

    @Override
    public void onLoad() {
        BukkitJavaGradle.instance = this;
    }

    @Override
    public void onDisable() {
        if (BukkitJavaGradle.api != null) {
            BukkitJavaGradle.api.disablePlugin();
        }
    }

    @Override
    public void onEnable() {
        final BukkitCommandManager manager = new BukkitCommandManager(this);
        BukkitJavaGradle.api = new BukkitJavaGradleAPI(this);

        this.getServer().getScheduler().runTask(this, () ->
            this.getServer().getScheduler().runTaskAsynchronously(this, () ->
                BukkitJavaGradle.api.reloadPlugin(true)
            )
        );
        manager.getCommandConditions().addCondition(String[].class, "player", (c, exec, value) -> {
            if (value == null || value.length == 0) {
                return;
            }

            final String playerName = value[c.getConfigValue("arg", 0)];

            if (c.hasConfig("arg") && Bukkit.getPlayer(playerName) == null) {
                throw new ConditionFailedException(
                    BukkitJavaGradle.api.languageFile.errors.player_not_found.build("%player_name%", () -> playerName)
                );
            }
        });
        manager.registerCommand(
            new BukkitJavaGradleCommand(BukkitJavaGradle.api)
        );
    }

}
