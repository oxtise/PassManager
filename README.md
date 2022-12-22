# PassManager
PassManager is a simply Java no GUI offline password manager.
Having a password manager is one of the best way to remember <a href="https://www.malwarebytes.com/blog/news/2017/05/dont-need-27-different-passwords">credentials of every account</a>.
PassManager will preserve your accounts <a href="#Crypt & Decrypt">crypted</a> in a SQLite database so you have local access to db.<br>

## How to install
### Windows:
Download and decompress the zip file.
Open a terminal in the current dir and type:
```
javaw
```
### Linux:
Clone the repository with:
```
git clone https://github.com/oxtise/PassManager.git
```
or download and decompress the zip file.<br>
Change directory in PassManager:
```
cd PassManager
```
Execute PassManager.sh
```
./PassManager.sh
```
PassManager will ask you to create a mainkey the first time you execute it.
If you enter the correct mainkey, you will be able to see in clear account.
## Crypt & Decrypt
Mainkey is hashed with <a href="https://en.wikipedia.org/wiki/Secure_Hash_Algorithms">SHA-512</a>.
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
The encryption used in this project is the [Ceasar Shift Cypher](https://en.wikipedia.org/wiki/Caesar_cipher). This encryption shifts the characters of a String up by a certain number of characters in the [ASCII](https://www.ascii-code.com/) alphabet.

The decryption uses the same principle, but instead of shifting upwards, it shifts the encrypted String downwards by the same amount to regain the original String.
