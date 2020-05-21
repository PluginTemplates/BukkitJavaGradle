package io.github.plugintemplate.bukkitjavagradle.hooks;

import org.jetbrains.annotations.NotNull;

public interface Hook {

    boolean initiate();

    @NotNull
    Wrapped create();

}
