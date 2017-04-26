package com.company;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

import java.util.concurrent.ConcurrentHashMap;

public class Main {

    public static void main(String[] args) throws Exception
    {
/*
            ReadWrite RW = new ReadWrite();
            CBCmode CBC= new CBCmode(10);
            byte[] alltext=  RW.ReadTextbyte("C:\\Users\\Stav\\Desktop\\Alice.txt");
            HashMap<Character ,Character> KeyMap=RW.ReadKey("C:\\Users\\Stav\\Desktop\\key_short.txt");
            byte[] IV = RW.ReadTextbyte("C:\\Users\\Stav\\Desktop\\IV_short.txt");
            SubstitutionCipherED Encryption =new SubstitutionCipherED(KeyMap);
            byte[][] a =CBC.CBCEncryption(IV,alltext,Encryption);
            RW.WriteEncryptedText("C:\\Users\\Stav\\Desktop\\test1.txt",a);
            alltext=  RW.ReadTextbyte("C:\\Users\\Stav\\Desktop\\test1.txt");
            String en=CBC.CBCDecryption(IV,alltext,Encryption);
            RW.WriteText("C:\\Users\\Stav\\Desktop\\test2.txt",en);

*/
            //Cipher text only attack
        /*SubstitutionCipherAttack sbattack=new SubstitutionCipherAttack(CBC);
        HashMap<Character ,Character> ans = sbattack.CipherTextOnlyAttack(alltext,IV,0.1, 8,0.01);*/


        //Known plainText attack
/*        String IV = RW.ReadText("C:\\Users\\Simo\\Desktop\\PartC\\IV_long.txt");
        String Key = RW.ReadText("C:\\Users\\Simo\\Desktop\\PartC\\key_long.txt");
        String cipher = RW.ReadText("C:\\Users\\Simo\\Desktop\\PartC\\unknown_cipher1.txt");
        String KnownTextPlain = RW.ReadText("C:\\Users\\Simo\\Desktop\\PartC\\known_plain_long.txt");
        String KnownTextCipher = RW.ReadText("C:\\Users\\Simo\\Desktop\\PartC\\known_cipher.txt");
        CBCmode CBC= new CBCmode(8128);
        SubstitutionCipherAttack sbattack=new SubstitutionCipherAttack(CBC);
        HashMap<Character ,Character> partialKey = sbattack.GetKeyFromPlainAndCipher(KnownTextCipher,KnownTextPlain,IV);*/
        ReadWrite RW= new ReadWrite();
        CommandReader input=new CommandReader( args);
        byte[] IV=RW.ReadTextbyte(input.IVPath);
        //
        if(input.EA == EncryptionAlgorithm.sub_cbc_10 &&input.action==(Action.Encrypt))
        {
            CBCmode cbc=new CBCmode(10);
            byte[] plainText= RW.ReadTextbyte(input.textPath);
            SubstitutionCipherED Encryption=new SubstitutionCipherED(RW.ReadKey(input.keyPath));
            byte[][] CBCEncryption=  cbc.CBCEncryption(IV, plainText, Encryption);
            RW.WriteEncryptedText(input.outputFilePath,CBCEncryption);
        }
        if(input.EA== EncryptionAlgorithm.sub_cbc_10 &&input.action==(Action.Decrypt))
        {
            CBCmode cbc=new CBCmode(10);
            byte[] CipherText= RW.ReadTextbyte(input.textPath);
            SubstitutionCipherED Encryption=new SubstitutionCipherED(RW.ReadKey(input.keyPath));
            String CBCEncryption=  cbc.CBCDecryption(IV, CipherText, Encryption);
            RW.WriteText(input.outputFilePath, CBCEncryption);
        }
        if(input.EA== EncryptionAlgorithm.sub_cbc_10 &&input.action==(Action.Attack))
        {

            CBCmode cbc=new CBCmode(10);
            SubstitutionCipherAttack sAttack=new SubstitutionCipherAttack(cbc);
            byte[] CipherText= RW.ReadTextbyte(input.textPath);
            HashMap<Character,Character> key =sAttack.CipherTextOnlyAttack(CipherText,IV, 5000,8,0.01) ;
            RW.WriteKey(key,input.outputFilePath);
        }
        if(input.EA == EncryptionAlgorithm.sub_cbc_52 &&input.action==(Action.Encrypt))
        {
            CBCmode cbc=new CBCmode(8128);
            byte[] plainText= RW.ReadTextbyte(input.textPath);
            SubstitutionCipherED Encryption=new SubstitutionCipherED(RW.ReadKey(input.keyPath));
            byte[][] CBCEncryption=  cbc.CBCEncryption(IV, plainText, Encryption);
            RW.WriteEncryptedText(input.outputFilePath,CBCEncryption);
        }
        if(input.EA == EncryptionAlgorithm.sub_cbc_52 &&input.action==(Action.Decrypt))
        {
            CBCmode cbc=new CBCmode(8128);
            byte[] CipherText= RW.ReadTextbyte(input.textPath);
            SubstitutionCipherED Encryption=new SubstitutionCipherED(RW.ReadKey(input.keyPath));
            String CBCEncryption=  cbc.CBCDecryption(IV, CipherText, Encryption);
            RW.WriteText(input.outputFilePath, CBCEncryption);
        }
        if(input.EA == EncryptionAlgorithm.sub_cbc_52 &&input.action==(Action.Attack))
        {
            CBCmode cbc=new CBCmode(8128);
            byte[] cipher= RW.ReadTextbyte(input.textPath);
            byte[] nknowCipher= RW.ReadTextbyte(input.knownCiphertext);
            byte[] plainText= RW.ReadTextbyte(input.knownPlaintext);
            SubstitutionCipherAttack sAttack=new SubstitutionCipherAttack(cbc);
            Map<Character,Character> key= sAttack.KnownPlainTextAttack(cipher, nknowCipher,plainText,IV,1000);
            RW.WriteKey(key,input.outputFilePath);


        }
    }
}
