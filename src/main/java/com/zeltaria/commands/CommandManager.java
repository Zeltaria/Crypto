package com.zeltaria.commands;

import com.zeltaria.crypto.Main;
import com.zeltaria.commands.adminsubcommands.AddSubCommand;
import com.zeltaria.commands.adminsubcommands.RemoveSubCommand;
import com.zeltaria.commands.adminsubcommands.ResetSubCommand;
import com.zeltaria.commands.adminsubcommands.SetSubCommand;
import com.zeltaria.commands.subcommands.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandManager implements TabExecutor {

    private static final ArrayList<SubCommand> subcommands = new ArrayList<>();
    private final Main main;

    // It's adding all the subcommands to the subcommands list.
    public CommandManager(Main main) {
        this.main = main;
        subcommands.add(new com.zeltaria.commands.subcommands.HelpSubCommand());
        subcommands.add(new BuySubCommand(main));
        subcommands.add(new SellSubCommand(main));
        subcommands.add(new InfoSubCommand(main));
        subcommands.add(new com.zeltaria.commands.subcommands.BalanceSubCommand(main));
        subcommands.add(new ListSubCommand(main));
        subcommands.add(new com.zeltaria.commands.adminsubcommands.HelpSubCommand());
        subcommands.add(new AddSubCommand(main));
        subcommands.add(new RemoveSubCommand(main));
        subcommands.add(new ResetSubCommand(main));
        subcommands.add(new SetSubCommand(main));
        subcommands.add(new com.zeltaria.commands.adminsubcommands.BalanceSubCommand(main));
        subcommands.add(new HowMuchSubCommand(main));
    }

    /**
     * If the sender is a player, if the command has arguments, if the first argument is "admin" and the player has the
     * permission "crypto.admin", then it will check if the second argument is a subcommand and if it is, it will perform
     * it. If the first argument is not "admin", it will check if the first argument is a subcommand and if it is, it will
     * perform it. If the command has no arguments, it will perform the help subcommand
     *
     * @param sender The player who sent the command
     * @param command The command that was executed
     * @param label The name of the command that was used.
     * @param args The arguments of the command.
     * @return The return type of the method.
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player p) {
            if(args.length > 0){
                if(args[0].equalsIgnoreCase("admin")){
                    if(p.hasPermission("crypto.admin")) {
                        if(!(args.length == 1)){
                            for (int i = 0; i < getSubcommands().size(); i++) {
                                if (getSubcommands().get(i).getName().equalsIgnoreCase("admin")) {
                                    if (args[1].equalsIgnoreCase(getSubcommands().get(i).getSecondName())) {
                                        getSubcommands().get(i).perform(p, args);
                                    }
                                }
                            }
                        }
                        else{
                            com.zeltaria.commands.adminsubcommands.HelpSubCommand help = new com.zeltaria.commands.adminsubcommands.HelpSubCommand();
                            help.perform(p, args);
                        }
                    }
                    else{
                        p.sendMessage(ChatColor.RED+"Vous n'avez pas la permission d'utiliser cette commande!");
                    }
                }
                else{
                    for (int i = 0; i < getSubcommands().size(); i++) {
                        if (args[0].equalsIgnoreCase(getSubcommands().get(i).getName())) {
                            getSubcommands().get(i).perform(p, args);
                        }
                    }
                }
            }
            else{
                com.zeltaria.commands.subcommands.HelpSubCommand help = new com.zeltaria.commands.subcommands.HelpSubCommand();
                help.perform(p,args);
            }
        }
        return false;
    }

    public static ArrayList<SubCommand> getSubcommands(){
        return subcommands;
    }

    /**
     * If the player has typed in one argument, then return a list of all the subcommands that the player has permission to
     * use. If the player has typed in two arguments, then return a list of all the cryptocurrencies that the player can
     * use. If the player has typed in three arguments, then return a list of all the players that the player can use. If
     * the player has typed in four arguments, then return a list of all the cryptocurrencies that the player can use
     *
     * @param sender The person who sent the command.
     * @param command The command that was executed
     * @param label The command label
     * @param args The arguments that were passed to the command.
     * @return A list of strings
     */
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> result = new ArrayList<>();
        List<String> all = main.getConfig().getStringList("cryptos");
        switch(args.length) {
            case 1:
                for (SubCommand c : getSubcommands()) {
                    if (c.getPermissions() != null) {
                        if (sender.hasPermission(c.getPermissions())) {
                            if (!result.contains(c.getName())) {
                                if (c.getName().startsWith(args[0])) {
                                    result.add(c.getName());
                                }
                            }
                        }
                    } else {
                        result.add(c.getName());
                    }
                }
                return result;
            case 2:
                if(args[0].equalsIgnoreCase("admin")){
                    if(sender.hasPermission("crypto.admin")){
                        for(SubCommand c : getSubcommands()) {
                            if (c.getPermissions() != null) {
                                if (sender.hasPermission(c.getPermissions())) {
                                    if(!result.contains(c.getSecondName())) {
                                        result.add(c.getSecondName());
                                    }
                                }
                            }
                        }
                        return result;
                    }
                }
                else{
                    if(args[0].equalsIgnoreCase("balance") || args[0].equalsIgnoreCase("sell") || args[0].equalsIgnoreCase("buy")
                            || args[0].equalsIgnoreCase("info")) {
                        for (String crypto : all) {
                            String[] abv = crypto.split("/");
                            result.add(abv[0]);
                        }
                        if(args[0].equalsIgnoreCase("balance")){
                            result.add("all");
                        }
                        return result;
                    }
                }
            case 3:
                if(args[0].equalsIgnoreCase("admin")){
                    if(sender.hasPermission("crypto.admin")){
                        if(args[1].equalsIgnoreCase("set") || args[1].equalsIgnoreCase("reset") || args[1].equalsIgnoreCase("remove")
                                || args[1].equalsIgnoreCase("add") || args[1].equalsIgnoreCase("balance")){
                            for(Player p : Bukkit.getOnlinePlayers()){
                                result.add(p.getName());
                            }
                            return result;
                        }
                    }
                }
            case 4:
                if(args[0].equalsIgnoreCase("admin")) {
                    if (sender.hasPermission("crypto.admin")) {
                        if(args[1].equalsIgnoreCase("balance") || args[1].equalsIgnoreCase("set") || args[1].equalsIgnoreCase("remove")
                                || args[1].equalsIgnoreCase("add")){
                            for (String crypto : all) {
                                String[] abv = crypto.split("/");
                                result.add(abv[0]);
                            }
                            if(args[1].equalsIgnoreCase("balance")){
                                result.add("all");
                            }
                            return result;
                        }
                    }
                }
                default:
                    return Collections.singletonList("");
        }
    }
}
