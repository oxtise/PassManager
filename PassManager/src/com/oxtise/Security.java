package com.oxtise;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;

public class Security {
    public static boolean createBackup(LocalDate todaysDate){
        File directory = new File(PassManager.dirConfig+"backup/");
        //System.out.println(directory.getPath()+"/"+todaysDate);
        if(!directory.isDirectory()){
            directory.mkdir();
        }

            try (FileInputStream in = new FileInputStream(PassManager.file_config.getParent() + "/data.db"); //fonte
                 FileOutputStream out = new FileOutputStream(directory.getPath() + "/" + todaysDate+".db")) { //destinazione
                byte[] buffer = new byte[1024];
                int length;
                while ((length = in.read(buffer)) > 0)
                    out.write(buffer, 0, length);
out.close();
                return true;
            } catch (IOException io) {
                io.printStackTrace();
                return false;
            }

    }
    public static boolean restoreDatabase(String database_path) {
        File database = new File(database_path);
        if(database.exists()){
            System.out.println(PassManager.ANSI_GREEN+PassManager.ANSI_CHECKMARK+" Database trovato"+PassManager.ANSI_RESET);

            try (FileInputStream in=new FileInputStream(database_path); //fonte
                 FileOutputStream out=new FileOutputStream(PassManager.file_config.getParent()+"/data.db")){ //destinazione
                byte[] buffer = new byte[1024];
                int length;
                while ((length = in.read(buffer)) > 0)
                    out.write(buffer, 0, length);
                out.close();
                return true;
            }
            catch(IOException io)
            {   io.printStackTrace();
                return false;
            }
        } else{
            System.out.println(PassManager.ANSI_RED+"x Errore! Il database di backup non esiste."+PassManager.ANSI_RESET);
            return false;
        }
    }
}
