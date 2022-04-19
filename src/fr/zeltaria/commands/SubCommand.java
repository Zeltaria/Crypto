package fr.zeltaria.commands;

import org.bukkit.entity.Player;

public abstract class SubCommand {

    public abstract String getName();

    public abstract String getDescription();

    public abstract String getSyntax();

    public abstract String getPermissions();

    public abstract void perform(Player player, String[] args);

    public abstract String getSecondName();
}
