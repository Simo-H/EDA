package com.company;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Simo on 16/04/2017.
 */
public class SubstitutionCipherAttack {

    Set EnglishWordsSet;
    CBCmode cbc;
    public SubstitutionCipherAttack(CBCmode CBC) {
        EnglishWordsSet = ConcurrentHashMap.newKeySet();
        ReadWrite RW = new ReadWrite();
        String test = RW.ReadText("C:\\Users\\Simo\\Desktop\\words.txt");
        String[] test2 = test.split("\n");
        for(String x:test2)
        {
            EnglishWordsSet.add(x);
        }
        cbc=CBC;
    }

    public ArrayList<HashMap<Character,Character>> findAllPossibleKeys(int KeySize)
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
        ArrayList<String> AllPermutaionStrings = new ArrayList<>();
        perm1(characterToPremute,AllPermutaionStrings);
        return AllPossibleKeys(AllPermutaionStrings,characterToPremute);
    }

    public   void perm1(String s, ArrayList<String> a) { perm1("", s,a); }
    private  void perm1(String prefix, String s,ArrayList<String> outPut) {
        int n = s.length();
        if (n == 0) {
            outPut.add(prefix);
        }
        else {
            for (int i = 0; i < n; i++)
                perm1(prefix + s.charAt(i), s.substring(0, i) + s.substring(i+1, n),outPut);
        }

    }

    private HashMap<Character,Character> StringToHashMap(String singlePermutaion,String CharactersHashKeys)
    {
        HashMap<Character,Character> cipherKey = new HashMap<>();
        char[] KeysCharacters = CharactersHashKeys.toCharArray();
        char[] ValueCharacters = singlePermutaion.toCharArray();
        for (int i = 0;i<KeysCharacters.length;i++)
        {
            cipherKey.put(KeysCharacters[i],ValueCharacters[i]);
        }
        return  cipherKey;
    }
    private ArrayList<HashMap<Character,Character>> AllPossibleKeys(ArrayList<String> AllPermutaionStrings,String CharactersHashKeys)
    {
        ArrayList<HashMap<Character,Character>> AllPossibleKeys = new ArrayList<HashMap<Character,Character>>();
        for(String x:AllPermutaionStrings)
        {
            AllPossibleKeys.add(StringToHashMap(x,CharactersHashKeys));
        }
        return AllPossibleKeys;
    }
    private byte[] SectionOfCiphertext (byte[] textCipher, double Percent )
    {
        int lengthtext= (int)(textCipher.length*Percent) ;
        return  Arrays.copyOfRange(textCipher,0,lengthtext);
    }

    public   HashMap<Character,Character> CipherTextOnlyAttack(byte[] textCipher,byte[] IV,double PercentCheck,int KeySize,double minimumNumberOfNonEnglishWords)
    {
        ArrayList<HashMap<Character,Character>> findAllPossibleKeys=findAllPossibleKeys(KeySize);
        byte[] SubCipher= SectionOfCiphertext (textCipher, PercentCheck);

        //paralell
        for (int i=0; i<findAllPossibleKeys.size();i++)
        {
            String decryptedText= cbc.CBCDecryption(IV,SubCipher,new SubstitutionCipherED(findAllPossibleKeys.get(i)));
            if (CheckKeyReturnsEnglish(decryptedText,minimumNumberOfNonEnglishWords))
            {
                return findAllPossibleKeys.get(i);
            }
        }
        return null;
    }
    public boolean CheckKeyReturnsEnglish(String decryptedText,double minimumNumberOfNonEnglishWords)
    {
        decryptedText = decryptedText.toLowerCase();
        String[] decryptedTextSaperatedBySpace = decryptedText.split("[\\]\\[0123546789,'+=_\\;:{}/<>|)(?!@#$%^&*\\-. \r\n\"]");
        ArrayList<String> DecryptedWordsArray = new ArrayList<String>(Arrays.asList(decryptedTextSaperatedBySpace));
        DecryptedWordsArray.removeAll(Arrays.asList(""));
        DecryptedWordsArray.removeAll(EnglishWordsSet);
        if ((double)DecryptedWordsArray.size()/decryptedTextSaperatedBySpace.length < minimumNumberOfNonEnglishWords)
            return true;
        return false;
    }

    public HashMap<Character,Character> GetKeyFromPlainAndCipher(String cipher, String plainText,String IV)
    {
        byte[] IVByte= cbc.StringToUtf8(IV);
        byte[] plaintextByte = cbc.StringToUtf8(plainText);
        byte[] xor= cbc.xor( plaintextByte,IVByte) ;
        String ToBeEncrypted=cbc.Uf8ToString(xor);
        HashMap<Character,Character> partialKey = new HashMap<Character,Character>();
        for (int i=0;i<ToBeEncrypted.length();i++)
        {
            byte a = xor[i];
            //byte[] byteChar = {(byte) (0xff & i)};
            String b = cbc.Uf8ToString(new byte[]{a});
            //if ((xor[i]<(0xff & 123) && xor[i]>(0xff & 97)) || (xor[i]<(0xff & 91) && xor[i]>(0xff & 65)))
            if ((Character.getType(ToBeEncrypted.charAt(i)) == Character.UPPERCASE_LETTER ||Character.getType(ToBeEncrypted.charAt(i)) == Character.LOWERCASE_LETTER) && ToBeEncrypted.charAt(i) != 'Ҥ')
            {
                partialKey.put(ToBeEncrypted.charAt(i),cipher.charAt(i));
            }
            /*if (Character.isLetter(ToBeEncrypted.charAt(i)))
            {
                partialKey.put(ToBeEncrypted.charAt(i),cipher.charAt(i));
            }*/
        }
        return partialKey;
    }
}
