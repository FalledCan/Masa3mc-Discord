package xyz.masa3mc.masa3mcjda;

import com.github.ucchyocean.lc3.japanize.JapanizeType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.UserSnowflake;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.awt.*;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            Masa3mcJDA.jda.getGuildById(GuildID).addRoleToMember(UserSnowflake.fromId(dBcontrol.getDiscordID(event.getPlayer().getName())),role).queue();

        if(sc.get(player.getName()) == null){
            TextChannel channel = Masa3mcJDA.jda.getTextChannelById(RenkeiID);
            EmbedBuilder builder = new EmbedBuilder()
                    .setColor(Color.GREEN)
                    .setTitle(player.getName() + " さんが参加しました。");

            channel.sendMessageEmbeds( builder.build()).queue();
            sc.put(player.getName(),true);
            Masa3mcJDA.jda.getPresence().setActivity(Activity.playing(sc.size() + "人がまさ鯖"));
        }
    }

    @EventHandler
    public void onPlayerDisconnect(PlayerDisconnectEvent event){
        ProxiedPlayer player = event.getPlayer();
        TextChannel channel = Masa3mcJDA.jda.getTextChannelById(RenkeiID);
        Role role = Masa3mcJDA.jda.getGuildById(GuildID).getRoleById(RoleID);
        DBcontrol dBcontrol = new DBcontrol();
        if(dBcontrol.getDiscordID(player.getName()) != null)
            Masa3mcJDA.jda.getGuildById(GuildID).removeRoleFromMember(UserSnowflake.fromId(dBcontrol.getDiscordID(player.getName())),role).queue();
        EmbedBuilder builder = new EmbedBuilder()
                .setColor(Color.YELLOW)
                .setTitle(player.getName() + " さんが退出しました。");

        channel.sendMessageEmbeds(builder.build()).queue();
        sc.remove(player.getName());
        Masa3mcJDA.jda.getPresence().setActivity(Activity.playing(sc.size() + "人がまさ鯖"));
        JDAListeners.checkertimer.remove(event.getPlayer().getName());
        JDAListeners.playercheck.remove(JDAListeners.checker.get(event.getPlayer().getName()));
        JDAListeners.checker.remove(event.getPlayer().getName());
    }


    @EventHandler()
    public void getChat(ChatEvent event){
        String Ochat = event.getMessage();
        ProxiedPlayer player = (ProxiedPlayer) event.getSender();
        if(String.valueOf(Ochat.charAt(0)).equals("!")) {
            event.setCancelled(true);
            String chat;
            chat = Ochat.substring(1);
            Pattern pattern = Pattern.compile("^[A-Za-z0-9-~=!?^_&%#$|()/+*<>:;.,{}'\"@` ]+$");
            Matcher matcher = pattern.matcher(chat);
            if (matcher.matches()) {
                if (!chat.startsWith("#")) {
                    chat = Masa3mcJDA.lunachatapi.japanize(chat, JapanizeType.GOOGLE_IME) + " (" + event.getMessage().substring(1) + ")";
                }else
                    chat = chat.substring(1);
            }
            for (ProxiedPlayer target : ProxyServer.getInstance().getPlayers()) {
                target.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', "&7[&6Global&7]&r " + player.getName() + "&6: &r" + chat)));
            }
            TextChannel channel = Masa3mcJDA.jda.getTextChannelById(RenkeiID);
            channel.sendMessage("[" + player.getServer().getInfo().getName() + "] " + chat
                    .replace("&0","")
                    .replace("&1","")
                    .replace("&2","")
                    .replace("&3","")
                    .replace("&4","")
                    .replace("&5","")
                    .replace("&6","")
                    .replace("&7","")
                    .replace("&8","")
                    .replace("&9","")
                    .replace("&a","")
                    .replace("&b","")
                    .replace("&c","")
                    .replace("&d","")
                    .replace("&e","")
                    .replace("&f","")
                    .replace("&k","")
                    .replace("&l","")
                    .replace("&n","")
                    .replace("&m","")
            ).queue();
        }
    }
}
