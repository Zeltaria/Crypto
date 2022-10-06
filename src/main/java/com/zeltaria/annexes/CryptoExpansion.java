package com.zeltaria.annexes;

import com.zeltaria.crypto.Main;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

import java.util.List;

import static java.lang.Double.parseDouble;

public class CryptoExpansion extends PlaceholderExpansion {

    private final com.zeltaria.crypto.Main main;


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
        if (p == null) {
            return "";
        }
        List<String> all = main.getConfig().getStringList("cryptos");
        for (String crypto : all) {
            String[] abv = crypto.split("/");
            if(!params.startsWith(abv[0])) {
                continue;
            }
            if (params.equals(abv[0] + "account")) {
                main.testCrypt(p, abv[0]);
                double total = main.getPlayerAmountCrypto(p,abv[0]);
                if(total == -1){
                    return "ERROR";
                }
                return total + " " + abv[0];
            }
            if (params.equals(abv[0] + "amount") || params.startsWith(abv[0] + "amount_")) {
                if (main.isOnConfig(abv[0])) {
                    double amount = main.getConfig().getDouble("amount."+abv[0]);
                    if (params.startsWith(abv[0] + "amount_")) {
                        String s = params.replace(abv[0] + "amount_", "");
                        if (parseDouble(s) > 0) {
                            return (amount * parseDouble(s)) + "€";
                        } else {
                            return "ERROR";
                        }
                    }
                    return amount + "€";
                }
            }
        }
        return null;
    }
}
