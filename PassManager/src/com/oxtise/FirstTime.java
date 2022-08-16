package com.oxtise;

import connection.DbConnection;

import java.io.*;
import java.util.Scanner;

public class FirstTime {

    FirstTime(){
        if(!PassManager.actualpath.exists()){ //Se il file di configurazione non esiste nella cartella predefinita
            configuration();
        } else{
            try {
                FileReader frreader = new FileReader(PassManager.actualpath); //Legge dal file della cartella attuale
                BufferedReader bfreader = new BufferedReader(frreader);
                PassManager.dirConfig= bfreader.readLine();
                bfreader.close();
                frreader.close();
                PassManager.file_config = new File(PassManager.dirConfig+"config");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void configuration(){
        try {
            Scanner scan4 = new Scanner(System.in);
            System.out.println("Questa è la tua prima volta,\nInserisci il percorso di installazione (default: "+PassManager.dirConfig+")");
            PassManager.cursor();
            String path = scan4.nextLine();
            if(!path.equals("")){
                if(path.charAt(path.length()-1)!='/'){
                    path += "/";
                }
                PassManager.dirConfig = path;
            }

            PassManager.actualpath.createNewFile();
            FileWriter fwriter = new FileWriter(PassManager.actualpath);
            fwriter.write(PassManager.dirConfig);
            fwriter.close();
            PassManager.file_config = new File(PassManager.dirConfig+"config");
            File directory = new File(PassManager.dirConfig); //directory del programma dal filepath config
            System.out.print("Inserisci il tuo nome: "+PassManager.ANSI_CYAN);
            Scanner scan = new Scanner(System.in);
            String name = scan.next();
            name = name.substring(0,1).toUpperCase()+name.substring(1).toLowerCase();
            Console console1 = System.console();
            System.out.println(PassManager.ANSI_RESET+"Avrai bisogno di una mainkey, una password unica che servira a sbloccare gli account e vederne le password.");
            String pwd,pwd_conf;
            boolean corrisponde;
            do {
                System.out.print("Inserisci la mainkey: ");
                pwd = String.valueOf(console1.readPassword());
                System.out.print("Conferma password: ");
                pwd_conf = String.valueOf(console1.readPassword());
                corrisponde = pwd.equals(pwd_conf);
                if(!corrisponde)
                    System.out.println("Password diverse. Riprova.");
            }while(!corrisponde);
            Crypter crypter = new Crypter();
            pwd = crypter.encrypt(pwd,"SHA-512");//crypta la password con SHA-512
            System.out.println(PassManager.ANSI_GREEN+PassManager.ANSI_CHECKMARK+" Password cifrata: \n"+PassManager.ANSI_RESET+pwd+"\n");

            directory.mkdir();
            PassManager.file_config.createNewFile(); //Se tutto ok crea il file config
            FileWriter writer = new FileWriter(PassManager.file_config);
            BufferedWriter bfwriter = new BufferedWriter(writer);
            bfwriter.write(name+"\n"+pwd); //Salva il nome, la pwd cifrata e la directory del file di backup
            bfwriter.close();

            System.out.println(PassManager.dirConfig);
            File database = new File(PassManager.dirConfig+"data.db");
            if(!database.exists()) {
                System.out.println("Nessun database trovato. Hai un file di backup?\n[1] Si\n[2] No, crea un nuovo database.");
                PassManager.cursor();
                int backup = Integer.parseInt(scan.next());
                if(backup==1){
                    System.out.println("Inserisci il percorso del database");
                    PassManager.cursor();
                    Scanner scan2 = new Scanner(System.in);
                    String database_path = scan2.nextLine();
                    if(Security.restoreDatabase(database_path)){
                        System.out.println(PassManager.ANSI_GREEN+PassManager.ANSI_CHECKMARK+" Backup ripristinato con successo\n"+PassManager.ANSI_RESET);
                    } else{
                        System.out.println(PassManager.ANSI_RED+"x Ripristino del backup fallito.\n"+PassManager.ANSI_RESET);
                    }
                } else if(backup==2){
                    DbConnection.createTable();
                }
            } else{
                DbConnection.createTable();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}
