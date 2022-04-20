package fr.zeltaria.commands.subcommands;

import fr.zeltaria.commands.CommandManager;
import fr.zeltaria.commands.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class HelpSubCommand extends SubCommand {
    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "Affiche l'aide pour les commandes du plugin";
    }

    @Override
    public String getSyntax() {
        return "/crypto help";
    }

    @Override
    public String getPermissions() {
        return null;
    }

    @Override
    public void perform(Player player, String[] args) {
        player.sendMessage(ChatColor.GRAY+"Aide pour les commandes du plugin "+ChatColor.LIGHT_PURPLE+"Crypto:");
        for(SubCommand command : CommandManager.getSubcommands()){
            if(command.getPermissions() != null){
                if(player.hasPermission(command.getPermissions())){
                    if(!command.getName().equalsIgnoreCase("admin")){
                        player.sendMessage(ChatColor.WHITE+command.getSyntax()+ ChatColor.GOLD+ " - "+ command.getDescription());
                    }
                }
            }
            else{
                player.sendMessage(ChatColor.WHITE+command.getSyntax()+ ChatColor.GOLD+ " - "+ command.getDescription());
            }
        }
        if(player.hasPermission("crypto.admin")){
            player.sendMessage(ChatColor.WHITE+"/crypto admin"+ ChatColor.GOLD+ " - Affiche la page d'aide des commandes admins");
        }
    }

    @Override
    public String getSecondName() {
        return null;
    }
}
