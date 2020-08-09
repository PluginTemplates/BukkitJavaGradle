package io.github.plugintemplate.bukkittemplate.hooks;

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
