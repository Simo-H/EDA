package com.company;

import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.io.File;
import java.nio.file.Files;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by Simo on 16/04/2017.
 */
public class SubstitutionCipherAttack {

    Set EnglishWordsSet;
    CBCmode cbc;
    public SubstitutionCipherAttack(CBCmode CBC)  throws IOException{
        EnglishWordsSet = ConcurrentHashMap.newKeySet();
        String fileName = "words.txt";
        InputStream in = getClass().getResourceAsStream("/words.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line =  reader.lines().collect(Collectors.joining("\n"));
            String[] test2 = line.split("\n");
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

    public   HashMap<Character,Character> CipherTextOnlyAttack(byte[] textCipher,byte[] IV,int numberOfChar,int KeySize)
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
            String decryptedText= cbc.CBCDecryption(IV,SubCipher,new SubstitutionCipherED(findAllPossibleKeys.get(i)),false);
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
        //String[] decryptedTextSaperatedBySpace = decryptedText.split("[\\]\\[0123546789,'+=_\\;:{}/<>|)(?!@#$%^&*\\-. \r\n\"]");
        String[] decryptedTextSaperatedBySpace = decryptedText.split("[^A-Za-z]");
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

    public Map<Character,Character> KnownPlainTextAttack(byte[] cipher,byte[] knownCipher, byte[] plainText,byte[] IV)
    {
        HashMap<Character, Character> KeyFromNownPlainAndCipher = GetKeyFromPlainAndCipher(knownCipher, plainText, IV);
        SubstitutionCipherED SCED = new SubstitutionCipherED(KeyFromNownPlainAndCipher);
        int best = Integer.MAX_VALUE;
        int bestPosition = 0;
        if (false)//KeyFromNownPlainAndCipher.size() > 52-8)
        {
            //not working -- please ignore ---------------------------------- start
            byte[] SectionOfCiphertext = SectionOfCiphertext(cbc.StringToUtf8(cbc.CBCDecryption(IV,cipher,SCED,false)), 5000);
            //byte[] section = SectionOfCiphertext(cipher,5000);
            String cipherDecryptWithPartialKay = cbc.CBCDecryption(IV,SectionOfCiphertext,SCED,false);
            ArrayList<HashMap<Character, Character>> KeysFromNownPlainAndCipher = GetKeyReminder(KeyFromNownPlainAndCipher);
            for (int i = 0; i < KeysFromNownPlainAndCipher.size(); i++) {
                String decryptedText = cbc.CBCDecryption(IV, cbc.StringToUtf8(cipherDecryptWithPartialKay), new SubstitutionCipherED(KeysFromNownPlainAndCipher.get(i)),false);
                int nonEnglishWords = CheckKeyReturnsEnglish(decryptedText);
                if (best > nonEnglishWords) {
                    best = nonEnglishWords;
                    bestPosition = i;
                }
            }
            return joinKeys(KeysFromNownPlainAndCipher.get(bestPosition),KeyFromNownPlainAndCipher);
            //not working -- please ignore ----------------------------------- end
        }
        else
        {
            byte[] section = SectionOfCiphertext(cipher,100000);
            String cipherDecryptWithPartialKay = cbc.CBCDecryption(IV,section,SCED,true);
            ArrayList<Byte> a = cbc.changedLettersAndSpaces;
            String[] b = splitByteArrayToWordsArrays(a.toArray(new Byte[a.size()]));
            ArrayList<String> d = new ArrayList<String>(Arrays.asList(b));
            d.removeAll(Arrays.asList(""));
            //d.removeIf(p->p.length()==1);
            HashMap<Integer,Pair<Integer,String>> c = onlyOneCharChanged(d,cbc.IVforAttack);
            String[] decryptedTextSaperatedBySpace = cipherDecryptWithPartialKay.split("[^A-Za-z]");
            ArrayList<String> DecryptedWordsArray = new ArrayList<String>(Arrays.asList(decryptedTextSaperatedBySpace));
            DecryptedWordsArray.removeAll(Arrays.asList(""));
            Map<Character,Character> joinedKey = joinKeys(KeyFromNownPlainAndCipher,getSecondPartialKey(DecryptedWordsArray,c,KeyFromNownPlainAndCipher));
            Map<Character,Character> aaa = GetFinalKey(joinKeys(KeyFromNownPlainAndCipher,getSecondPartialKey(DecryptedWordsArray,c,KeyFromNownPlainAndCipher)));
            return joinKeys(joinedKey,aaa);
        }
        //return joinKeys(KeysFromNownPlainAndCipher.get(bestPosition),KeyFromNownPlainAndCipher);
    }
        //byte[] SubCipher= SectionOfCiphertext (cipherDecryptWithPartialKay, PercentCheck);

        //ArrayList<HashMap<Character,Character>> keys = new ArrayList<>();
        //KeysFromNownPlainAndCipher.parallelStream().filter(s->CheckKeyReturnsEnglish(cbc.CBCDecryption(IV,SubCipher,new SubstitutionCipherED(s)),minimumNumberOfNonEnglishWords)).findFirst().ifPresent(s->keys.add(s));//.forEach(p->keys.add(p));
        public Map<Character,Character> joinKeys(Map<Character,Character> keyA,Map<Character,Character> keyB )
        {
            keyA.putAll(keyB);
            return new TreeMap<Character, Character>(keyA);
        }

    public HashMap<Character,Character> GetFinalKey(Map<Character,Character> partialKey)
    {
        String aZKey = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        //String aZValue = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String reminderKey = "";
        String reminderValue = "";
        HashMap<Character,Character> key= new HashMap<Character,Character>();
        for (int i=0;i<52;i++)
        {
            if (!partialKey.keySet().contains(aZKey.charAt(i)))
                reminderKey+=aZKey.charAt(i);
        }
        for (int i=0;i<reminderKey.length();i++)
        {
            if (!partialKey.keySet().contains(aZKey.charAt(i)))
                reminderValue+=aZKey.charAt(i);
        }
        for (int i=0;i<reminderKey.length();i++)
        {
            key.put(reminderKey.charAt(i),reminderValue.charAt(i));
        }
        return  key;

    }
    public String[] splitByteArrayToWordsArrays(Byte[] changedLetterAndSpaces)
    {
        String changedLASString = "";

        for (int i = 0; i < changedLetterAndSpaces.length ; i++)
        {
            switch (changedLetterAndSpaces[i])
            {
                case 0:
                {
                    changedLASString+="0";
                    break;
                }
                case 1:
                {
                    changedLASString+="1";
                    break;
                }
                case 2:
                {
                    changedLASString+="2";
                    break;
                }
            }
        }
        String[] words = changedLASString.split("2");
        return words;
    }
    public HashMap<Integer,Pair<Integer,String>> onlyOneCharChanged(ArrayList<String> words, String IVString)
    {
        HashMap<Integer,Pair<Integer,String>> dictionary = new HashMap<Integer,Pair<Integer,String>>();
        String[] IVbyWords = IVString.split(",");
        ArrayList<String> IVArrayListAfterSplit = new ArrayList<String>(Arrays.asList(IVbyWords));
        IVArrayListAfterSplit.removeAll(Arrays.asList(""));
        for (int i = 0; i < words.size(); i++)
        {
            int count = words.get(i).length() - words.get(i).replace("0", "").length();
            if(count == 1 && words.get(i).length()>4)
            {
                int pos = words.get(i).indexOf("0");
                String[] g = IVArrayListAfterSplit.get(i).split("\\.");
                ArrayList<String> IVword = new ArrayList<String>(Arrays.asList(g));
                IVword.removeAll(Arrays.asList(""));
                dictionary.put(i,new Pair<Integer,String> (pos,IVword.get(pos)));
            }
        }
        return dictionary;
    }
    public HashMap<Character,Character> getSecondPartialKey(ArrayList<String> DecryptedWordsArray,HashMap<Integer,Pair<Integer,String>> c,HashMap<Character,Character>firstPartialKey)
    {
        HashMap<Character,Character> partialKey = new HashMap<Character,Character>();
        String aZ = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        for (HashMap.Entry<Integer,Pair<Integer,String>>candidate:c.entrySet())
        {
            String word = DecryptedWordsArray.get(candidate.getKey());
            char keyPartValue = word.charAt(candidate.getValue().getKey());
            for (Character keyChar: firstPartialKey.keySet()
                 ) {
                aZ = aZ.replace(Character.toString(keyChar),"");
            }
            int counter = 0;
            char keyPartKey = ' ';
            for (char azChar:aZ.toCharArray()
                 ) {
                char x = xorChar(azChar,Byte.parseByte(candidate.getValue().getValue()));
                String wordCandidate = (word.substring(0,candidate.getValue().getKey()) + azChar + word.substring(candidate.getValue().getKey()+1,word.length())).toLowerCase();
                if(EnglishWordsSet.contains(wordCandidate))
                {
                    counter++;
                    keyPartKey = azChar;
                }
            }
            if(counter==1)
            {
                String byteValue = candidate.getValue().getValue();
                byte t = Byte.parseByte(byteValue);
                char x = xorChar(keyPartKey,t);
                char y = xorChar(keyPartValue,t);
                if (!partialKey.keySet().contains(x) && !partialKey.containsValue(y) && !firstPartialKey.containsValue(y) && !firstPartialKey.containsKey(x) && Character.isLetter(x) && Character.isLetter(y))
                {
                    partialKey.put(new Character(x),new Character(y));
                    aZ = aZ.replace(Character.toString(x),"");

                }
            }
        }
        return partialKey;
    }
    public char xorChar(char C,byte xorIV)
    {
        int xor = (int)C ^ (int)xorIV;
        return (char)((byte)(0xff & xor));
    }

}

