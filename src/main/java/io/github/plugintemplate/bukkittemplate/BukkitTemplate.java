package io.github.plugintemplate.bukkittemplate;

import co.aikar.commands.*;
import io.github.plugintemplate.bukkittemplate.commands.BukkitTemplateCommand;
import java.util.Optional;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

// TODO Change the class name as you want.
public final class BukkitTemplate extends JavaPlugin {

    private static BukkitTemplateAPI api;

    private static BukkitTemplate instance;

    @NotNull
    public static BukkitTemplate getInstance() {
        return Optional.ofNullable(BukkitTemplate.instance).orElseThrow(() ->
            new IllegalStateException("You cannot be used BukkitJavaGradle plugin before its start!"));
    }

    private void setInstance(@NotNull final BukkitTemplate instance) {
        if (Optional.ofNullable(BukkitTemplate.instance).isPresent()) {
            throw new IllegalStateException("You can't use #setInstance method twice!");
        }
        synchronized (this) {
            BukkitTemplate.instance = instance;
        }
    }

    @NotNull
    public static BukkitTemplateAPI getAPI() {
        return Optional.ofNullable(BukkitTemplate.api).orElseThrow(() ->
            new IllegalStateException("You cannot be used BukkitJavaGradle plugin before its start!"));
    }

    private void setAPI(@NotNull final BukkitTemplateAPI loader) {
        if (Optional.ofNullable(BukkitTemplate.api).isPresent()) {
            throw new IllegalStateException("You can't use #setAPI method twice!");
        }
        synchronized (this) {
            BukkitTemplate.api = loader;
        }
    }

    @Override
    public void onLoad() {
        this.setInstance(this);
    }

    @Override
    public void onDisable() {
        Optional.ofNullable(BukkitTemplate.api).ifPresent(BukkitTemplateAPI::disablePlugin);
    }

    @Override
    public void onEnable() {
        final BukkitTemplateAPI api = new BukkitTemplateAPI(this);
        this.setAPI(api);
        this.getServer().getScheduler().runTask(this, () ->
            this.getServer().getScheduler().runTaskAsynchronously(this, () ->
                api.reloadPlugin(true)));
        final BukkitCommandManager manager = new BukkitCommandManager(this);
        final CommandConditions<BukkitCommandIssuer, BukkitCommandExecutionContext, BukkitConditionContext> conditions =
            manager.getCommandConditions();
        conditions.addCondition(String[].class, "player", (context, exec, value) -> {
            if (value == null || value.length == 0) {
                return;
            }
            final int arg = context.getConfigValue("arg", 0);
            if (arg >= value.length) {
                return;
            }
            final String name = value[arg];
            if (context.hasConfig("arg") && Bukkit.getPlayer(name) == null) {
                throw new ConditionFailedException(api.languageFile.errors.player_not_found.get()
                    .build("%player_name%", () -> name));
            }
        });
        manager.registerCommand(new BukkitTemplateCommand());
    }

}
