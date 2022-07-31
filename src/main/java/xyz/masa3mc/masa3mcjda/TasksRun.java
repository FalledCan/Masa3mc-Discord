package xyz.masa3mc.masa3mcjda;

import java.util.Timer;
import java.util.TimerTask;

public class TasksRun {

    public void run(){

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                for (String mcid: JDAListeners.playercheck.values()){
                    int i = JDAListeners.checkertimer.get(mcid);
                    if(i == 0) {
                        JDAListeners.checkertimer.remove(mcid);
                        JDAListeners.playercheck.remove(JDAListeners.checker.get(mcid));
                        JDAListeners.checker.remove(mcid);
                    }else {
                        JDAListeners.checkertimer.put(mcid, i - 1);
                    }
                }

            }
        };
        timer.scheduleAtFixedRate(task,0,1000);

    }

}
