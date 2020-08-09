package io.github.plugintemplate.bukkittemplate.file;

import io.github.portlek.configs.annotations.*;
import io.github.portlek.configs.bukkit.BukkitLinkedManaged;
import io.github.portlek.configs.bukkit.BukkitSection;
import io.github.portlek.configs.bukkit.util.ColorUtil;
import io.github.portlek.configs.type.YamlFileType;
import io.github.portlek.configs.util.Scalar;
import io.github.portlek.mapentry.MapEntry;
import io.github.portlek.replaceable.Replaceable;
import io.github.portlek.replaceable.rp.RpString;
import java.util.Map;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;

@LinkedConfig({
    @LinkedFile(
        key = "en",
        config = @Config(
            name = "en_US",
            type = YamlFileType.class,
            // TODO: Change the plugin data folder as you want.
            location = "%basedir%/BukkitTemplate/languages"
        )
    ),
    @LinkedFile(
        key = "tr",
        config = @Config(
            name = "tr_TR",
            type = YamlFileType.class,
            // TODO: Change the plugin data folder as you want.
            location = "%basedir%/BukkitTemplate/languages"
        )
    ),
})
public final class LanguageFile extends BukkitLinkedManaged {

    @Instance
    public LanguageFile.Error errors = new Error();

    @Instance
    public LanguageFile.General generals = new General();

    @Property
    public Scalar<RpString> help_messages = this.match(m -> {
        m.put("en", Replaceable.from(
            new StringBuilder()
                .append("&a====== %prefix% &a======")
                .append('\n')
                .append("&7/bukkittemplate &r> &eShows help message.")
                .append('\n')
                .append("&7/bukkittemplate help &r> &eShows help message.")
                .append('\n')
                .append("&7/bukkittemplate reload &r> &eReloads the plugin.")
                .append('\n')
                .append("&7/bukkittemplate version &r> &eChecks for update.")
                .append('\n')
                .append("&7/bukkittemplate message <player> <message> &r> &eSends the message to the player."))
            .map(ColorUtil::colored)
            .replace(this.getPrefix()));
        m.put("tr", Replaceable.from(
            new StringBuilder()
                .append("&a====== %prefix% &a======")
                .append('\n')
                .append("&7/bukkittemplate &r> &eYardım mesajını görüntüler.")
                .append('\n')
                .append("&7/bukkittemplate help &r> &eYardım mesajını görüntüler.")
                .append('\n')
                .append("&7/bukkittemplate reload &r> &eEklentiyi yeniden başlatır.")
                .append('\n')
                .append("&7/bukkittemplate version &r> &eGüncellemeleri kontrol eder.")
                .append('\n')
                .append("&7/bukkittemplate message <oyuncu> <mesaj> &r> &eMesajı oyuncuya gönderir."))
            .map(ColorUtil::colored)
            .replace(this.getPrefix()));
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
    public final class Error extends BukkitSection {

        @Property
        public Scalar<RpString> player_not_found = LanguageFile.this.match(m -> {
            m.put("en", Replaceable.from("%prefix% &cPlayer not found! (%player%)")
                .map(ColorUtil::colored)
                .replaces("%player%")
                .replace(LanguageFile.this.getPrefix()));
            m.put("tr", Replaceable.from("%prefix% &cOyuncu bulunamadı! (%player%)")
                .map(ColorUtil::colored)
                .replaces("%player%")
                .replace(LanguageFile.this.getPrefix()));
        });

    }

    @Section("general")
    public final class General extends BukkitSection {

        @Property
        public Scalar<RpString> reload_complete = LanguageFile.this.match(m -> {
            m.put("en", Replaceable.from("%prefix% &aReload complete! &7Took (%ms%ms)")
                .map(ColorUtil::colored)
                .replace(LanguageFile.this.getPrefix())
                .replaces("%ms%"));
            m.put("tr", Replaceable.from("%prefix% &aYeniden yükleme tamamlandı! &7Took (%ms%ms)")
                .map(ColorUtil::colored)
                .replace(LanguageFile.this.getPrefix())
                .replaces("%ms%"));
        });

        @Property
        public Scalar<RpString> new_version_found = LanguageFile.this.match(m -> {
            m.put("en", Replaceable.from("%prefix% &eNew version found (v%version%)")
                .map(ColorUtil::colored)
                .replaces("%version%")
                .replace(LanguageFile.this.getPrefix()));
            m.put("tr", Replaceable.from("%prefix% &eYeni sürüm bulundu (v%version%)")
                .map(ColorUtil::colored)
                .replaces("%version%")
                .replace(LanguageFile.this.getPrefix()));
        });

        @Property
        public Scalar<RpString> latest_version = LanguageFile.this.match(m -> {
            m.put("en", Replaceable.from("%prefix% &aYou're using the latest version (v%version%)")
                .map(ColorUtil::colored)
                .replaces("%version%")
                .replace(LanguageFile.this.getPrefix()));
            m.put("tr", Replaceable.from("%prefix% &aSon sürümü kullanıyorsunuz (v%version%)")
                .map(ColorUtil::colored)
                .replaces("%version%")
                .replace(LanguageFile.this.getPrefix())
            );
        });

    }

}
