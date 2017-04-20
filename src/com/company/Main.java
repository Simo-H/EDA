package com.company;
import java.util.*;

import java.util.concurrent.ConcurrentHashMap;

public class Main {

    public static void main(String[] args)
    {
        // write your code here
        ReadWrite RW = new ReadWrite();
        String path = "C:\\Users\\Stav\\Desktop\\cipherMsg_example.txt";
        String alltext = RW.ReadText(path);

        HashMap<Character ,Character> KeyMap=new HashMap<Character, Character>();

        KeyMap=RW.ReadKey("C:\\Users\\Stav\\Desktop\\key_example.txt");

        String pathForIV = "C:\\Users\\Stav\\Desktop\\IV_example.txt";
        String IV = RW.ReadText(pathForIV);
        SubstitutionCipherED Encryption =new SubstitutionCipherED(KeyMap);
        CBCmode CBC= new CBCmode(10);
      // RW.WriteText("C:\\Users\\Stav\\Desktop\\test.txt",CBC.CBCDecryption(IV,alltext,));

       // ArrayList<HashMap<Character,Character>> a = sa.findAllPossibleKeys(8);
        //ConcurrentHashMap<String,String> b = new ConcurrentHashMap<String,String>();
        //Set r = ConcurrentHashMap.newKeySet();
       // String test = RW.ReadText("C:\\Users\\Simo\\Desktop\\words.txt");

        SubstitutionCipherAttack sbattack=new SubstitutionCipherAttack(CBC);
        KeyMap=sbattack.CipherTextOnlyAttack(alltext,IV,1, 10,0.1);
    }
}
