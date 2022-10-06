package com.zeltaria.commands.subcommands;

import com.zeltaria.commands.SubCommand;
import com.zeltaria.crypto.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

public class ListSubCommand extends SubCommand {

    private final Main main;

    public ListSubCommand(Main main){
        this.main = main;
    }

    @Override
    public String getName() {
        return "list";
    }

    @Override
    public String getDescription() {
        return "Montre toutes les cryptos prises en charge ainsi que le montant pour 1 crypto";
    }

    @Override
    public String getSyntax() {
        return "/crypto list";
    }

    @Override
    public String getPermissions() {
        return null;
    }

    @Override
    public void perform(Player player, String[] args) {
        List<String> all = main.getConfig().getStringList("cryptos");
        for (String crypto : all) {
            String[] abv = crypto.split("/");
            String[] crypt = crypto.split("/");
            player.sendMessage(main.prefix+" "+ ChatColor.GREEN+crypt[0]+": "+ChatColor.GOLD+main.getConfig().getDouble("amount."+abv[0])+"€");
        }
    }

    @Override
    public String getSecondName() {
        return null;
    }
}
