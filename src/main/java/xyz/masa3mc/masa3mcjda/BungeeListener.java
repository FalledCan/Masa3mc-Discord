package xyz.masa3mc.masa3mcjda;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class BungeeListener implements Listener {

    HashMap<String,Boolean> sc = new HashMap<>();
    String GuildID = "";
    String RenkeiID = "";
    String RoleID = "";
    @EventHandler
    public void onServerConnect(ServerConnectEvent event){
        ProxiedPlayer player = event.getPlayer();
        Role role = Masa3mcJDA.jda.getGuildById(GuildID).getRoleById(RoleID);
        DBcontrol dBcontrol = new DBcontrol();
        dBcontrol.put(player.getName(),player.getUniqueId());
        if(dBcontrol.getDiscordID(event.getPlayer().getName()) != null)
            Masa3mcJDA.jda.getGuildById(GuildID).addRoleToMember(dBcontrol.getDiscordID(event.getPlayer().getName()),role).queue();
        if(sc.get(player.getName()) == null){
            TextChannel channel = Masa3mcJDA.jda.getTextChannelById(RenkeiID);
            EmbedBuilder builder = new EmbedBuilder()
                    .setColor(Color.GREEN)
                    .setTitle(player.getName() + " さんが参加しました。");

            channel.sendMessage(builder.build()).queue();
            sc.put(player.getName(),true);
            Masa3mcJDA.jda.getPresence().setActivity(Activity.playing(sc.size() + "人がまさ鯖"));
        }
    }

    @EventHandler
    public void onPlayerDisconnect(PlayerDisconnectEvent event){
        TextChannel channel = Masa3mcJDA.jda.getTextChannelById(RenkeiID);
        Role role = Masa3mcJDA.jda.getGuildById(GuildID).getRoleById(RoleID);
        DBcontrol dBcontrol = new DBcontrol();
        if(dBcontrol.getDiscordID(event.getPlayer().getName()) != null)
            Masa3mcJDA.jda.getGuildById(GuildID).removeRoleFromMember(dBcontrol.getDiscordID(event.getPlayer().getName()),role).queue();
        ProxiedPlayer player = event.getPlayer();
        EmbedBuilder builder = new EmbedBuilder()
                .setColor(Color.YELLOW)
                .setTitle(player.getName() + " さんが退出しました。");

        channel.sendMessage(builder.build()).queue();
        sc.remove(player.getName());
        Masa3mcJDA.jda.getPresence().setActivity(Activity.playing(sc.size() + "人がまさ鯖"));
        DiscordPartner.tokentime.remove(event.getPlayer().getName());
        DiscordPartner.tokensec.remove(event.getPlayer().getName());
        DiscordPartner.privatetoken.remove(event.getPlayer().getName());
    }


    @EventHandler
    public void getChat(ChatEvent event){

        String Ochat = event.getMessage();

        String global = String.valueOf(Ochat.charAt(0));

        if(!global.equals("!"))
            return;

        event.setCancelled(true);
        ProxiedPlayer player = (ProxiedPlayer) event.getSender();
        String chat;
        chat = Ochat.substring(1);
        ProxyServer.getInstance().getLogger().info(ChatColor.translateAlternateColorCodes('&', "&7[&6Global&7]&r " + player.getName() + "&6: &r" + chat));
        for (ProxiedPlayer target : ProxyServer.getInstance().getPlayers()) {
            target.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&6Global&7]&r " + player.getName() + "&6: &r" + chat));
        }
        TextChannel channel = Masa3mcJDA.jda.getTextChannelById(RenkeiID);
        channel.sendMessage("[Global] ["+ player.getServer().getInfo().getName() +"] " + player.getName() + "» " + chat).queue();
    }
}
