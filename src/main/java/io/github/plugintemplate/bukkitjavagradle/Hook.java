package io.github.plugintemplate.bukkitjavagradle;

import org.jetbrains.annotations.NotNull;

public interface Hook {

    boolean initiate();

    @NotNull
    Wrapped create();

}
