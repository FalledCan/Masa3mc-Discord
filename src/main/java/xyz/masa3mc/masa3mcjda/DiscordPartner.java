package xyz.masa3mc.masa3mcjda;

import net.dv8tion.jda.api.entities.Role;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class DiscordPartner extends Command {
    public DiscordPartner() {
        super("discord");
    }

    String GuildID = "";
    String RoleID = "";
    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) sender;
            for (String p : JDAListeners.playercheck.values()) {
                if(player.getName().equals(p)){
                    DBcontrol dBcontrol = new DBcontrol();
                    dBcontrol.setDiscordID(player.getUniqueId(),JDAListeners.checker.get(player.getName()).getId());
                    Role role = Masa3mcJDA.jda.getGuildById(GuildID).getRoleById(RoleID);
                    Role role1 = Masa3mcJDA.jda.getGuildById(GuildID).getRoleById("");
                    Role role2 = Masa3mcJDA.jda.getGuildById(GuildID).getRoleById("");
                    Masa3mcJDA.jda.getGuildById(GuildID).addRoleToMember(JDAListeners.checker.get(player.getName()).getUser(),role).queue();
                    Masa3mcJDA.jda.getGuildById(GuildID).addRoleToMember(JDAListeners.checker.get(player.getName()).getUser(),role1).queue();
                    Masa3mcJDA.jda.getGuildById(GuildID).removeRoleFromMember(JDAListeners.checker.get(player.getName()).getUser(),role2).queue();
                    JDAListeners.checkertimer.remove(player.getName());
                    JDAListeners.playercheck.remove(JDAListeners.checker.get(player.getName()));
                    JDAListeners.checker.remove(player.getName());
                    player.sendMessage(new TextComponent(ChatColor.AQUA + "Discord連携: " + ChatColor.WHITE +"連携が完了しました。"));
                    return;
                }
            }
            player.sendMessage(new TextComponent( ChatColor.AQUA + "Discord連携: " + ChatColor.WHITE + player.getName() +"に接続リクエストがありません。"));
        }else {
            System.out.println("Noooooooooooooooooooooo");
        }
    }

}
