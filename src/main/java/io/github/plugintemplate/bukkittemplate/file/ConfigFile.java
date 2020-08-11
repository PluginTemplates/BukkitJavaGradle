package io.github.plugintemplate.bukkittemplate.file;

import co.aikar.idb.DB;
import co.aikar.idb.DatabaseOptions;
import co.aikar.idb.HikariPooledDatabase;
import co.aikar.idb.PooledDatabaseOptions;
import io.github.plugintemplate.bukkittemplate.hooks.*;
import io.github.portlek.configs.annotations.Config;
import io.github.portlek.configs.annotations.Instance;
import io.github.portlek.configs.annotations.Property;
import io.github.portlek.configs.annotations.Section;
import io.github.portlek.configs.bukkit.BukkitManaged;
import io.github.portlek.configs.bukkit.BukkitSection;
import io.github.portlek.configs.bukkit.util.ColorUtil;
import io.github.portlek.configs.type.YamlFileType;
import io.github.portlek.replaceable.Replaceable;
import io.github.portlek.replaceable.rp.RpString;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

@Config(
    name = "config",
    type = YamlFileType.class,
    // TODO: Change the plugin data folder as you want.
    location = "%basedir%/BukkitTemplate"
)
public final class ConfigFile extends BukkitManaged {

    // Hook Paths
    private static final String PLACEHOLDER_API = "PlaceholderAPI";

    private static final String LUCK_PERMS = "LuckPerms";

    private static final String VAULT = "Vault";

    // Instances
    @Instance
    public final ConfigFile.Saving saving = new ConfigFile.Saving();

    @Instance
    public final ConfigFile.Hooks hooks = new ConfigFile.Hooks();

    // Wrapped map.
    private final Map<String, Wrapped> wrapped = new HashMap<>();

    // TODO: Change the plugin prefix as you want.
    @Property
    public RpString plugin_prefix = Replaceable.from("&6[&eBukkitTemplate&6]")
        .map(ColorUtil::colored);

    @Property
    public String plugin_language = "en";

    @Property
    public boolean check_for_update = true;

    @Override
    public void onLoad() {
        this.loadWrapped();
        this.setAutoSave(true);
    }

    @NotNull
    public void createSQL() {
        final PooledDatabaseOptions poolOptions;
        if (this.isMySQL()) {
            poolOptions = PooledDatabaseOptions
                .builder()
                .options(DatabaseOptions
                    .builder()
                    .poolName("BukkitTemplate DB")
                    .logger(Logger.getLogger("BukkitTemplate"))
                    .mysql(this.saving.mysql.username, this.saving.mysql.password, this.saving.mysql.database,
                        this.saving.mysql.host + ':' + this.saving.mysql.port)
                    .useOptimizations(true)
                    .build())
                .build();
        } else {
            poolOptions = PooledDatabaseOptions
                .builder()
                .options(DatabaseOptions
                    .builder()
                    .poolName("BukkitTemplate DB")
                    .logger(Logger.getLogger("BukkitTemplate"))
                    .sqlite("plugins/BukkitTemplate/bukkitTemplate.db")
                    .useOptimizations(true)
                    .build())
                .build();
        }
        DB.setGlobalDatabase(new HikariPooledDatabase(poolOptions));
    }

    @NotNull
    @SuppressWarnings("unchecked")
    public <T extends Wrapped> Optional<T> getWrapped(@NotNull final String wrappedId) {
        return Optional.ofNullable((T) this.wrapped.get(wrappedId));
    }

    private boolean isMySQL() {
        return "mysql".equalsIgnoreCase(this.saving.storage_type) ||
            "remote".equalsIgnoreCase(this.saving.storage_type) ||
            "net".equalsIgnoreCase(this.saving.storage_type);
    }

    private void loadWrapped() {
        this.hookLittle(this.hooks.LuckPerms, new LuckPermsHook(), ConfigFile.LUCK_PERMS,
            () -> {
                this.hooks.LuckPerms = true;
                this.hooks.set(ConfigFile.LUCK_PERMS, true);
            },
            () -> {
                this.hooks.LuckPerms = false;
                this.hooks.set(ConfigFile.LUCK_PERMS, false);
            });
        this.hookLittle(this.hooks.PlaceholderAPI, new PlaceholderAPIHook(), ConfigFile.PLACEHOLDER_API,
            () -> {
                this.hooks.PlaceholderAPI = true;
                this.hooks.set(ConfigFile.PLACEHOLDER_API, true);
            },
            () -> {
                this.hooks.PlaceholderAPI = false;
                this.hooks.set(ConfigFile.PLACEHOLDER_API, false);
            });
        this.hookLittle(this.hooks.Vault, new VaultHook(), ConfigFile.VAULT,
            () -> {
                this.hooks.Vault = true;
                this.hooks.set(ConfigFile.VAULT, true);
            },
            () -> {
                this.hooks.Vault = false;
                this.hooks.set(ConfigFile.VAULT, true);
            });
    }

    private void hookLittle(final boolean force, @NotNull final Hook hook, @NotNull final String path,
                            @NotNull final Runnable succeed, @NotNull final Runnable failed) {
        this.hookLittle(force, hook, path, () -> true, succeed, failed);
    }

    private void hookLittle(final boolean force, @NotNull final Hook hook, @NotNull final String path,
                            @NotNull final BooleanSupplier supplier, @NotNull final Runnable succeed,
                            @NotNull final Runnable failed) {
        if ((this.hooks.auto_detect || force) && hook.initiate() && supplier.getAsBoolean()) {
            this.wrapped.put(path, hook.create());
            this.sendHookNotify(path);
            this.hooks.set(path, true);
            succeed.run();
        } else {
            this.hooks.set(path, false);
            failed.run();
        }
    }

    private void sendHookNotify(@NotNull final String path) {
        Bukkit.getConsoleSender().sendMessage(
            // TODO Change the message as you want.
            ColorUtil.colored(
                this.plugin_prefix.build() + " &r>> &a" + path + " is hooking"));
    }

    @Section("saving")
    public static final class Saving extends BukkitSection {

        @Instance
        public final ConfigFile.Saving.MySQL mysql = new ConfigFile.Saving.MySQL();

        @Property
        public String storage_type = "sqlite";

        @Property
        public boolean save_when_plugin_disable = true;

        @Property
        public boolean auto_save = true;

        @Property
        public long auto_save_time = 60L;

        @Section("mysql")
        public static final class MySQL extends BukkitSection {

            @Property
            public String host = "localhost";

            @Property
            public int port = 3306;

            @Property
            public String database = "database";

            @Property
            public String username = "username";

            @Property
            public String password = "password";

        }

    }

    @Section("hooks")
    public static final class Hooks extends BukkitSection {

        @Property
        public boolean auto_detect = true;

        @Property
        public boolean PlaceholderAPI = false;

        @Property
        public boolean LuckPerms = false;

        @Property
        public boolean Vault = false;

    }

}
