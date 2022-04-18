package fr.zeltaria.commands;

import fr.zeltaria.main.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.Double.parseDouble;

public class SellCryptoCmd implements TabExecutor {

    private final Main main;

    public SellCryptoCmd(Main main){
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if(sender instanceof Player){
            Player p = (Player) sender;
            if(strings.length == 2) {
                List<String> all = main.getConfig().getStringList("cryptos");
                int find = 0;
                for (String crypto : all) {
                    String[] abv = crypto.split("/");
                    if (strings[0].equals(abv[0])) {
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
                        String cle = p.getUniqueId()+"."+abv[0];
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
                            url = new URL("https://api.binance.com/api/v3/ticker/price?symbol="+strings[0]+"EUR");
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
                                if (!((inputLine = br.readLine()) != null)) break;
                                String mots[] = inputLine.split("\"");
                                double total = parseDouble(strings[1])*parseDouble(mots[7]);
                                if((double) configuration.get(cle) < parseDouble(strings[1])){
                                    p.sendMessage(main.prefix+ChatColor.RED+"Tu n'as pas assez de "+abv[0]+" à vendre.");
                                    break;
                                }
                                Main.getEconomy().depositPlayer(p,total);
                                p.sendMessage(main.prefix+ChatColor.GREEN+"Vous venez de vendre "+ ChatColor.GOLD+strings[1]+" "+abv[0]+ChatColor.GREEN+" pour "+ChatColor.GOLD+total+"€");
                                configuration.set(cle,(double) configuration.get(cle)-parseDouble(strings[1]));
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
                    p.sendMessage(main.prefix+ChatColor.RED+"La crypto "+ChatColor.BOLD+strings[0]+ChatColor.RESET+ChatColor.RED+" n'existe pas ou n'est pas prise en charge par le plugin.");
                }
            }
            else{
                p.sendMessage(main.prefix+ChatColor.RED+"Veuillez entrer la commande comme ceci: /buycrypto <crypto> <montant>");
            }
        }
        else{
            System.out.println(ChatColor.RED+"Cette commande ne peut être éxécutée que par des Joueurs");
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
