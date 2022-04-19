package fr.zeltaria.commands.subcommands;

import fr.zeltaria.commands.SubCommand;
import fr.zeltaria.main.Main;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
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
        return "/crypto buy <crypto> <montant>";
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
                    if(!file.exists()) {
                        try {
                            file.createNewFile();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
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
                    URL url = null;
                    try {
                        url = new URL("https://api.binance.com/api/v3/ticker/price?symbol="+args[1]+"EUR");
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    URLConnection conn = null;
                    try {
                        conn = url.openConnection();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    BufferedReader br = null;
                    try {
                        br = new BufferedReader(
                                new InputStreamReader(conn.getInputStream())
                        );
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String inputLine;

                    while (true){
                        try {
                            if ((inputLine = br.readLine()) == null) break;
                            String[] mots = inputLine.split("\"");
                            double total = parseDouble(args[2])*parseDouble(mots[7]);
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
                        } catch (IOException e) {
                            e.printStackTrace();
                            break;
                        }
                    }
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
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
