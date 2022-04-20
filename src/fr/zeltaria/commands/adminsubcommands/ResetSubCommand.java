package fr.zeltaria.commands.adminsubcommands;

import fr.zeltaria.commands.SubCommand;
import fr.zeltaria.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ResetSubCommand extends SubCommand {

    private final Main main;

    public ResetSubCommand(Main main) {this.main = main;}

    @Override
    public String getName() {
        return "admin";
    }

    @Override
    public String getDescription() {
        return "Remet à 0 toutes les cryptos du joueur désigné";
    }

    @Override
    public String getSyntax() {
        return "/crypto admin reset"+ChatColor.YELLOW+" <joueur>";
    }

    @Override
    public String getPermissions() {
        return "crypto.reset";
    }

    @Override
    public void perform(Player player, String[] args) {
        if (args.length == 3) {
            List<String> all = main.getConfig().getStringList("cryptos");
            Player target = Bukkit.getPlayerExact(args[2]);
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
            player.sendMessage(main.prefix + ChatColor.GREEN + "Le solde de toutes les cryptos du joueur " + ChatColor.GOLD + args[2] + ChatColor.GREEN + " ont bien été réinitialisées");

        }
        else{
            player.sendMessage(main.prefix + ChatColor.RED + "Veuillez entrer la commande comme ceci: /resetcrypto <joueur> ");
        }
    }

    @Override
    public String getSecondName() {
        return "reset";
    }
}
