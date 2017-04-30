package com.company;

import java.util.HashMap;

/**
 * Created by Simo on 15/04/2017.
 */
public class SubstitutionCipherED {

    HashMap<Character,Character> key;
    public byte[] changedLettersAndSpaces;

    public SubstitutionCipherED(HashMap<Character, Character> key) {
        this.key = key;
    }

    public byte[] Encrypt(byte[] xoredText)
    {
        HashMap<Byte, Byte> ByteKey  = new HashMap<Byte, Byte>();
        for(HashMap.Entry<Character, Character> entry : key.entrySet()){
            ByteKey.put((byte)(entry.getKey().charValue()),(byte)entry.getValue().charValue());
        }
        byte[] EncryptionBytes = new byte[xoredText.length];
        for (int i = 0;i<xoredText.length;i++)
        {
            if (ByteKey.containsKey(xoredText[i]))
            {
                EncryptionBytes[i] = ByteKey.get(xoredText[i]);
            }
            else
            {
                EncryptionBytes[i] =xoredText[i];
            }
        }
        return EncryptionBytes;
    }

    public byte[] Decrypt(byte[] cipherText, boolean knownPlainTextAttack)
    {
        if(knownPlainTextAttack)
            changedLettersAndSpaces = new byte[cipherText.length];
        HashMap<Byte, Byte> reverseByteKey  = new HashMap<Byte, Byte>();
        for(HashMap.Entry<Character, Character> entry : key.entrySet()){
            reverseByteKey.put((byte)entry.getValue().charValue(),(byte)(entry.getKey().charValue()));
        }
        byte[] DecryptionBytes = new byte[cipherText.length];
        for (int i = 0;i<cipherText.length;i++)
        {
            if (reverseByteKey.containsKey(cipherText[i]))
            {
                DecryptionBytes[i] = reverseByteKey.get(cipherText[i]);
                if(knownPlainTextAttack)
                    changedLettersAndSpaces[i] = 1;
            }
            else
            {
                DecryptionBytes[i] =cipherText[i];
                if(knownPlainTextAttack)
                {
                    if (!((cipherText[i]<(0xff & 123) && cipherText[i]>(0xff & 96)) || (cipherText[i]<(0xff & 91) && cipherText[i]>(0xff & 64))))
                        changedLettersAndSpaces[i] = 1;
                    else
                        changedLettersAndSpaces[i] = 0;
                }
            }
        }
        return DecryptionBytes;
    }
}
