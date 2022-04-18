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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.Double.parseDouble;

public class ResetCryptosCmd implements TabExecutor {

    private final Main main;

    public ResetCryptosCmd(Main main) {this.main = main;}

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission("crypto.resetcrypto")) {
                if (strings.length == 1) {
                    List<String> all = main.getConfig().getStringList("cryptos");
                    Player target = Bukkit.getPlayerExact(strings[0]);
                    for (String crypto : all) {
                        String[] abv = crypto.split("/");
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
                        configuration.set(cle, (double) 0);
                        try {
                            configuration.save(file);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    p.sendMessage(main.prefix + ChatColor.GREEN + "Le solde de toutes les cryptos du joueur " + ChatColor.GOLD + strings[0] + ChatColor.GREEN + " ont bien été réinitialisées");

                }
                else{
                    p.sendMessage(main.prefix + ChatColor.RED + "Veuillez entrer la commande comme ceci: /resetcrypto <joueur> ");
                }
            }
        }
        else{
            System.out.println(ChatColor.RED+"Cette commande ne peut être éxécutée que par des Joueurs");
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] strings) {
        if(sender.hasPermission("crypto.resetcrypto")){
            if(strings.length == 1){
                List<String> list = new ArrayList<String>();
                for (Player p : Bukkit.getOnlinePlayers()) {
                    list.add(p.getName());
                }
                return list;
            }
        }
        return Collections.singletonList("");
    }
}
