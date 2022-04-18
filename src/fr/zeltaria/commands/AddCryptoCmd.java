package fr.zeltaria.commands;

import fr.zeltaria.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.Double.parseDouble;

public class AddCryptoCmd implements TabExecutor {

    private final Main main;

    public AddCryptoCmd(Main main){this.main = main;}

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if(p.hasPermission("crypto.addcrypto")){
                if(strings.length == 3){
                    List<String> all = main.getConfig().getStringList("cryptos");
                    Player target = Bukkit.getPlayerExact(strings[0]);
                    int find = 0;
                    for (String crypto : all) {
                        String[] abv = crypto.split("/");
                        if (strings[1].equals(abv[0])) {
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
                            String cle = target.getUniqueId()+"."+abv[0];
                            if(!configuration.contains(cle)){
                                configuration.set(cle,(double) 0);
                                try {
                                    configuration.save(file);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            configuration.set(cle,(double) configuration.get(cle)+parseDouble(strings[2]));
                            try {
                                configuration.save(file);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            p.sendMessage(main.prefix+ ChatColor.GREEN+"Vous avez ajouter "+ChatColor.GOLD+strings[2]+" "+abv[0]+ChatColor.GREEN+" au joueur "+ChatColor.GOLD+strings[0]);
                            p.sendMessage(main.prefix+ ChatColor.GREEN+"Son solde de "+ChatColor.GOLD+abv[0]+ChatColor.GREEN+" s'élève donc à "+ChatColor.GOLD+configuration.get(cle)+abv[0]);
                        }
                    }
                    if (find == 0) {
                        p.sendMessage(main.prefix + ChatColor.RED + "La crypto " + ChatColor.BOLD + strings[1] + ChatColor.RESET + ChatColor.RED + " n'existe pas ou n'est pas prise en charge par le plugin.");
                    }
                }
                else{
                    p.sendMessage(main.prefix+ ChatColor.RED+"Veuillez entrer la commande comme ceci: /addcrypto <joueur> <crypto> <montant>");
                }
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] strings) {
        if(sender.hasPermission("crypto.addcrypto")){
            if(strings.length == 1){
                List<String> list = new ArrayList<String>();
                for (Player p : Bukkit.getOnlinePlayers()) {
                    list.add(p.getName());
                }
                return list;
            }
            if(strings.length == 2){
                List<String> all = main.getConfig().getStringList("cryptos");
                List<String> cryptos = new ArrayList<String>();
                for (String crypto : all) {
                    String[] abv = crypto.split("/");
                    cryptos.add(abv[0]);
                }
                return cryptos;
            }
        }
        return Collections.singletonList("");
    }
}
