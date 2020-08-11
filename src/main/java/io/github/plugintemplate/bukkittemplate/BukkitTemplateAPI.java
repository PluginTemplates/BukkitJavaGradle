package io.github.plugintemplate.bukkittemplate;

import co.aikar.idb.DB;
import io.github.plugintemplate.bukkittemplate.file.ConfigFile;
import io.github.plugintemplate.bukkittemplate.file.LanguageFile;
import io.github.plugintemplate.bukkittemplate.util.FileElement;
import io.github.plugintemplate.bukkittemplate.util.ListenerBasic;
import io.github.plugintemplate.bukkittemplate.util.UpdateChecker;
import io.github.portlek.configs.CfgSection;
import io.github.portlek.smartinventory.SmartInventory;
import io.github.portlek.smartinventory.manager.BasicSmartInventory;
import org.bukkit.command.CommandSender;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

// TODO Change the class name as you want.
public final class BukkitTemplateAPI {

    @NotNull
    public final SmartInventory inventoryManager;

    @NotNull
    public final BukkitTemplate bukkitTemplate;

    @NotNull
    public final ConfigFile configFile = new ConfigFile();

    @NotNull
    public final LanguageFile languageFile = new LanguageFile(this.configFile);

    public BukkitTemplateAPI(@NotNull final BukkitTemplate bukkitTemplate) {
        this.inventoryManager = new BasicSmartInventory(bukkitTemplate);
        this.bukkitTemplate = bukkitTemplate;
        CfgSection.addProvidedClass(FileElement.class, new FileElement.Provider());
    }

    public void reloadPlugin(final boolean first) {
        this.languageFile.load();
        this.configFile.load();
        this.configFile.createSQL();

        if (first) {
            this.inventoryManager.init();
            new ListenerBasic<>(
                PlayerJoinEvent.class,
                event -> event.getPlayer().hasPermission("bukkittemplate.version"),
                event -> this.checkForUpdate(event.getPlayer())
            ).register(this.bukkitTemplate);
            // TODO: Listeners should be here.
        }

        this.bukkitTemplate.getServer().getScheduler().cancelTasks(this.bukkitTemplate);

        if (this.configFile.saving.auto_save) {
            this.bukkitTemplate.getServer().getScheduler().runTaskTimer(
                this.bukkitTemplate,
                () -> {
                    // TODO Add codes for saving data as automatic
                },
                this.configFile.saving.auto_save_time * 20L,
                this.configFile.saving.auto_save_time * 20L
            );
        }

        this.checkForUpdate(this.bukkitTemplate.getServer().getConsoleSender());
    }

    public void checkForUpdate(@NotNull final CommandSender sender) {
        if (!this.configFile.check_for_update) {
            return;
        }
        // TODO Change the UpdateChecker resource id as you want.
        final UpdateChecker updater = new UpdateChecker(this.bukkitTemplate, 11111);

        try {
            if (updater.checkForUpdates()) {
                sender.sendMessage(this.languageFile.generals.new_version_found.get()
                    .build("%version%", updater::getLatestVersion));
            } else {
                sender.sendMessage(this.languageFile.generals.latest_version.get()
                    .build("%version%", updater::getLatestVersion));
            }
        } catch (final Exception exception) {
            this.bukkitTemplate.getLogger().warning("Update checker failed, could not connect to the API.");
        }
    }

    public void disablePlugin() {
        if (this.configFile.saving.save_when_plugin_disable) {
            // TODO Add codes for saving data
        }
        DB.close();
    }

}
