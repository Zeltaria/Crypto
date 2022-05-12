package fr.zeltaria.papi;

import fr.zeltaria.main.Main;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
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

public class CryptoExpansion extends PlaceholderExpansion {

    private fr.zeltaria.main.Main main;


    public CryptoExpansion(Main main) {
        this.main = main;
    }

    @Override
    public String getIdentifier() {
        return "Crypto";
    }

    @Override
    public String getAuthor() {
        return "Zeltaria";
    }

    @Override
    public String getVersion() {
        return "1.3";
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player p, String params) {
        if(p==null){
            return "";
        }
        List<String> all = main.getConfig().getStringList("cryptos");
        for (String crypto : all) {
            String[] abv = crypto.split("/");
            if(params.equals(abv[0]+"account")) {
                File file = main.getFile("balances.yml");
                YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
                String cle = p.getUniqueId() + "." + abv[0];
                if (!configuration.contains(cle)) {
                    configuration.set(cle, (double) 0);
                    try {
                        configuration.save(file);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                return configuration.get(cle) + " " + abv[0];
            }
            if (params.equals(abv[0] + "amount") || params.startsWith(abv[0] + "amount_")) {
                URL url = null;
                try {
                    url = new URL("https://api.binance.com/api/v3/ticker/price?symbol=" + abv[0] + "EUR");
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
                    br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                String inputLine;
                while (true) {
                    try {
                        if ((inputLine = br.readLine()) == null) break;
                        String[] mots = inputLine.split("\"");
                        if(params.startsWith(abv[0]+"amount_")){
                            String s = params.replace(abv[0]+"amount_", "");
                            if(parseDouble(s) > 0){
                                return parseDouble(mots[7]) * parseDouble(s) + "€";
                            }
                        }
                        return parseDouble(mots[7]) + "€";
                    } catch (IOException e) {
                        e.printStackTrace();
                        break;
                    }
                }
                try {
                    br.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        return null;
    }
}
