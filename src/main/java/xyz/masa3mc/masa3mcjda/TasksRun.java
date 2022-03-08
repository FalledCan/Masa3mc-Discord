package xyz.masa3mc.masa3mcjda;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Timer;
import java.util.TimerTask;

public class TasksRun {

    public void run(){

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                for(ProxiedPlayer player: ProxyServer.getInstance().getPlayers()) {
                    if (DiscordPartner.tokensec.get(player.getName()) != null) {
                        if (DiscordPartner.tokensec.get(player.getName()) > 0) {
                            DiscordPartner.tokensec.put(player.getName(), DiscordPartner.tokensec.get(player.getName()) - 1);
                            if(DiscordPartner.tokentime.get(player.getName()) != null){
                                if(DiscordPartner.tokentime.get(player.getName()) == 0){
                                    DiscordPartner.privatetoken.remove(player.getName());
                                    DiscordPartner.tokentime.remove(player.getName());
                                }else {
                                    DiscordPartner.tokentime.put(player.getName(), DiscordPartner.tokentime.get(player.getName()) - 1);
                                }
                            }
                        } else {
                            DiscordPartner.tokensec.remove(player.getName());
                        }
                    }
                }

            }
        };
        timer.scheduleAtFixedRate(task,0,1000);

    }

}
