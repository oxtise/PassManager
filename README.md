# PassManager
PassManager is a simply Java no GUI offline password manager.
Having a password manager is one of the best way to remember <a href="https://www.malwarebytes.com/blog/news/2017/05/dont-need-27-different-passwords">credentials of every account</a>.
PassManager will preserve your accounts <a href="#Crypt & Decrypt">crypted</a> in a SQLite database so you have local access to db.<br>

## How to install
### Windows:
Download and decompress the zip file.
Open command prompt in the current dir and type:
```bash
java -cp ./out/production/PassManager/;./lib/sqlite-jdbc-3.36.0.3.jar com.oxtise.PassManager
```
### Linux:
Clone the repository with:
```bash
git clone https://github.com/oxtise/PassManager.git
```
or download and decompress the zip file.<br>
Change directory and execute PassManager.sh
```bash
cd PassManager
```
```bash
./PassManager.sh
```
PassManager will ask you to create a mainkey the first time you execute it.
If you enter the correct mainkey, you will be able to see in clear account.
## Crypt & Decrypt
Mainkey is hashed in <a href="https://en.wikipedia.org/wiki/Secure_Hash_Algorithms">SHA-512</a> with this method: 
<br>
```java
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
```
<br>
The encryption used in this project to preserve accounts is the <a href="https://en.wikipedia.org/wiki/Caesar_cipher">Ceasar Shift Cypher</a>. This encryption shifts the characters of a String up by a certain number of characters in the <a href="https://www.ascii-code.com/">ASCII</a> alphabet.

```java
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
```

The decryption uses the same principle, but instead of shifting upwards, it shifts the encrypted String downwards by the same amount to regain the original String.
