package com.company;
import java.util.Map;

import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        // write your code here
        ReadWrite RW = new ReadWrite();
        String path = "C:\\Users\\Stav\\Desktop\\plainMsg_example.txt";
        String alltext = RW.ReadText(path);

        HashMap<Character ,Character> KeyMap=new HashMap<Character, Character>();

        KeyMap=RW.ReadKey("C:\\Users\\Stav\\Desktop\\key_example.txt");

        String pathForIV = "C:\\Users\\Stav\\Desktop\\IV_example.txt";
        String IV = RW.ReadText(pathForIV);

        SubstitutionCipherED sc= new SubstitutionCipherED(KeyMap);
        int blockSIZE=10;
        CBCmode cbc= new CBCmode(blockSIZE,sc);
        String test = cbc.CBCEncryption(IV,alltext);
        System.out.print(test);
        String origonal=cbc.CBCDecryption(IV,test);
        RW.WriteText( "C:\\Users\\Stav\\Desktop\\YA.txt",origonal);
    }
}
