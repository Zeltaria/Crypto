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

public class SellSubCommand extends SubCommand {

    private final Main main;

    public SellSubCommand(Main main){this.main = main;}

    @Override
    public String getName() {
        return "sell";
    }

    @Override
    public String getDescription() {
        return "Vendre de la crypto";
    }

    @Override
    public String getSyntax() {
        return "/crypto sell"+ChatColor.YELLOW+" <crypto> <montant>";
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
                    double amountP = main.getPlayerAmountCrypto(player,abv[0]);
                    double amountC = main.getAmount(abv[0]);
                    if(amountC == -1){
                        player.sendMessage(main.prefix+ ChatColor.RED+"Une erreur est survenue avec le lecteur de crypto. Contactez l'administrateur pour résoudre le problème.");
                    }
                    if(amountP == -1){
                        player.sendMessage(main.prefix+ChatColor.RED+"La crypto "+ abv[0]+ " n'est pas prise en charge par le plugin, si cela est une erreur merci de contacter un administrateur.");
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
                            if((double) configuration.get(cle) < parseDouble(args[2])){
                                player.sendMessage(main.prefix+ ChatColor.RED+"Tu n'as pas assez de "+abv[0]+" à vendre.");
                                break;
                            }
                            Main.getEconomy().depositPlayer(player,total);
                            player.sendMessage(main.prefix+ChatColor.GREEN+"Vous venez de vendre "+ ChatColor.GOLD+args[2]+" "+abv[0]+ChatColor.GREEN+" pour "+ChatColor.GOLD+total+"€");
                            configuration.set(cle,(double) configuration.get(cle)-parseDouble(args[2]));
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
            player.sendMessage(main.prefix+ChatColor.RED+"Veuillez entrer la commande comme ceci: "+getSyntax());
        }
    }

    @Override
    public String getSecondName() {
        return null;
    }
}
