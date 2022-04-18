package fr.zeltaria.commands;

import fr.zeltaria.main.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class ListCryptoCmd implements CommandExecutor {
    
    private static Main main;
    public ListCryptoCmd(Main main){
        ListCryptoCmd.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (sender instanceof Player){
            Player p = (Player)sender;
            List<String> all = main.getConfig().getStringList("cryptos");
            for (String crypto : all) {
                String[] abv = crypto.split("/");
                URL url = null;
                try {
                    url = new URL("https://api.binance.com/api/v3/ticker/price?symbol="+abv[0]+"EUR");
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
                        String crypt[] = crypto.split("/");
                        p.sendMessage(main.prefix+" "+ ChatColor.GREEN+crypt[0]+": "+ChatColor.GOLD+mots[7]+"€");
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
            }
        }
        else{
            System.out.println("Cette commande ne peut être exécuté que par un joueur");
        }

        return false;
    }
}
