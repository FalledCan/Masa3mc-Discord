package xyz.masa3mc.masa3mcjda;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class PingCommand extends Command {

    public PingCommand() {
        super("ping");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if(sender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) sender;

            int ping = player.getPing();
            String pings;
            if(ping < 31){
                pings = "§a" + ping + "ms";
            }else if(ping < 101){
                pings = "§e" + ping + "ms";
            }else {
                pings = "§c" + ping + "ms";
            }

            player.sendMessage(ChatMessageType.ACTION_BAR,new TextComponent("§6Your ping: " + pings));
        }

    }
}
