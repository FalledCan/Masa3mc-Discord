package xyz.masa3mc.masa3mcjda;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.md_5.bungee.api.plugin.Plugin;

import javax.security.auth.login.LoginException;

public final class Masa3mcJDA extends Plugin {

    public static JDA jda;
    public static Masa3mcJDA in;
    @Override
    public void onEnable() {

        in = this;

        new TasksRun().run();

        DBcontrol dBcontrol = new DBcontrol();
        dBcontrol.loadDB();

        try {
            jda = JDABuilder.createDefault("token").build();
        } catch (LoginException e) {
            e.printStackTrace();
        }
        jda.addEventListener(new JDAListeners());
        getProxy().getPluginManager().registerListener(this ,new BungeeListener());
        getProxy().getPluginManager().registerCommand(this, new PingCommand());
        getProxy().getPluginManager().registerCommand(this, new DiscordPartner());

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
