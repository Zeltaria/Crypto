package com.zeltaria.commands.subcommands;

import com.zeltaria.commands.SubCommand;
import com.zeltaria.crypto.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class InfoSubCommand extends SubCommand {

    private final Main main;

    public InfoSubCommand(Main main){this.main = main;}

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String getDescription() {
        return "Voir les informations d'une crypto";
    }

    @Override
    public String getSyntax() {
        return "/crypto info"+ChatColor.YELLOW+" <crypto>";
    }

    @Override
    public String getPermissions() {
        return null;
    }

    @Override
    public void perform(Player player, String[] args) {
        if(args.length == 2) {
            List<String> all = main.getConfig().getStringList("cryptos");
            int find = 0;
            for (String crypto : all) {
                String[] abv = crypto.split("/");
                if (args[1].equals(abv[0])) {
                    find = 1;
                    player.sendMessage(main.prefix + ChatColor.GREEN + "Nom de la crypto: " + ChatColor.BLUE + abv[1] + ChatColor.GREEN + "  Abrégé: " + ChatColor.BLUE + abv[0] + "\n" + ChatColor.GREEN + "            Prix: " + ChatColor.BLUE + main.getConfig().getDouble("amount."+abv[0]) + "€");
                    break;
                }
            }
            if (find == 0) {
                player.sendMessage(main.prefix + ChatColor.RED + "La crypto " + ChatColor.BOLD + args[1] + ChatColor.RESET + ChatColor.RED + " n'existe pas ou n'est pas prise en charge par le plugin.");
            }
        }
        else{
            player.sendMessage(main.prefix+ChatColor.RED+"Veuillez entrer la commande comme ceci: "+getSyntax());
        }
    }

    @Override
    public String getSecondName() {
        return null;
    }
}
