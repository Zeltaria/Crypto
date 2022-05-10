package fr.zeltaria.main;

import fr.zeltaria.commands.*;
import fr.zeltaria.papi.CryptoExpansion;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;


public class Main extends JavaPlugin{

    public String prefix = ChatColor.LIGHT_PURPLE+"[Crypto] ";
    private static Economy econ = null;

    @Override
    public void onEnable(){
        saveDefaultConfig();
        if(getServer().getPluginManager().getPlugin("Vault") != null){
            if(getServer().getPluginManager().getPlugin("PlaceholderAPI") != null){
                new CryptoExpansion(this).register();
            }
            System.out.println("Crypto à bien été chargé!");
            getCommand("crypto").setExecutor(new CommandManager(this));
            setupEconomy();
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
}
