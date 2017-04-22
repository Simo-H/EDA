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

    public byte[] Decrypt(byte[] cipherText)
    {
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
            }
            else
            {
                DecryptionBytes[i] =cipherText[i];
            }
        }
        return DecryptionBytes;
    }
}
