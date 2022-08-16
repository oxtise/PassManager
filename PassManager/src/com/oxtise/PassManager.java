package com.oxtise;

import java.io.*;
import java.time.LocalDate;
import java.util.Scanner;
import java.sql.SQLException;

public class PassManager {
    public static String dirConfig = System.getProperty("user.home")+"/Documenti/PassManager/";
    public static File file_config;
    public static File actualpath = new File(System.getProperty("user.dir")+"/path"); //File sulla cartella del programma per sapere dove si trova il file config
    public static String name;
    public static String hash;
    /*ansi code text color*/
    public static final String ANSI_RESET = "\u001B[0m"; //resetta il colore originale
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_BOLD = "\033[0;1m";
    public static final String ANSI_REVERSED = "\u001b[7m";
    public static final String ANSI_CHECKMARK = "\u2713";

    public static void main(String[] args) {
        System.out.println(ANSI_BLUE+ANSI_REVERSED+"PassManager"+ANSI_RESET+"\n-----------------");
        FirstTime check = new FirstTime(); //Crea l'oggetto che controlla se il programma è mai stato aperto
        BufferedReader reader;
        String pwd_hashed;
        boolean access = false; //da impostare su falso per chiedere la password all'inizio
        try {
            reader = new BufferedReader(new FileReader(file_config)); //Leggo dal file di configurazione nome e hash della mainkey
            name = reader.readLine(); //la prima riga del file config è il nome
            hash = reader.readLine(); //hash SHA512 della mainkey
            reader.close();
        } catch (IOException e) {   e.printStackTrace();}
        Console console = System.console();
        System.out.println("Bentornato "+ANSI_BLUE+name+ANSI_RESET+".");
        while(!access) { //Login con la mainkey
            System.out.println("Inserisci la mainkey: ");
            pwd_hashed = Crypter.encrypt(String.valueOf(console.readPassword()), "SHA-512"); //ottengo l'hash della pwd inserita
            access = pwd_hashed.equals(hash); //confronto l'hash della pwd inserita con quella del file di configurazione
            if(!access) System.out.println(ANSI_BOLD+ANSI_RED+"Password errata."+ANSI_RESET+" Riprova.");
        }
        System.out.print(ANSI_GREEN+"Accesso effettuato con successo. "+ANSI_CHECKMARK+ANSI_RESET+"\n---------------------------\n");
        mainMenu(); //lo mando al menu principale
    }
    private static void mainMenu(){
        System.out.println("\nMenu principale: ");
        System.out.println("[1] Stampa tutti gli account");
        System.out.println("[2] Stampa gli account di un sito specifico");
        System.out.println("[3] Aggiungi un account");
        System.out.println("[4] Modifica o elimina un account");
        System.out.println("[5] Crea un backup dei dati");
        System.out.println("[6] Ripristina un backup");
        System.out.println("[99] Elimina tutti gli account");
        System.out.println("[00] Chiudi il programma");
        cursor();
        Scanner scan = new Scanner(System.in);
        int choice = 0;
        try {
            choice = Integer.parseInt(scan.nextLine());
        }catch (NumberFormatException e){
            System.out.println("Formato errato. Inserire un numero tra le opzioni del menu.");
            mainMenu();
        }
        Data data = new Data();
        String title,username,email,pwd;
        switch (choice){
            case 1: //MENU PRINCIPALE: Stampa tutti gli account
                data.readAllAccount();
                break;
            case 2: //MENU PRINCIPALE: Stampa gli account di un sito specifico
                System.out.print("-------------------\n");
                System.out.println("Inserisci il titolo del sito.");
                cursor();
                title = scan.nextLine();
                data.readAccount(Crypter.shiftEncrypt(title.toLowerCase()));
                break;
            case 3: //MENU PRINCIPALE: aggiungi un account
                System.out.println("-------------------");
                System.out.print("Titolo del sito: ");
                title = scan.nextLine();
                System.out.print("Username: ");
                username = scan.nextLine();
                System.out.print("E-mail: ");
                email = scan.nextLine();
                System.out.print("Password (lascia in bianco se vuoi generarla casualmente): ");
                pwd = scan.nextLine();
                if(pwd.equals(""))
                    pwd = Crypter.generatePwd();

                Account account = new Account(Crypter.shiftEncrypt(title).toLowerCase(),Crypter.shiftEncrypt(username),Crypter.shiftEncrypt(email),Crypter.shiftEncrypt(pwd));
                data.addAccount(account);//aggiungi account con dati criptati al database
                break;
            case 4://MENU PRINCIPALE: modifica o elimina un account
                System.out.println("-------------------");
                System.out.print("Inserisci ID dell'account da modificare: \n");
                System.out.println("Se non lo sai, lascia in bianco e seleziona 2 dal menu principale.");
                cursor();
                int id = Integer.parseInt(scan.nextLine());
                if(id==0){ //se l'utente non sa l'id
                    mainMenu();
                } else{
                    data.readAccount(id);
                    System.out.println("Vuoi modificare o eliminare l'acccount?\n[1] Modifica\n[2] Elimina");
                    cursor();

                    int choice2 = Integer.parseInt(scan.nextLine());
                    switch (choice2){
                        case 1: //modifica account
                            //System.out.print("Inserisci solo i campi da modificare, altrimenti lascia vuoto.\n");
                            System.out.print("---------------\n");
                            System.out.print("Titolo del sito: ");
                            title = scan.nextLine();
                            System.out.print("Username: ");
                            username = scan.nextLine();
                            System.out.print("E-mail: ");
                            email = scan.nextLine();
                            System.out.print("Password: ");
                            pwd = scan.nextLine();

                            Account account2 = new Account(Crypter.shiftEncrypt(title).toLowerCase(),Crypter.shiftEncrypt(username),Crypter.shiftEncrypt(email),Crypter.shiftEncrypt(pwd));
                            data.updateAccount(id,account2);
                            break;
                        case 2: //elimina account
                            System.out.print("Sei sicuro di voler eliminare l'account seguente (S/N)?\n");
                            char choice3 = scan.nextLine().toLowerCase().charAt(0);
                            if(choice3=='s') {
                                data.deleteAccount(id);
                                System.out.println(ANSI_GREEN+ANSI_CHECKMARK+" Account eliminato correttamente."+ANSI_RESET);
                            } else{
                                System.out.println(ANSI_RED+"x Account non eliminato."+ANSI_RESET);
                            }
                            break;
                        default:
                            System.out.println("Opzione non in lista. ");
                    }
                }
                break;
            case 5: //MENU PRINCIPALE: Crea un backup dei dati
                System.out.println("Creazione backup in corso...");
                LocalDate todaysdate = LocalDate.now();
                if(Security.createBackup(todaysdate)){
                    System.out.println(ANSI_GREEN+ANSI_CHECKMARK+" Backup creato con successo in "+ANSI_BLUE+dirConfig+"backup/"+todaysdate+".db"+ANSI_RESET);
                } else{
                    System.out.println(ANSI_RED+"x Errore, creazione del backup fallita: "+ANSI_RESET);
                }
                break;
            case 6: //MENU PRINCIPALE: Ripristina un backup
                System.out.println("Inserisci il percorso del database");
                PassManager.cursor();
                String database_path = scan.next();
                if(Security.restoreDatabase(database_path)){
                    System.out.println(ANSI_GREEN+ANSI_CHECKMARK+" Backup ripristinato con successo"+ANSI_RESET);
                } else{
                    System.out.println(ANSI_RED+"x Errore, ripristino del backup fallito."+ANSI_RESET);
                }
                break;
            case 99: //MENU PRINCIPALE: ELIMINA TUTTI GLI ACCOUNT
                System.out.println("ELIMINARE TUTTI GLI ACCOUNT SALVATI? (S/N)");
                cursor();
                char choice4 = scan.nextLine().toLowerCase().charAt(0);
                switch (choice4){
                    case 'n': //No
                        System.out.println("Ok, ritorno al menu principale.");
                        break;
                    case 's': //Si
                        System.out.println("Inserisci la mainkey per continuare: ");
                        Console console = System.console();
                        String pwd_hashed = Crypter.encrypt(String.valueOf(console.readPassword()),"SHA-512");
                        if(hash.equals(pwd_hashed)){ //se la pwd inserita equivale alla mainkey
                            System.out.println("ATTENZIONE! Gli account non potranno essere ripristinati");
                            System.out.println("Sei sicuro di voler continuare?\nInserisci 'delete' per confermare, o 'exit' per annullare");
                            cursor();
                            String confirm = scan.nextLine();
                            if(confirm.equals("delete")){
                                data.deleteAllAccount();
                            } else if(confirm.equals("exit")){
                                System.out.println("Uscita in corso");
                            } else{
                                System.out.println("Opzione non in lista");
                            }
                        } else{
                            System.out.println(ANSI_RED+"x Password errata."+ANSI_RESET);
                        }
                        break;
                    default:
                        System.out.println("Opzione non in lista. Inserisci (S/N).");
                        break;
                }
                break;
            case 0: //MENU PRINCIPALE: termina il programma
                exitProgram(data);
            default:
                System.out.println("Devi scegliere tra le opzioni del menu (1-4)");
        }
        System.out.println("-------------------");
        mainMenu();

    }

    public static void cursor(){
        System.out.print(ANSI_CYAN+">>>"+ANSI_RESET);
    }
    public static void exitProgram(Data data){
        try {
            data.closeConn();
            //System.out.println(ANSI_GREEN+ANSI_CHECKMARK+" ");
        } catch (SQLException e) { e.printStackTrace();}
        System.out.println("Chiusura in corso...");
        System.exit(0);
    }
}