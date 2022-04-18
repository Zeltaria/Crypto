package fr.zeltaria.commands;

import fr.zeltaria.main.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InfoCryptoCmd implements TabExecutor {

    private static Main main;

    public InfoCryptoCmd(Main main){
        InfoCryptoCmd.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (sender instanceof Player){
            Player p = (Player)sender;
            if(strings.length == 1) {
                List<String> all = main.getConfig().getStringList("cryptos");
                int find = 0;
                for (String crypto : all) {
                    String[] abv = crypto.split("/");
                    if (strings[0].equals(abv[0])) {
                        find = 1;
                        URL url = null;
                        try {
                            url = new URL("https://api.binance.com/api/v3/ticker/price?symbol=" + strings[0] + "EUR");
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

                        while (true) {
                            try {
                                if ((inputLine = br.readLine()) == null) break;
                                String mots[] = inputLine.split("\"");
                                p.sendMessage(main.prefix + ChatColor.GREEN + "Nom de la crypto: " + ChatColor.BLUE + abv[1] + ChatColor.GREEN + "  Abrégé: " + ChatColor.BLUE + abv[0] + "\n" + ChatColor.GREEN + "            Prix: " + ChatColor.BLUE + mots[7] + "€");
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
                    p.sendMessage(main.prefix + ChatColor.RED + "La crypto " + ChatColor.BOLD + strings[0] + ChatColor.RESET + ChatColor.RED + " n'existe pas ou n'est pas prise en charge par le plugin.");
                }
            }
            else{
                p.sendMessage(main.prefix+ChatColor.RED+"Veuillez entrer la commande comme ceci: /infocrypto <crypto>");
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
