package com.company;

import java.io.IOException;
import java.util.*;
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
    private byte[] SectionOfCiphertext (byte[] textCipher, int numberOfChar )
    {
        //int lengthtext= (int)(textCipher.length*Percent) ;
        if (textCipher.length < numberOfChar)
            return  textCipher;
        return  Arrays.copyOfRange(textCipher,0,numberOfChar);
    }

    public   HashMap<Character,Character> CipherTextOnlyAttack(byte[] textCipher,byte[] IV,int numberOfChar,int KeySize,double minimumNumberOfNonEnglishWords)
    {
        ArrayList<HashMap<Character,Character>> findAllPossibleKeys=findAllPossibleKeys(KeySize);
        byte[] SubCipher= SectionOfCiphertext (textCipher, numberOfChar);
        ArrayList<HashMap<Character,Character>> keys = new ArrayList<>();
        //findAllPossibleKeys.parallelStream().forEach();
        //findAllPossibleKeys.parallelStream().filter(s->CheckKeyReturnsEnglish(cbc.CBCDecryption(IV,SubCipher,new SubstitutionCipherED(s)),minimumNumberOfNonEnglishWords)).forEach(p->keys.add(p));//.//findFirst().ifPresent(s->keys.add(s));//.forEach(p->keys.add(p));
        //paralell
        int best = Integer.MAX_VALUE;
        int bestPosition = 0;
        for (int i=0; i<findAllPossibleKeys.size();i++)
        {
            String decryptedText= cbc.CBCDecryption(IV,SubCipher,new SubstitutionCipherED(findAllPossibleKeys.get(i)));
            int nonEnglishWords = CheckKeyReturnsEnglish(decryptedText);
            if (best>nonEnglishWords)
            {
                best = nonEnglishWords;
                bestPosition = i;
            }
        }

        return findAllPossibleKeys.get(bestPosition);
    }
    public int CheckKeyReturnsEnglish(String decryptedText)
    {
        decryptedText = decryptedText.toLowerCase();
        String[] decryptedTextSaperatedBySpace = decryptedText.split("[\\]\\[0123546789,'+=_\\;:{}/<>|)(?!@#$%^&*\\-. \r\n\"]");
        ArrayList<String> DecryptedWordsArray = new ArrayList<String>(Arrays.asList(decryptedTextSaperatedBySpace));
        DecryptedWordsArray.removeAll(Arrays.asList(""));
        DecryptedWordsArray.removeAll(EnglishWordsSet);
        return DecryptedWordsArray.size();
    }

    public HashMap<Character,Character> GetKeyFromPlainAndCipher(byte[] cipher, byte[] plainText,byte[] IV)
    {
        byte[] xor= cbc.xor( plainText,IV) ;
        HashMap<Character,Character> partialKey = new HashMap<Character,Character>();
        for (int i=0;i<plainText.length;i++)
        {
            byte[] a = new byte[]{xor[i]};
            byte[] b = new byte[]{cipher[i]};

            if ((xor[i]<(0xff & 123) && xor[i]>(0xff & 96)) || (xor[i]<(0xff & 91) && xor[i]>(0xff & 64)))
//            if ((Character.getType(ToBeEncrypted.charAt(i)) == Character.UPPERCASE_LETTER ||Character.getType(ToBeEncrypted.charAt(i)) == Character.LOWERCASE_LETTER) && ToBeEncrypted.charAt(i) != 'Ҥ')
            {
                partialKey.put(cbc.Uf8ToString(a).charAt(0),cbc.Uf8ToString(b).charAt(0));
            }
            /*if (Character.isLetter(ToBeEncrypted.charAt(i)))
            {
                partialKey.put(ToBeEncrypted.charAt(i),cipher.charAt(i));
            }*/
        }
        return partialKey;
    }

    public ArrayList<HashMap<Character,Character>> GetKeyReminder(HashMap<Character,Character> partialKey)
    {
        String aZKey = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        //String aZValue = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String reminderKey = "";
        String reminderValue = "";
        for (int i=0;i<52;i++)
        {
            if (!partialKey.keySet().contains(aZKey.charAt(i)))
                reminderKey+=aZKey.charAt(i);
        }
        for (int i=0;i<52;i++)
        {
            if (!partialKey.keySet().contains(aZKey.charAt(i)))
                reminderValue+=aZKey.charAt(i);
        }
        ArrayList<String> AllPermutaionStrings = new ArrayList<>();
        perm1(reminderValue,AllPermutaionStrings);
        return AllPossibleKeys(AllPermutaionStrings,reminderKey);
    }

    public Map<Character,Character> KnownPlainTextAttack(byte[] cipher,byte[] knownCipher, byte[] plainText,byte[] IV,int numberOfChar) {
        HashMap<Character, Character> KeyFromNownPlainAndCipher = GetKeyFromPlainAndCipher(knownCipher, plainText, IV);
        SubstitutionCipherED SCED = new SubstitutionCipherED(KeyFromNownPlainAndCipher);
        byte[] cipherDecryptWithPartialKay = SCED.Decrypt(cipher);
        ArrayList<HashMap<Character, Character>> KeysFromNownPlainAndCipher = GetKeyReminder(KeyFromNownPlainAndCipher);
        int best = Integer.MAX_VALUE;
        int bestPosition = 0;
        byte[] SectionOfCiphertext = SectionOfCiphertext(cipher, numberOfChar);
        for (int i = 0; i < KeysFromNownPlainAndCipher.size(); i++) {
            String decryptedText = cbc.CBCDecryption(IV, SectionOfCiphertext, new SubstitutionCipherED(KeysFromNownPlainAndCipher.get(i)));
            int nonEnglishWords = CheckKeyReturnsEnglish(decryptedText);
            if (best > nonEnglishWords) {
                best = nonEnglishWords;
                bestPosition = i;
            }
        }
        return joinKeys(KeysFromNownPlainAndCipher.get(bestPosition),KeyFromNownPlainAndCipher);
    }
        //byte[] SubCipher= SectionOfCiphertext (cipherDecryptWithPartialKay, PercentCheck);

        //ArrayList<HashMap<Character,Character>> keys = new ArrayList<>();
        //KeysFromNownPlainAndCipher.parallelStream().filter(s->CheckKeyReturnsEnglish(cbc.CBCDecryption(IV,SubCipher,new SubstitutionCipherED(s)),minimumNumberOfNonEnglishWords)).findFirst().ifPresent(s->keys.add(s));//.forEach(p->keys.add(p));
        public Map<Character,Character> joinKeys(HashMap<Character,Character> keyA,HashMap<Character,Character> keyB )
        {
            keyA.putAll(keyB);
            return new TreeMap<Character, Character>(keyA);
        }
    }

