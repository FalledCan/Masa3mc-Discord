package xyz.masa3mc.masa3mcjda;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

import java.util.HashMap;

public class DiscordPartner extends Command {
    public DiscordPartner() {
        super("gettoken");
    }

    public static HashMap<String,Integer> tokensec = new HashMap<>();
    public static HashMap<String,Integer> tokentime = new HashMap<>();
    public static HashMap<String,String> privatetoken = new HashMap<>();

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(new DBcontrol().token(sender.getName())) {
            if (tokensec.get(sender.getName()) == null) {
                String token = createToken();
                sender.sendMessage(new TextComponent("§cDiscordの#botで次のコマンドを２分以内に入力してください: \n§a!ms token " + token + "\n§c※注意-連携が完了するまでログアウトしないでください!!"));
                privatetoken.put(sender.getName(),token);
                tokentime.put(sender.getName(),120);
                tokensec.put(sender.getName(), 900);
            } else {
                int a = tokensec.get(sender.getName());
                if (a < 60) {
                    sender.sendMessage(new TextComponent("§c" + a + "秒間隔を開けてください!!"));
                } else {
                    sender.sendMessage(new TextComponent("§c" + a / 60 + "分" + a % 60 + "秒間隔を開けてください!!"));
                }
            }
        }else {
            sender.sendMessage(new TextComponent("§cすでに連携済みです。"));
        }
    }

    private String createToken() {
        String theAlphaNumericS;
        StringBuilder builder;
        theAlphaNumericS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +"abcdefghijklmnopqrstuvwxyz"+ "0123456789";
        builder = new StringBuilder(10);
        for (int m = 0; m < 10; m++) {
            int myindex = (int)(theAlphaNumericS.length() * Math.random());
            builder.append(theAlphaNumericS.charAt(myindex));
        }
        return builder.toString();
    }
}
