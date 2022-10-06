package com.zeltaria.tasks;

import com.zeltaria.crypto.Main;

public class SchedulerManager {

    private final Main main;

    public AmountUpdater task;

    public SchedulerManager(Main main) {
        this.main = main;
    }

    public void relaunchUpdater(){
        task = new AmountUpdater(this, main);
    }
}
