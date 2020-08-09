package io.github.plugintemplate.bukkittemplate.util;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public final class Placeholder {

    @NotNull
    private final String regex;

    @NotNull
    private final String replace;

    @NotNull
    public String replace(@NotNull final String text) {
        return text.replace(this.regex, this.replace);
    }

}
