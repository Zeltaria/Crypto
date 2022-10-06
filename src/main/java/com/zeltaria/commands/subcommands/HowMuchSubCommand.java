package com.zeltaria.commands.subcommands;

import com.zeltaria.commands.SubCommand;
import com.zeltaria.crypto.Main;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class HowMuchSubCommand extends SubCommand {

    private Main main;

    public HowMuchSubCommand(Main main) {
        this.main = main;
    }

    @Override
    public String getName() {
        return "howmuch";
    }

    @Override
    public String getDescription() {
        return "retourne le nombre de la crypto que tu possède et le montant total qu'elles font";
    }

    @Override
    public String getSyntax() {
        return "/crypto howMuch"+ChatColor.YELLOW+" <crypto>";
    }

    @Override
    public String getPermissions() {
        return null;
    }

    @Override
    public void perform(Player player, String[] args) {
        if(args.length == 2) {
            List<String> all = main.getConfig().getStringList("cryptos");
            if(args[1].equalsIgnoreCase("all")){
                for (String crypto : all) {
                    String[] abv = crypto.split("/");
                    final File file = new File(main.getDataFolder(), "balances.yml");
                    main.testFile(file);
                    final YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
                    String cle = player.getUniqueId()+"."+abv[0];
                    if(!configuration.contains(cle)){
                        configuration.set(cle,(double) 0);
                        try {
                            configuration.save(file);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    player.sendMessage(main.prefix + ChatColor.GREEN +  abv[0]+ ChatColor.BLUE+ configuration.getDouble(cle) +" Valeur: " + ChatColor.BLUE + (main.getConfig().getDouble("amount."+abv[0])*configuration.getDouble(cle)) + "€");
                    break;
                }
            }
            else {
                int find = 0;
                for (String crypto : all) {
                    String[] abv = crypto.split("/");
                    if (args[1].equals(abv[0])) {
                        find = 1;
                        final File file = new File(main.getDataFolder(), "balances.yml");
                        main.testFile(file);
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
                        player.sendMessage(main.prefix + ChatColor.GREEN + abv[0] + ChatColor.BLUE+ configuration.getDouble(cle) + " Valeur: " + ChatColor.BLUE + (main.getConfig().getDouble("amount." + abv[0]) * configuration.getDouble(cle)) + "€");
                        break;
                    }
                }
                if (find == 0) {
                    player.sendMessage(main.prefix + ChatColor.RED + "La crypto " + ChatColor.BOLD + args[1] + ChatColor.RESET + ChatColor.RED + " n'existe pas ou n'est pas prise en charge par le plugin.");
                }
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
