package fr.zeltaria.commands;

import fr.zeltaria.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class BalanceAllCryptosCmd implements CommandExecutor {

    private final Main main;

    public BalanceAllCryptosCmd(Main main){
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if(sender instanceof Player){
            Player p = (Player) sender;
            List<String> all = main.getConfig().getStringList("cryptos");
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
        else{
            System.out.println(ChatColor.RED+"Cette commande ne peut être éxécutée que par des Joueurs");
        }
        return false;
    }
}
