package com.zeltaria.commands.adminsubcommands;

import com.zeltaria.commands.CommandManager;
import com.zeltaria.commands.SubCommand;
import fr.zeltaria.commands.CommandManager;
import fr.zeltaria.commands.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class HelpSubCommand extends SubCommand {
    @Override
    public String getName() {
        return "admin";
    }

    @Override
    public String getDescription() {
        return "Affiche la page d'aide des commandes admin";
    }

    @Override
    public String getSyntax() {
        return "/crypto admin help";
    }

    @Override
    public String getPermissions() {
        return "crypto.adminhelp";
    }

    @Override
    public void perform(Player player, String[] args) {
        player.sendMessage(ChatColor.GRAY+"Aide pour les commandes admins du plugin "+ChatColor.LIGHT_PURPLE+"Crypto:");
        for(SubCommand command : CommandManager.getSubcommands()){
            if(command.getName().equalsIgnoreCase("admin")){
                if(player.hasPermission(command.getPermissions())){
                    player.sendMessage(ChatColor.WHITE+command.getSyntax()+ ChatColor.GOLD+ " - "+ command.getDescription());
                }
            }
        }
    }

    @Override
    public String getSecondName() {
        return "help";
    }
}
