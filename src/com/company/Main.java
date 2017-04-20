package com.company;
import java.util.*;

import java.util.concurrent.ConcurrentHashMap;

public class Main {

    public static void main(String[] args)
    {
        // write your code here
        ReadWrite RW = new ReadWrite();
        String path = "C:\\Users\\Simo\\Desktop\\cipherMsg_example.txt";
        String alltext = RW.ReadText(path);
        HashMap<Character ,Character> KeyMap=new HashMap<Character, Character>();
        KeyMap=RW.ReadKey("C:\\Users\\Simo\\Desktop\\key_example.txt");
        String pathForIV = "C:\\Users\\Simo\\Desktop\\IV_example.txt";
        String IV = RW.ReadText(pathForIV);
        SubstitutionCipherED Encryption =new SubstitutionCipherED(KeyMap);
        CBCmode CBC= new CBCmode(10);
        String a =CBC.CBCDecryption(IV,alltext,Encryption);
        RW.WriteText("C:\\Users\\Simo\\Desktop\\test.txt",a);
        SubstitutionCipherAttack sbattack=new SubstitutionCipherAttack(CBC);
        KeyMap=sbattack.CipherTextOnlyAttack(alltext,IV,1, 8,0.05);
    }
}
