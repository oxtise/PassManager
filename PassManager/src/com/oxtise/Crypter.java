package com.oxtise;

import java.security.MessageDigest;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

/* encrypt serve a dare l'hash della stringa data in input con l'algoritmo
* String algorithm
* SHA-512
* SHA-384
* SHA-256
* SHA-224
* SHA-1
* MD5
* MD2
*
*
* */
public class Crypter {
    public static String encrypt(String input, String algorithm)
    {
        try {
            // getInstance() method is called with algorithm SHA-512
            MessageDigest md = MessageDigest.getInstance(algorithm);

            // digest() method is called
            // to calculate message digest of the input string
            // returned as array of byte
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);

            // Add preceding 0s to make it 32 bit
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }

            // return the HashText
            return hashtext;
        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            System.out.println(e);
            return "error";
        }
    }
    /*The encryption used in this project is the Ceasar Shift Cypher.
    This encryption shifts the characters of a String up by a certain number of characters in the ASCII alphabet.*/
    public static String shiftEncrypt(String toBeEncrypted){
        char[] ch = toBeEncrypted.toCharArray(); //char array
        int i = 0;
        for(char c : ch){
            c += 10; //shift up by charShift
            ch[i] = c;
            i++;
        }
        String encrypted = new String(ch); //switch back to String
        return encrypted;
    }
    public static String shiftDecrypt(String toBeDecrypted){
        char[] ch = toBeDecrypted.toCharArray(); //char array
        int i = 0;
        for(char c : ch){
            c -= 10; //shift down by charShift
            ch[i] = c;
            i++;
        }
        String decrypted = new String(ch); //switch back to String
        return decrypted;
    }
    public static String generatePwd(){
        String alphabet = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        String simboliTrue,pwd = "";
        int alphabetLength;
        System.out.print("Imposta la lunghezza della password (6-16), 8 di default");
        PassManager.cursor();
        Scanner scan = new Scanner(System.in);
        int lunghezza_pwd = scan.nextInt();
        System.out.print("Simboli ammessi? Una password casuale con simboli sarà più sicura. (S/N)");
        PassManager.cursor();
        simboliTrue = scan.next().toLowerCase();
        if(simboliTrue.equals("s")){
            alphabet += "?!<>-*[]{}/";
        } else{}
        alphabetLength = alphabet.length();
        for (int i = 0;i<lunghezza_pwd;i++){
            pwd += alphabet.charAt((int) (Math.random()*alphabetLength));
        }
        System.out.print("Password generata con successo: "+pwd+"\n");
        return pwd;
    }
}
