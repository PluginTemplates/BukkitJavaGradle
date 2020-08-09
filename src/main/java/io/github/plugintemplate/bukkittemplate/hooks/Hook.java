package io.github.plugintemplate.bukkittemplate.hooks;

import org.jetbrains.annotations.NotNull;

public interface Hook {

    boolean initiate();

    @NotNull
    Wrapped create();

}
