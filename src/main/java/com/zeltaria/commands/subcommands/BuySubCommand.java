package com.zeltaria.commands.subcommands;

import com.zeltaria.commands.SubCommand;
import com.zeltaria.crypto.Main;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static java.lang.Double.parseDouble;

public class BuySubCommand extends SubCommand {

    private final Main main;

    public BuySubCommand(Main main){this.main = main;}

    @Override
    public String getName() {
        return "buy";
    }

    @Override
    public String getDescription() {
        return "Acheter de la crypto";
    }

    @Override
    public String getSyntax() {
        return "/crypto buy"+ChatColor.YELLOW+" <crypto> <montant>";
    }

    @Override
    public String getPermissions() {
        return null;
    }

    @Override
    public void perform(Player player, String[] args) {
        if(args.length == 3) {
            List<String> all = main.getConfig().getStringList("cryptos");
            int find = 0;
            for (String crypto : all) {
                String[] abv = crypto.split("/");
                if (args[1].equals(abv[0])) {
                    find = 1;
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
                    while (true){
                        try {
                            double total = main.getConfig().getDouble("amount."+abv[0])*parseDouble(args[2]);
                            if(Main.getEconomy().getBalance(player) < total){
                                player.sendMessage(main.prefix+ ChatColor.RED+"Tu n'as pas assez d'argent pour acheter "+args[2]+" "+abv[0]);
                                break;
                            }
                            Main.getEconomy().withdrawPlayer(player,total);
                            player.sendMessage(main.prefix+ChatColor.GREEN+"Vous venez d'acheter "+ ChatColor.GOLD+args[2]+" "+abv[0]+ChatColor.GREEN+" pour "+ChatColor.GOLD+total+"€");
                            configuration.set(cle,(double) configuration.get(cle)+parseDouble(args[2]));
                            try {
                                configuration.save(file);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            player.sendMessage(main.prefix+ChatColor.RED+args[2]+" n'est pas un nombre!");
                            break;
                        }
                    }
                    break;
                }
            }
            if (find == 0) {
                player.sendMessage(main.prefix+ChatColor.RED+"La crypto "+ChatColor.BOLD+args[1]+ChatColor.RESET+ChatColor.RED+" n'existe pas ou n'est pas prise en charge par le plugin.");
            }
        }
        else{
            player.sendMessage(main.prefix+ChatColor.RED+"Veuillez entrer la commande comme ceci: "+ getSyntax());
        }
    }

    @Override
    public String getSecondName() {
        return null;
    }
}
