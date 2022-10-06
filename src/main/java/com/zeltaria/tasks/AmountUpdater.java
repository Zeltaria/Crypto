package com.zeltaria.tasks;

import com.zeltaria.crypto.Main;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class AmountUpdater extends BukkitRunnable {

    private int timer;

    private final SchedulerManager sm;

    private final Main main;

    public AmountUpdater(SchedulerManager sm, Main main){
        this.sm = sm;
        timer = 300;
        this.main = main;
    }

    @Override
    public void run() {
        if (timer == 0) {
            List<String> all = main.getConfig().getStringList("cryptos");
            for (String crypto : all) {
                String[] abv = crypto.split("/");
                main.getConfig().set("amount." + abv[0], main.getAmount(abv[0]));
            }
            main.saveConfig();
            sm.relaunchUpdater();
            cancel();
        }
        timer--;
    }
}
