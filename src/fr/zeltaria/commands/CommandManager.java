package fr.zeltaria.commands;

import fr.zeltaria.commands.adminsubcommands.AddSubCommand;
import fr.zeltaria.commands.adminsubcommands.RemoveSubCommand;
import fr.zeltaria.commands.adminsubcommands.ResetSubCommand;
import fr.zeltaria.commands.adminsubcommands.SetSubCommand;
import fr.zeltaria.commands.subcommands.*;
import fr.zeltaria.main.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandManager implements TabExecutor {

    private static ArrayList<SubCommand> subcommands = new ArrayList<>();
    private final Main main;

    public CommandManager(Main main) {
        this.main = main;
        subcommands.add(new HelpSubCommand());
        subcommands.add(new AddSubCommand(main));
        subcommands.add(new BuySubCommand(main));
        subcommands.add(new SellSubCommand(main));
        subcommands.add(new InfoSubCommand(main));
        subcommands.add(new BalanceSubCommand(main));
        subcommands.add(new ListSubCommand(main));
        subcommands.add(new RemoveSubCommand(main));
        subcommands.add(new ResetSubCommand(main));
        subcommands.add(new SetSubCommand(main));
    }

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
                            p.sendMessage(ChatColor.RED+"Veuillez choisir une commande");
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
                HelpSubCommand help = new HelpSubCommand();
                help.perform(p,args);
            }
        }

        return false;
    }

    public static ArrayList<SubCommand> getSubcommands(){
        return subcommands;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> result = new ArrayList<>();
        if(args.length == 1){
            for(SubCommand c : getSubcommands()) {
                if (c.getPermissions() != null) {
                    if (sender.hasPermission(c.getPermissions())) {
                        if(!result.contains(c.getName())) {
                            result.add(c.getName());
                        }
                    }
                }
                else{
                    result.add(c.getName());
                }
            }
            return result;
        }
        /*else{
            if(args.length == 2 && (args[0] == "add" || args[0] == "remove" || args[0] == "set" || args[0] == "reset")) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    result.add(p.getName());
                }
                return result;
            }
            else{
                if(args.length == 2 && (args[0] == "buy" || args[0] == "sell" || args[0] == "info" || args[0] == "balance")){
                    List<String> all = main.getConfig().getStringList("cryptos");
                    for (String crypto : all) {
                        String[] abv = crypto.split("/");
                        result.add(abv[0]);
                    }
                    if(args[0] == "balance"){
                        result.add("all");
                    }
                    return result;
                }
            }
        }*/
        return Collections.singletonList("");
    }
}
