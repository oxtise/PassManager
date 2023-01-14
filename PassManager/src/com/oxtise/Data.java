package com.oxtise;

import javax.swing.plaf.nimbus.State;
import java.io.*;
import java.sql.*;


public class Data {
    Connection con;
    public String sql;
    Data() {
        System.out.println("Connessione al database in corso...");
        this.con = connection.DbConnection.connect();
    }
    public void readAllAccount(){ //Lista tutti gli account
        sql = "SELECT * FROM Account";
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                System.out.println("ID: "+rs.getInt("ID"));
                System.out.println("Title: "+Crypter.shiftDecrypt(rs.getString("title")));
                System.out.println("Username: "+Crypter.shiftDecrypt(rs.getString("username")));
                System.out.println("E-mail: "+Crypter.shiftDecrypt(rs.getString("email")));
                System.out.println("Password: "+Crypter.shiftDecrypt(rs.getString("pwd"))+"\n");
            }
            stmt.close();
            rs.close();
        }catch (SQLException e) {
            if(e.getMessage().contains("SQL error or missing database")){
                System.out.println(PassManager.ANSI_RED+"x Connessione non riuscita "+PassManager.ANSI_RESET);
            } else{
                e.printStackTrace();
            }
        }
    }
    public void addAccount(Account account){ //Aggiungi un account al database
        PreparedStatement ps = null;
        sql = "INSERT INTO Account ('title','username','email','pwd') VALUES ('"+ account.title+"','"+account.username+"','"+account.email+"', '"+account.pwd+"')";
        try {
            ps = con.prepareStatement(sql);
            ps.execute();
            System.out.println(PassManager.ANSI_GREEN+PassManager.ANSI_CHECKMARK+" Operazione riuscita, account aggiunto."+PassManager.ANSI_RESET);
        } catch (SQLException e) { e.printStackTrace(); }
    }
    public void readAccount_email(String email){ //Legge gli account con quella email
        sql = "SELECT * FROM Account WHERE email='"+email+"'";
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                System.out.println("ID: "+rs.getInt("ID"));
                System.out.println("Title: "+Crypter.shiftDecrypt(rs.getString("title")));
                System.out.println("Username: "+Crypter.shiftDecrypt(rs.getString("username")));
                System.out.println("E-mail: "+Crypter.shiftDecrypt(rs.getString("email")));
                System.out.println("Password: "+Crypter.shiftDecrypt(rs.getString("pwd")));
            }
            stmt.close();
            rs.close();
        }catch (SQLException e){e.printStackTrace();}
    }
    public void readAccount(String title) { //Leggi gli account con il titolo come parametro
        sql = "SELECT * FROM Account WHERE title='"+title+"'";
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                System.out.println("ID: "+rs.getInt("ID"));
                System.out.println("Title: "+Crypter.shiftDecrypt(rs.getString("title")));
                System.out.println("Username: "+Crypter.shiftDecrypt(rs.getString("username")));
                System.out.println("E-mail: "+Crypter.shiftDecrypt(rs.getString("email")));
                System.out.println("Password: "+Crypter.shiftDecrypt(rs.getString("pwd")));
            }
            stmt.close();
            rs.close();
        }catch (SQLException e){e.printStackTrace();}
    }
    public void readAccount(int id){ //Leggi un account in base all'id
        sql = "SELECT * FROM Account WHERE id = "+id;
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                System.out.println("ID: "+rs.getInt("ID"));
                System.out.println("Title: "+Crypter.shiftDecrypt(rs.getString("title")));
                System.out.println("Username: "+Crypter.shiftDecrypt(rs.getString("username")));
                System.out.println("E-mail: "+Crypter.shiftDecrypt(rs.getString("email")));
                System.out.println("Password: "+Crypter.shiftDecrypt(rs.getString("pwd")));
            }
            System.out.println("---------------");
            stmt.close();
            rs.close();
        }catch (SQLException e){e.printStackTrace();}
    }
    public void deleteAccount(int id){ //Elimina un account in base all'id
        sql = "DELETE FROM Account WHERE id = "+id;
        try {
            Statement stmt = con.createStatement();
            stmt.execute(sql);
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void updateAccount(int id, Account account) { //Aggiorna un account in base all'id
        sql = "UPDATE Account SET title='"+account.title+"', username='"+account.username+"', email='"+account.email+"', pwd='"+account.pwd+"' WHERE id = "+id;
        Statement stmt;
        try {
            stmt = con.createStatement();
            stmt.execute(sql);
            System.out.println(PassManager.ANSI_GREEN+PassManager.ANSI_CHECKMARK+" Account modificato correttamemte."+PassManager.ANSI_RESET);
        } catch (SQLException e) {
            System.out.println(PassManager.ANSI_RED+"x Errore. Account non modificato correttamente."+PassManager.ANSI_RESET);
            e.printStackTrace();
        }
    }
    public void closeConn() throws SQLException {
        con.close();
    }

    public void deleteAllAccount() { //Cancella tutti gli account della tabella
        sql = "DELETE FROM Account";
        Statement stmt;
        try {
            stmt = con.createStatement();
            stmt.execute(sql);
            System.out.println(PassManager.ANSI_REVERSED+"Tutti gli account sono stati eliminati con successo."+PassManager.ANSI_RESET);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
