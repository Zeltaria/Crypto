package fr.zeltaria.commands.subcommands;

import fr.zeltaria.commands.SubCommand;
import fr.zeltaria.main.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class ListSubCommand extends SubCommand {

    private final Main main;

    public ListSubCommand(Main main){
        this.main = main;
    }

    @Override
    public String getName() {
        return "list";
    }

    @Override
    public String getDescription() {
        return "Montre toutes les cryptos prises en charge ainsi que le montant pour 1 crypto";
    }

    @Override
    public String getSyntax() {
        return "/crypto list";
    }

    @Override
    public String getPermissions() {
        return null;
    }

    @Override
    public void perform(Player player, String[] args) {
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
                    if ((inputLine = br.readLine()) == null) break;
                    String[] mots = inputLine.split("\"");
                    String[] crypt = crypto.split("/");
                    player.sendMessage(main.prefix+" "+ ChatColor.GREEN+crypt[0]+": "+ChatColor.GOLD+mots[7]+"€");
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

    @Override
    public String getSecondName() {
        return null;
    }
}
