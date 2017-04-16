package com.company;
import java.util.Map;

import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        // write your code here
        ReadWrite RW = new ReadWrite();
        String path = "C:\\Users\\Simo\\Desktop\\plainMsg_example.txt";
        String alltext = RW.ReadText(path);

        HashMap<Character ,Character> KeyMap=new HashMap<Character, Character>();

        KeyMap=RW.ReadKey("C:\\Users\\Simo\\Desktop\\key_example.txt");

        String pathForIV = "C:\\Users\\Simo\\Desktop\\IV_example.txt";
        String IV = RW.ReadText(pathForIV);

        SubstitutionCipherAttack sa = new SubstitutionCipherAttack();
        sa.findAllPossibleKeys(8);
    }
}
