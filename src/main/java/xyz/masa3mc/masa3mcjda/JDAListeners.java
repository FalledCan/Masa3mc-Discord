package xyz.masa3mc.masa3mcjda;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class JDAListeners extends ListenerAdapter {
    public static HashMap<Member,String> playercheck = new HashMap<>();
    public static HashMap<String,Member> checker = new HashMap<>();
    public static HashMap<String,Integer> checkertimer = new HashMap<>();

    @Override
    public void onGuildReady(GuildReadyEvent event){
        List<CommandData> commandData = new ArrayList<>();
        commandData.add(Commands.slash("setmcid","DiscordとMinecraftを連携します").addOption(OptionType.STRING,"mcid", "あなたのMCIDを入力してください"));
        commandData.add(Commands.slash("players","サーバー内の人数を表示します"));
        event.getGuild().updateCommands().addCommands(commandData).queue();
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if(event.getName().equals("players")) {
            if (event.getChannel().getId().equals("renkei")) {
                event.reply("❌ここでは使用できません。").setEphemeral(true).queue();
                return;
            }
            String lobby;
            if (ProxyServer.getInstance().getServerInfo("Lobby").getPlayers().size() != 0) {
                lobby = "\n-";
                for (ProxiedPlayer player : ProxyServer.getInstance().getServerInfo("Lobby").getPlayers()) {
                    lobby = lobby + " " + player.getName();
                }
            } else {
                lobby = "";
            }

            String Creative;
            if (ProxyServer.getInstance().getServerInfo("Creative").getPlayers().size() != 0) {
                Creative = "\n-";
                for (ProxiedPlayer player : ProxyServer.getInstance().getServerInfo("Creative").getPlayers()) {
                    Creative = Creative + " " + player.getName();
                }
            } else {
                Creative = "";
            }

            String Survival;
            if (ProxyServer.getInstance().getServerInfo("Survival").getPlayers().size() != 0) {
                Survival = "\n-";
                for (ProxiedPlayer player : ProxyServer.getInstance().getServerInfo("Survival").getPlayers()) {
                    Survival = Survival + " " + player.getName();
                }
            } else {
                Survival = "";
            }

            String PvP;
            if (ProxyServer.getInstance().getServerInfo("PvP").getPlayers().size() != 0) {
                PvP = "\n-";
                for (ProxiedPlayer player : ProxyServer.getInstance().getServerInfo("PvP").getPlayers()) {
                    PvP = PvP + " " + player.getName();
                }
            } else {
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
                            + "\n" + "PvP: " + PvPPlayers + PvP, true);

            event.replyEmbeds(builder.build()).queue();
        }else if(event.getName().equals("setmcid")){
            if(event.getChannel().getId().equals("renkei")){
                String mcid = event.getOption("mcid").getAsString();
                for(String string: playercheck.values()){
                    if(Objects.equals(string, mcid)){
                        event.reply("5分間隔を開けてください。").setEphemeral(true).queue();
                        return;
                    }
                }
                for(ProxiedPlayer player: ProxyServer.getInstance().getPlayers()){
                    if(player.getName().equals(mcid)){
                        DBcontrol dBcontrol = new DBcontrol();
                        if(dBcontrol.getDiscordID(mcid) == null) {
                            event.reply("連携リクエストを送りました。\nMinecraftの画面を開き/discordを実行してください。(5分以内に)").setEphemeral(true).queue();
                            checkServer(mcid, event.getMember());
                            return;
                        }
                    }
                }
                event.reply("masa3mc.xyzに接続した状態で実行してください。").setEphemeral(true).queue();
            }else {
                event.reply("❌ここでは使用できません。").setEphemeral(true).queue();
            }
        }
    }

    private void checkServer(String mcid,Member member) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(mcid);
        player.sendMessage(ChatMessageType.CHAT, new TextComponent(
                ChatColor.AQUA + "Discord連携: " + ChatColor.WHITE + member.getEffectiveName() + " から連携リクエストが届きました。\n" +
                        ChatColor.AQUA + "Discord連携: " + ChatColor.WHITE + "あなたのアカウントの場合は" + ChatColor.GREEN + "/discord" + ChatColor.WHITE + "を実行してください。\n" +
                        ChatColor.AQUA + "Discord連携: " + ChatColor.WHITE + "あなたのアカウントではない場合は無視してください。"));
        playercheck.put(member,mcid);
        checker.put(mcid,member);
        checkertimer.put(mcid,300);
    }
}
