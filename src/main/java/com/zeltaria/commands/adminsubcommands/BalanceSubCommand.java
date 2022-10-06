package com.zeltaria.commands.adminsubcommands;

import com.zeltaria.commands.SubCommand;
import com.zeltaria.crypto.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class BalanceSubCommand extends SubCommand {

    private final Main main;

    public BalanceSubCommand(Main main){this.main = main;}

    @Override
    public String getName() {
        return "admin";
    }

    @Override
    public String getDescription() {
        return "Voir la balance de la crypto ou des cryptos du joueur désigné";
    }

    @Override
    public String getSyntax() {
        return "/crypto admin balance "+ChatColor.YELLOW+"<joueur> <crypto>";
    }

    @Override
    public String getPermissions() {
        return "crypto.otherbalance";
    }

    @Override
    public void perform(Player player, String[] args) {
        if(args.length == 4) {
            List<String> all = main.getConfig().getStringList("cryptos");
            Player target = Bukkit.getPlayerExact(args[2]);
            if (args[3].equalsIgnoreCase("all")) {
                player.sendMessage(main.prefix + ChatColor.GRAY + "Balance des cryptos de " + ChatColor.LIGHT_PURPLE + args[2]);
                final File file = new File(main.getDataFolder(), "balances.yml");
                if (!file.exists()) {
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                final YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
                for (String crypto : all) {
                    String[] abv = crypto.split("/");
                    String cle = target.getUniqueId() + "." + abv[0];
                    if (!configuration.contains(cle)) {
                        configuration.set(cle, (double) 0);
                        try {
                            configuration.save(file);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    player.sendMessage(main.prefix + ChatColor.GREEN + abv[0] + ": " + ChatColor.GOLD + configuration.get(cle));
                }
            }
            else{
                int find = 0;
                for (String crypto : all) {
                    String[] abv = crypto.split("/");
                    if (args[3].equals(abv[0])) {
                        find = 1;
                        final File file = new File(main.getDataFolder(), "balances.yml");
                        if (!file.exists()) {
                            try {
                                file.createNewFile();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        final YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
                        String cle = target.getUniqueId() + "." + abv[0];
                        if (!configuration.contains(cle)) {
                            configuration.set(cle, (double) 0);
                            try {
                                configuration.save(file);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        player.sendMessage(main.prefix + ChatColor.GOLD + " " + args[2] + " " + ChatColor.GREEN + abv[0] + ": " + ChatColor.GOLD + configuration.get(cle));
                    }
                }
                if (find == 0) {
                    player.sendMessage(main.prefix + ChatColor.RED + "La crypto " + ChatColor.BOLD + args[3] + ChatColor.RESET + ChatColor.RED + " n'existe pas ou n'est pas prise en charge par le plugin.");
                }
            }
        }
    }

    @Override
    public String getSecondName() {
        return "balance";
    }
}
