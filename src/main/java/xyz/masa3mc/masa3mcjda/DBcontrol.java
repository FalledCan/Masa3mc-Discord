package xyz.masa3mc.masa3mcjda;

import net.md_5.bungee.api.ProxyServer;

import java.io.File;
import java.io.IOException;
import java.net.Proxy;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.UUID;

public class DBcontrol {

    public static Statement stmt = null;

    public void loadDB(){

        File file = new File(Masa3mcJDA.in.getDataFolder(), "/playerdata.db");
        if(!file.exists()) {
            file.getParentFile().mkdirs();
            createfile(file);
        return;
        }

        try {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + file.getAbsolutePath());
            stmt = conn.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

    }

    private void createfile(File file) {
        try {
            file.createNewFile();
            try {
                Class.forName("org.sqlite.JDBC");
                Connection conn = DriverManager.getConnection("jdbc:sqlite:" + file.getAbsolutePath());
                stmt = conn.createStatement();
                stmt.executeUpdate("CREATE TABLE players (UUID, MCID, DISCORD_ID, IN_TIME)");
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void put(String name, UUID uuid){

        String uuid_  = uuid.toString().replace("-", "");
        String name_ = null;
        try {
            ResultSet rs = stmt.executeQuery("select * from players where UUID = '" + uuid_ + "'");
            while (rs.next()){
                name_ = rs.getString("UUID");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日(E)ahh時mm分ss秒");
        if(name_ == null) {
            try {
                stmt.executeUpdate("INSERT INTO players VALUES('" + uuid_ + "', '" + name.toUpperCase() + "',null,'" + sdf.format(date) + "')");
            } catch (SQLException es) {
                es.printStackTrace();
            }
        }else {
            try {
                stmt.executeUpdate("update players set MCID = '" + name.toUpperCase() + "' where UUID = '" + uuid_ + "'");
                stmt.executeUpdate("update players set IN_TIME = '" + sdf.format(date) + "' where UUID = '" + uuid_ + "'");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public void setDiscordID(UUID uuid,String id){
        try {
            stmt.executeUpdate("update players set DISCORD_ID = '" + id + "' where UUID = '" + uuid.toString().replace("-", "") + "'");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
     public String getUuid(String name){
        String uuid = null;
        try {
            ResultSet rs = stmt.executeQuery("select * from players where MCID = '" + name.toUpperCase() + "'");
            while (rs.next()){
                uuid = rs.getString("UUID");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return uuid;
    }

    public String getDiscordID(String name){
        String id = null;
        try {
            ResultSet rs = stmt.executeQuery("select * from players where MCID = '" + name.toUpperCase() + "'");
            while (rs.next()){
                id = rs.getString("DISCORD_ID");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return id;
    }

    public String getInTime(String name){
        String time = null;
        try {
            ResultSet rs = stmt.executeQuery("select * from players where MCID = '" + name.toUpperCase() + "'");
            while (rs.next()){
                time = rs.getString("IN_TIME");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return time;
    }

    public boolean token(String name){

        String token = null;
        try {
            ResultSet rs = stmt.executeQuery("select * from players where MCID = '" + name.toUpperCase() + "'");
            while (rs.next()){
                token = rs.getString("DISCORD_ID");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if(token != null)
            return false;
        return true;
    }

}
