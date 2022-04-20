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

import static java.lang.Double.parseDouble;

public class AddSubCommand extends SubCommand {

    private final Main main;

    public AddSubCommand(Main main) {
        this.main = main;
    }

    @Override
    public String getName() {
        return "admin";
    }

    public String getSecondName() { return "add"; }

    @Override
    public String getDescription() {
        return "Ajouter de la crypto choisie au joueur désigné";
    }

    @Override
    public String getSyntax() {
        return "/crypto admin add "+ChatColor.YELLOW+"<joueur> <crypto> <montant>";
    }

    @Override
    public String getPermissions() {
        return "crypto.add";
    }

    @Override
    public void perform(Player player, String[] args) {
        if(args.length == 5){
            List<String> all = main.getConfig().getStringList("cryptos");
            Player target = Bukkit.getPlayerExact(args[2]);
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
                    String cle = target.getUniqueId()+"."+abv[0];
                    if(!configuration.contains(cle)){
                        configuration.set(cle,(double) 0);
                        try {
                            configuration.save(file);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    configuration.set(cle,(double) configuration.get(cle)+parseDouble(args[4]));
                    try {
                        configuration.save(file);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    player.sendMessage(main.prefix+ ChatColor.GREEN+"Vous avez ajouter "+ChatColor.GOLD+args[4]+" "+abv[0]+ChatColor.GREEN+" au joueur "+ChatColor.GOLD+args[2]);
                    player.sendMessage(main.prefix+ ChatColor.GREEN+"Son solde de "+ChatColor.GOLD+abv[0]+ChatColor.GREEN+" s'élève donc à "+ChatColor.GOLD+configuration.get(cle)+abv[0]);
                }
            }
            if (find == 0) {
                player.sendMessage(main.prefix + ChatColor.RED + "La crypto " + ChatColor.BOLD + args[3] + ChatColor.RESET + ChatColor.RED + " n'existe pas ou n'est pas prise en charge par le plugin.");
            }
        }
        else{
            player.sendMessage(main.prefix+ ChatColor.RED+"Veuillez entrer la commande comme ceci: "+ getSyntax());
        }
    }
}
