package com.company;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Simo on 16/04/2017.
 */
public class SubstitutionCipherAttack {



    public SubstitutionCipherAttack() {

    }

    public HashMap<Character,Character>[] findAllPossibleKeys(int KeySize)
    {

        String characterToPremute = "";
        int keyCounter = 0;
        for (int i = 97;i<123 && keyCounter < KeySize;i++,keyCounter++) {
            byte[] byteChar = {(byte) (0xff & i)};
            try {
                characterToPremute += new String(byteChar, "UTF-8");
            }
            catch (IOException ex)
            {
                System.out.print("String translation did not succeed");
                return null;
            }
        }
        for (int i = 65;i<91 && keyCounter < KeySize;i++,keyCounter++) {
            byte[] byteChar = {(byte) (0xff & i)};
            try {
                characterToPremute += new String(byteChar, "UTF-8");
            }
            catch (IOException ex)
            {
                System.out.print("String translation did not succeed");
                return null;
            }
        }
        perm1(characterToPremute);
        return null;
    }

    public  static void perm1(String s) { perm1("", s); }
    private static void perm1(String prefix, String s) {
        int n = s.length();
        if (n == 0) System.out.println(prefix);
        else {
            for (int i = 0; i < n; i++)
                perm1(prefix + s.charAt(i), s.substring(0, i) + s.substring(i+1, n));
        }

    }

}
