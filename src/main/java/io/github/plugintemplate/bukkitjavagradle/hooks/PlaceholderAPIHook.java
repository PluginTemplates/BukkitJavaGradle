package io.github.plugintemplate.bukkitjavagradle.hooks;

import io.github.plugintemplate.bukkitjavagradle.Hook;
import io.github.plugintemplate.bukkitjavagradle.Wrapped;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public final class PlaceholderAPIHook implements Hook {

    private PlaceholderAPIPlugin placeholderAPI;

    @Override
    public boolean initiate() {
        return (this.placeholderAPI = (PlaceholderAPIPlugin) Bukkit.getPluginManager().getPlugin("PlaceholderAPI")) != null;
    }

    @Override
    @NotNull
    public Wrapped create() {
        if (this.placeholderAPI == null) {
            throw new IllegalStateException("PlaceholderAPI not initiated! Use PlaceholderAPIHook#initiate method.");
        }
        return new PlaceholderAPIWrapper();
    }

}
