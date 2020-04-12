package io.github.plugintemplate.bukkitjavagradle.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import io.github.plugintemplate.bukkitjavagradle.BukkitJavaGradleAPI;
import io.github.portlek.bukkititembuilder.util.ColorUtil;
import io.github.portlek.configs.util.ListToString;
import java.util.Optional;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

// TODO Change class and command name as you want.
@CommandAlias("bukkitjavagradle|bjg")
public final class BukkitJavaGradleCommand extends BaseCommand {

    @NotNull
    private final BukkitJavaGradleAPI api;

    public BukkitJavaGradleCommand(@NotNull final BukkitJavaGradleAPI api) {
        this.api = api;
    }

    @Default
    // TODO Change the permission as you want.
    @CommandPermission("bukkitjavagradle.command.main")
    public void defaultCommand(final CommandSender sender) {
        sender.sendMessage(
            (String) this.api.languageFile.help_messages.buildMap(list ->
                new ListToString(list).value()
            )
        );
    }

    @Subcommand("help")
    // TODO Change the permission as you want.
    @CommandPermission("bukkitjavagradle.command.help")
    public void helpCommand(final CommandSender sender) {
        sender.sendMessage(
            (String) this.api.languageFile.help_messages.buildMap(list ->
                new ListToString(list).value()
            )
        );
    }

    @Subcommand("reload")
    // TODO Change the permission as you want.
    @CommandPermission("bukkitjavagradle.command.reload")
    public void reloadCommand(final CommandSender sender) {
        final long ms = System.currentTimeMillis();

        this.api.reloadPlugin(false);
        sender.sendMessage(
            this.api.languageFile.generals.reload_complete.build(
                "%ms%", () -> String.valueOf(System.currentTimeMillis() - ms)
            )
        );
    }

    @Subcommand("version")
    // TODO Change the permission as you want.
    @CommandPermission("bukkitjavagradle.command.version")
    public void versionCommand(final CommandSender sender) {
        this.api.checkForUpdate(sender);
    }

    @Subcommand("message")
    // TODO Change the permission as you want.
    @CommandPermission("bukkitjavagradle.command.message")
    @CommandCompletion("@players <message>")
    public void messageCommand(final CommandSender sender, @Conditions("player:arg=0") final String[] args) {
        if (args.length < 1) {
            return;
        }
        final StringBuilder builder = new StringBuilder();
        for (int index = 1; index < args.length; index++) {
            builder.append(ColorUtil.colored(args[index]));
        }
        // player cannot be null cause @Conditions("player:arg=0") this condition checks
        // if args[0] is in the server.
        Optional.ofNullable(Bukkit.getPlayer(args[0])).ifPresent(player ->
            player.sendMessage(builder.toString()));
    }

}
