package com.company;
import java.util.Map;

import java.util.HashMap;

public class Main {

    public static void main(String[] args) {

        HashMap<Character,Character> key = new HashMap<Character,Character>();
        key.put('a','b');
        key.put('b','a');
        String text = "aaabbb";
        SubstitutionCipherED sub = new SubstitutionCipherED(key);
        String cipher = sub.Decrypt(text);
        System.out.print(cipher);
    }
}
