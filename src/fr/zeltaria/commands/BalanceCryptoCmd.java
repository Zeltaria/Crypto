package fr.zeltaria.commands;

import fr.zeltaria.main.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BalanceCryptoCmd implements TabExecutor {

    private final Main main;

    public BalanceCryptoCmd(Main main){
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if(sender instanceof Player){
            Player p = (Player) sender;
            if(strings.length == 1){
                List<String> all = main.getConfig().getStringList("cryptos");
                int find = 0;
                for (String crypto : all) {
                    String[] abv = crypto.split("/");
                    if (strings[0].equals(abv[0])) {
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
                        String cle = p.getUniqueId()+"."+abv[0];
                        if(!configuration.contains(cle)){
                            configuration.set(cle,(double) 0);
                            try {
                                configuration.save(file);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        p.sendMessage(main.prefix+ ChatColor.GREEN+abv[0]+": "+ChatColor.GOLD+configuration.get(cle));
                    }
                }
                if (find == 0) {
                    p.sendMessage(main.prefix + ChatColor.RED + "La crypto " + ChatColor.BOLD + strings[0] + ChatColor.RESET + ChatColor.RED + " n'existe pas ou n'est pas prise en charge par le plugin.");
                }
            }
            else{
                p.sendMessage(main.prefix+ ChatColor.RED+"Veuillez entrer la commande comme ceci: /balancecrypto <crypto>");
            }
        }
        else{
            System.out.println("Cette commande ne peut être exécuté que par un joueur");
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        if(strings.length == 1){
            List<String> all = main.getConfig().getStringList("cryptos");
            List<String> cryptos = new ArrayList<String>();
            for (String crypto : all) {
                String[] abv = crypto.split("/");
                cryptos.add(abv[0]);
            }
            return cryptos;
        }
        return Collections.singletonList("");
    }
}
