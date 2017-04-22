package com.company;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

import java.util.concurrent.ConcurrentHashMap;

public class Main {

    public static void main(String[] args) throws Exception
    {
        // Decryption
        ReadWrite RW = new ReadWrite();
        //CBCmode CBC= new CBCmode(10);
/*        byte[] alltext=  RW.ReadTextbyte("C:\\Users\\Simo\\Desktop\\Security Tests\\examples_ascii\\PartB\\cipher.txt");
        HashMap<Character ,Character> KeyMap=RW.ReadKey("C:\\Users\\Simo\\Desktop\\Security Tests\\examples_ascii\\PartB\\key_short.txt");
        byte[] IV = RW.ReadTextbyte("C:\\Users\\Simo\\Desktop\\Security Tests\\examples_ascii\\PartB\\IV_short.txt");
        SubstitutionCipherED Encryption =new SubstitutionCipherED(KeyMap);*/
        //String a =CBC.CBCDecryption(IV,alltext,Encryption);
        //RW.WriteText("C:\\Users\\Simo\\Desktop\\test.txt",a);

        //Cipher text only attack
/*        SubstitutionCipherAttack sbattack=new SubstitutionCipherAttack(CBC);
        HashMap<Character ,Character> ans = sbattack.CipherTextOnlyAttack(alltext,IV,0.1, 8,0.01);*/

        //Known plainText attack
        byte[] IV = RW.ReadTextbyte("C:\\Users\\Simo\\Desktop\\PartC\\IV_long.txt");
        //HashMap<Character ,Character> Key = RW.ReadText("C:\\Users\\Simo\\Desktop\\PartC\\key_long.txt");
        byte[] cipher = RW.ReadTextbyte("C:\\Users\\Simo\\Desktop\\PartC\\unknown_cipher1.txt");
        byte[] KnownTextPlain = RW.ReadTextbyte("C:\\Users\\Simo\\Desktop\\PartC\\known_plain_long.txt");
        byte[] KnownTextCipher = RW.ReadTextbyte("C:\\Users\\Simo\\Desktop\\PartC\\known_cipher.txt");
        CBCmode CBC= new CBCmode(8128);
        SubstitutionCipherAttack sbattack=new SubstitutionCipherAttack(CBC);
        HashMap<Character ,Character> partialKey = sbattack.GetKeyFromPlainAndCipher(KnownTextCipher,KnownTextPlain,IV);
        RW.WriteKey(partialKey,"C:\\Users\\Simo\\Desktop\\key.txt");
    }
}
