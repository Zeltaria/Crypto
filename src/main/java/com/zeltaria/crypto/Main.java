package com.zeltaria.crypto;

import com.zeltaria.annexes.CryptoExpansion;
import com.zeltaria.commands.CommandManager;
import com.zeltaria.tasks.SchedulerManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import static java.lang.Double.parseDouble;


public class Main extends JavaPlugin{

    public String prefix = ChatColor.LIGHT_PURPLE+"[Crypto] ";
    private static Economy econ = null;

    public SchedulerManager sm = new SchedulerManager(this);

    @Override
    public void onEnable(){
        saveDefaultConfig();
        if(getServer().getPluginManager().getPlugin("Vault") != null){
            if(getServer().getPluginManager().getPlugin("PlaceholderAPI") != null){
                new CryptoExpansion(this).register();
            }
            getCommand("crypto").setExecutor(new CommandManager(this));
            setupEconomy();
            List<String> all = getConfig().getStringList("cryptos");
            for (String crypto : all) {
                String[] abv = crypto.split("/");
                getConfig().set("amount."+abv[0],getAmount(abv[0]));
            }
            saveConfig();
            System.out.println("Crypto à bien été chargé!");
            sm.relaunchUpdater();
        }
        else{
            getServer().getPluginManager().disablePlugin(this);
        }

    }

    public boolean setupEconomy(){
        RegisteredServiceProvider<Economy> eco = getServer().getServicesManager().getRegistration(Economy.class);
        if(eco != null){
            econ = eco.getProvider();
        }
        return econ != null;
    }

    public static Economy getEconomy(){
        return econ;
    }

    public File getFile(String name){
        return new File(this.getDataFolder(),name);
    }

    public void testFile(File file){
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public double getAmount(String crypto){
        URL url = null;
        try {
            url = new URL("https://api.binance.com/api/v3/ticker/price?symbol=" + crypto + "EUR");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return -1;
        }
        URLConnection conn = null;
        try {
            conn = url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        }
        catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
        String inputLine;
        try {
            if ((inputLine = br.readLine()) == null) return -1;
            String[] mots = inputLine.split("\"");
            return parseDouble(mots[7]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public boolean isOnConfig(String crypto){
        List<String> all = getConfig().getStringList("cryptos");
        for (String recherche : all) {
            String[] abv = recherche.split("/");
            if (crypto.equals(abv[0])) {
                return true;
            }
        }
        return false;
    }

    public void testCrypt(Player p, String crypto){
        if(isOnConfig(crypto)){
            File file = getFile("balances.yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            String cle = p.getUniqueId()+"."+crypto;
            if (!config.contains(cle)) {
                config.set(cle, (double) 0);
                try {
                    config.save(file);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

    public double getPlayerAmountCrypto(Player p, String crypto){
        if(isOnConfig(crypto)) {
            testCrypt(p, crypto);
            File file = getFile("balances.yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            String cle = p.getUniqueId()+"."+crypto;
            return (double) config.get(cle);
        }
        else{
            return -1;
        }
    }
}
