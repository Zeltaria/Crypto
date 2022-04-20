package fr.zeltaria.main;

import fr.zeltaria.commands.*;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;


public class Main extends JavaPlugin{

    public String prefix = ChatColor.LIGHT_PURPLE+"[Crypto] ";
    private static Economy econ = null;

    @Override
    public void onEnable(){
        saveDefaultConfig();
        System.out.println("Crypto à bien été chargé!");
        getCommand("crypto").setExecutor(new CommandManager(this));
        setupEconomy();
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
}
