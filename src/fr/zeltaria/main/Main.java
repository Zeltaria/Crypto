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
        this.getCommand("listcryptos").setExecutor(new ListCryptoCmd(this));
        this.getCommand("infocrypto").setExecutor(new InfoCryptoCmd(this));
        this.getCommand("buycrypto").setExecutor(new BuyCryptoCmd(this));
        this.getCommand("sellcrypto").setExecutor(new SellCryptoCmd(this));
        this.getCommand("balanceallcryptos").setExecutor(new BalanceAllCryptosCmd(this));
        this.getCommand("balancecrypto").setExecutor(new BalanceCryptoCmd(this));
        this.getCommand("addcrypto").setExecutor(new AddCryptoCmd(this));
        this.getCommand("removecrypto").setExecutor(new RemoveCryptoCmd(this));
        this.getCommand("setcrypto").setExecutor(new SetCryptoCmd(this));
        this.getCommand("resetcrypto").setExecutor(new ResetCryptosCmd(this));
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


    //***************************************************
    //Exemple de code pour récup une valeur d'une crypto
    //***************************************************
    /*public static void main(String[] args) throws IOException {
        URL url = new URL("https://api.binance.com/api/v3/ticker/price?symbol=BTCEUR");
        URLConnection conn = url.openConnection();
        System.out.println("Connected");

        BufferedReader br = new BufferedReader(
                new InputStreamReader(conn.getInputStream())
        );
        String inputLine;

        while ((inputLine = br.readLine()) != null){
            System.out.println(inputLine);
            String mots[] = inputLine.split("\"");
            double wallet = 250000;
            wallet -= Double.parseDouble(mots[7]);
            System.out.println(mots[7]);
            System.out.println(wallet);
        }
        br.close();
    }*/
}
