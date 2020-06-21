package io.github.plugintemplate.bukkitjavagradle.file;

import io.github.portlek.configs.annotations.*;
import io.github.portlek.configs.bukkit.BukkitLinkedManaged;
import io.github.portlek.configs.bukkit.BukkitSection;
import io.github.portlek.configs.bukkit.util.ColorUtil;
import io.github.portlek.configs.replaceable.Replaceable;
import io.github.portlek.configs.replaceable.ReplaceableString;
import io.github.portlek.configs.util.MapEntry;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;

@LinkedConfig(value = {
    @LinkedFile(
        key = "en",
        config = @Config(
            value = "en_US",
            // TODO: Change the plugin data folder as you want.
            location = "%basedir%/BukkitJavaGradle/languages"
        )
    ),
    @LinkedFile(
        key = "tr",
        config = @Config(
            value = "tr_TR",
            // TODO: Change the plugin data folder as you want.
            location = "%basedir%/BukkitJavaGradle/languages"
        )
    ),
})
public final class LanguageFile extends BukkitLinkedManaged {

    @Instance
    public LanguageFile.Error errors = new Error();

    @Instance
    public LanguageFile.General generals = new General();

    @Property
    public ReplaceableString help_messages = this.match(s -> {
        if ("en".equals(s)) {
            return Optional.of(
                Replaceable.from(
                    new StringBuilder()
                        .append("&a====== %prefix% &a======")
                        .append('\n')
                        .append("&7/bukkitjavagradle &r> &eShows help message.")
                        .append('\n')
                        .append("&7/bukkitjavagradle help &r> &eShows help message.")
                        .append('\n')
                        .append("&7/bukkitjavagradle reload &r> &eReloads the plugin.")
                        .append('\n')
                        .append("&7/bukkitjavagradle version &r> &eChecks for update.")
                        .append('\n')
                        .append("&7/bukkitjavagradle message <player> <message> &r> &eSends the message to the player."))
                    .map(ColorUtil::colored)
                    .replace(this.getPrefix()));
        }
        if ("tr".equals(s)) {
            return Optional.of(
                Replaceable.from(
                    new StringBuilder()
                        .append("&a====== %prefix% &a======")
                        .append('\n')
                        .append("&7/bukkitjavagradle &r> &eYardım mesajını görüntüler.")
                        .append('\n')
                        .append("&7/bukkitjavagradle help &r> &eYardım mesajını görüntüler.")
                        .append('\n')
                        .append("&7/bukkitjavagradle reload &r> &eEklentiyi yeniden başlatır.")
                        .append('\n')
                        .append("&7/bukkitjavagradle version &r> &eGüncellemeleri kontrol eder.")
                        .append('\n')
                        .append("&7/bukkitjavagradle message <oyuncu> <mesaj> &r> &eMesajı oyuncuya gönderir."))
                    .map(ColorUtil::colored)
                    .replace(this.getPrefix()));
        }
        return Optional.empty();
    });

    public LanguageFile(@NotNull final ConfigFile configFile) {
        super(() -> configFile.plugin_language, MapEntry.from("config", configFile));
    }

    @NotNull
    public Map.Entry<String, Supplier<String>> getPrefix() {
        return MapEntry.from("%prefix%", () -> this.getConfig().plugin_prefix.build());
    }

    @NotNull
    private ConfigFile getConfig() {
        return (ConfigFile) this.object("config").orElseThrow(() ->
            new IllegalStateException("Config couldn't put into the objects!"));
    }

    @Section("error")
    public class Error extends BukkitSection {

        @Property
        public ReplaceableString player_not_found = LanguageFile.this.match(s -> {
            if ("en".equals(s)) {
                return Optional.of(
                    Replaceable.from("%prefix% &cPlayer not found! (%player%)")
                        .map(ColorUtil::colored)
                        .replaces("%player%")
                        .replace(LanguageFile.this.getPrefix()));
            }
            if ("tr".equals(s)) {
                return Optional.of(
                    Replaceable.from("%prefix% &cOyuncu bulunamadı! (%player%)")
                        .map(ColorUtil::colored)
                        .replaces("%player%")
                        .replace(LanguageFile.this.getPrefix()));
            }
            return Optional.empty();
        });

    }

    @Section("general")
    public class General extends BukkitSection {

        @Property
        public ReplaceableString reload_complete = LanguageFile.this.match(s -> {
            if ("en".equals(s)) {
                return Optional.of(
                    Replaceable.from("%prefix% &aReload complete! &7Took (%ms%ms)")
                        .map(ColorUtil::colored)
                        .replace(LanguageFile.this.getPrefix())
                        .replaces("%ms%")
                );
            }
            if ("tr".equals(s)) {
                return Optional.of(
                    Replaceable.from("%prefix% &aYeniden yükleme tamamlandı! &7Took (%ms%ms)")
                        .map(ColorUtil::colored)
                        .replace(LanguageFile.this.getPrefix())
                        .replaces("%ms%")
                );
            }
            return Optional.empty();
        });

        @Property
        public ReplaceableString new_version_found = LanguageFile.this.match(s -> {
            if ("en".equals(s)) {
                return Optional.of(
                    Replaceable.from("%prefix% &eNew version found (v%version%)")
                        .map(ColorUtil::colored)
                        .replaces("%version%")
                        .replace(LanguageFile.this.getPrefix())
                );
            }
            if ("tr".equals(s)) {
                return Optional.of(
                    Replaceable.from("%prefix% &eYeni sürüm bulundu (v%version%)")
                        .map(ColorUtil::colored)
                        .replaces("%version%")
                        .replace(LanguageFile.this.getPrefix())
                );
            }
            return Optional.empty();
        });

        @Property
        public ReplaceableString latest_version = LanguageFile.this.match(s -> {
            if ("en".equals(s)) {
                return Optional.of(
                    Replaceable.from("%prefix% &aYou're using the latest version (v%version%)")
                        .map(ColorUtil::colored)
                        .replaces("%version%")
                        .replace(LanguageFile.this.getPrefix())
                );
            }
            if ("tr".equals(s)) {
                return Optional.of(
                    Replaceable.from("%prefix% &aSon sürümü kullanıyorsunuz (v%version%)")
                        .map(ColorUtil::colored)
                        .replaces("%version%")
                        .replace(LanguageFile.this.getPrefix())
                );
            }
            return Optional.empty();
        });

    }

}
