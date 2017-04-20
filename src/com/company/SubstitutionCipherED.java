package com.company;

import java.util.HashMap;

/**
 * Created by Simo on 15/04/2017.
 */
public class SubstitutionCipherED {

    HashMap<Character,Character> key;

    public SubstitutionCipherED(HashMap<Character, Character> key) {
        this.key = key;
    }

    public String Encrypt(String plainText)
    {
        char[] cipherText = plainText.toCharArray();
        for (int i = 0;i<cipherText.length;i++)
        {
            if (key.containsKey(cipherText[i]))
            {
                cipherText[i] = key.get(cipherText[i]);
            }
        }
        return new String(cipherText);
    }
    public String Decrypt(String cipherText)
    {
        HashMap<Character, Character> reverseKey = new HashMap<Character, Character>();
        for(HashMap.Entry<Character, Character> entry : key.entrySet()){
            reverseKey.put(entry.getValue(), entry.getKey());
        }
        char[] plainText = cipherText.toCharArray();
        for (int i = 0;i<plainText.length;i++)
        {
            if (reverseKey.containsKey(plainText[i]))
            {
                plainText[i] = reverseKey.get(plainText[i]);
            }
        }
        return new String(plainText);
    }
}
