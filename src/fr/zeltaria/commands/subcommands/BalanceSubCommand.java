package fr.zeltaria.commands.subcommands;

import fr.zeltaria.commands.SubCommand;
import fr.zeltaria.main.Main;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class BalanceSubCommand extends SubCommand {

    private final Main main;

    public BalanceSubCommand(Main main) {this.main = main;}

    @Override
    public String getName() {
        return "balance";
    }

    @Override
    public String getDescription() {
        return "Voir combien on a de la crypto";
    }

    @Override
    public String getSyntax() {
        return "/crypto balance <crypto>";
    }

    @Override
    public String getPermissions() {
        return null;
    }

    @Override
    public void perform(Player player, String[] args) {
        if(args.length == 2) {
            List<String> all = main.getConfig().getStringList("cryptos");
            if (args[1].equalsIgnoreCase("all")) {
                final File file = new File(main.getDataFolder(), "balances.yml");
                if(!file.exists()) {
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                final YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
                for (String crypto : all) {
                    String[] abv = crypto.split("/");
                    String cle = player.getUniqueId()+"."+abv[0];
                    if(!configuration.contains(cle)){
                        configuration.set(cle,(double) 0);
                        try {
                            configuration.save(file);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    player.sendMessage(main.prefix+ ChatColor.GREEN+abv[0]+": "+ChatColor.GOLD+configuration.get(cle));
                }
            }
            else {
                int find = 0;
                for (String crypto : all) {
                    String[] abv = crypto.split("/");
                    if (args[1].equals(abv[0])) {
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
                        String cle = player.getUniqueId() + "." + abv[0];
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
                if (find == 0) {
                    player.sendMessage(main.prefix + ChatColor.RED + "La crypto " + ChatColor.BOLD + args[1] + ChatColor.RESET + ChatColor.RED + " n'existe pas ou n'est pas prise en charge par le plugin.");
                }
            }
        }
        else{
                player.sendMessage(main.prefix + ChatColor.RED + "Veuillez entrer la commande comme ceci: " + getSyntax());
            }
    }

    @Override
    public String getSecondName() {
        return null;
    }
}
