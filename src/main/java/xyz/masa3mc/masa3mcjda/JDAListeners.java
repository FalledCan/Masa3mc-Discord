package xyz.masa3mc.masa3mcjda;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.awt.*;
import java.util.Objects;


public class JDAListeners extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if(event.getMessage().getContentRaw().equalsIgnoreCase("!ms status")){
            TextChannel channel = event.getChannel();
            Message message = event.getMessage();
            sendServerStatus(channel, message);
        }
        if(event.getMessage().getContentRaw().contains("!ms check")){
            TextChannel channel = event.getChannel();
            Member member = event.getMember();
            Message message = event.getMessage();
            sendPlayerInfo(channel,member,message);
        }
        if(event.getMessage().getContentRaw().contains("!ms token")){
            TextChannel channel = event.getChannel();
            Member member = event.getMember();
            Message message = event.getMessage();
            checkToken(channel,member,message);
        }
    }

    private void checkToken(TextChannel channel, Member member, Message message) {
        String[] msg = message.getContentRaw().split(" ");
        if(msg.length == 3) {
            String token = msg[2];
            for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                if (Objects.equals(DiscordPartner.privatetoken.get(player.getName()), token)) {
                    new DBcontrol().setDiscordID(player.getUniqueId(), member.getId());
                    channel.sendMessage("<@" + member.getId() + ">と" + player.getName() + "の連携に成功しました。").queue(message1 -> {
                        try {
                            Thread.sleep(3000);
                            message1.delete().queue();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    });
                    DiscordPartner.tokentime.remove(player.getName());
                    DiscordPartner.tokensec.remove(player.getName());
                    DiscordPartner.privatetoken.remove(player.getName());
                    message.delete().queue();
                    return;
                }
            }
            channel.sendMessage("<@" + member.getId() + ">との連携に失敗しました、やり直してください。").queue(message1 -> {
                try {
                    Thread.sleep(3000);
                    message1.delete().queue();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        message.delete().queue();
    }

    private void sendPlayerInfo(TextChannel channel, Member member, Message message) {
        String[] msg = message.getContentRaw().split(" ");
        if(msg.length == 3) {
            String msgmcid = msg[2].toUpperCase();
            try {
                ProxiedPlayer player = ProxyServer.getInstance().getPlayer(msgmcid);
                String mcid = player.getName();
                String discord_name = null;
                if(new DBcontrol().getDiscordID(msgmcid) != null)
                    discord_name = channel.getGuild().getMemberById(new DBcontrol().getDiscordID(msgmcid)).getUser().getName();
                EmbedBuilder builder = new EmbedBuilder()
                        .setColor(Color.GREEN)
                        .setFooter("masa3mc.xyz")
                        .setTitle(mcid + "'s Info", "https://ja.namemc.com/profile/" + mcid + ".1")
                        .addField("オンライン", "接続サーバー: " + player.getServer().getInfo().getName()
                                + "\nUUID: " + player.getUniqueId().toString()
                                + "\nPing: " + player.getPing() + "ms"
                                + "\n連携: " + discord_name
                                + "\n最終ログイン: " + new DBcontrol().getInTime(msgmcid), true)
                        .setImage("https://crafatar.com/renders/body/" + player.getUniqueId().toString());
                channel.sendMessage(builder.build()).queue();
            } catch (Exception e) {
                String discord_name = null;
                if(new DBcontrol().getDiscordID(msgmcid) != null)
                    discord_name = channel.getGuild().getMemberById(new DBcontrol().getDiscordID(msgmcid)).getUser().getName();
                String uuid = new DBcontrol().getUuid(msgmcid);
                EmbedBuilder builder = new EmbedBuilder()
                        .setColor(Color.RED)
                        .setFooter("masa3mc.xyz")
                        .setTitle(msgmcid + "'s Info", "https://ja.namemc.com/profile/" + msgmcid + ".1")
                        .addField("オフライン", "接続サーバー: none"
                                + "\nUUID: " + uuid
                                + "\nPing: -1ms"
                                + "\n連携: " + discord_name
                                + "\n最終ログイン: " + new DBcontrol().getInTime(msgmcid), true)
                        .setImage("https://crafatar.com/renders/body/" + uuid);
                channel.sendMessage(builder.build()).queue();
            }
        }
        message.delete().queue();
    }


    private void sendServerStatus(TextChannel channel, Message message) {
        String lobby;
        if(ProxyServer.getInstance().getServerInfo("Lobby").getPlayers().size() != 0){
            lobby = "\n-";
            for(ProxiedPlayer player: ProxyServer.getInstance().getServerInfo("Lobby").getPlayers()){
                lobby = lobby + " " + player.getName();
            }
        }else {
            lobby = "";
        }

        String Creative;
        if(ProxyServer.getInstance().getServerInfo("Creative").getPlayers().size() != 0){
            Creative = "\n-";
            for(ProxiedPlayer player: ProxyServer.getInstance().getServerInfo("Creative").getPlayers()){
                Creative = Creative + " " + player.getName();
            }
        }else {
            Creative = "";
        }

        String Survival;
        if(ProxyServer.getInstance().getServerInfo("Survival").getPlayers().size() != 0){
            Survival = "\n-";
            for(ProxiedPlayer player: ProxyServer.getInstance().getServerInfo("Survival").getPlayers()){
                Survival = Survival + " " + player.getName();
            }
        }else {
            Survival = "";
        }

        String PvP;
        if(ProxyServer.getInstance().getServerInfo("PvP").getPlayers().size() != 0){
            PvP = "\n-";
            for(ProxiedPlayer player: ProxyServer.getInstance().getServerInfo("PvP").getPlayers()){
                PvP = PvP + " " + player.getName();
            }
        }else {
            PvP = "";
        }

        int ServerPlayers = ProxyServer.getInstance().getOnlineCount();
        int LobbyPlayers = ProxyServer.getInstance().getServerInfo("Lobby").getPlayers().size();
        int CreativePlayers = ProxyServer.getInstance().getServerInfo("Creative").getPlayers().size();
        int SurvivalPlayers = ProxyServer.getInstance().getServerInfo("Survival").getPlayers().size();
        int PvPPlayers = ProxyServer.getInstance().getServerInfo("PvP").getPlayers().size();
        EmbedBuilder builder = new EmbedBuilder()
                .setColor(Color.ORANGE)
                .setFooter("masa3mc.xyz")
                .setTitle("Masa3MCNetwork Gen2")
                .addField("Info", "onlinePlayers:  " + ServerPlayers + "/100"
                        + "\n" + "Lobby: " + LobbyPlayers + lobby
                        + "\n" + "Creative: " + CreativePlayers + Creative
                        + "\n" + "Survival: " + SurvivalPlayers + Survival
                        + "\n" + "PvP: " + PvPPlayers + PvP , true);

        channel.sendMessage(builder.build()).queue();
        message.delete().queue();
    }
}
