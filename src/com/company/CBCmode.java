package com.company;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Handling the entire process of encryption and decoding by cbc mode
 */
public class CBCmode {

    private int BlockSize;
   // private SubstitutionCipherED Encryption;



    public CBCmode(int blockSize)
    {
        BlockSize=blockSize;
    }

    public  byte[][] Divided(byte[] text)
    {
        byte[][] textDivided;
        if ((text.length % BlockSize)== 0)
        {
            textDivided= new byte[text.length/ BlockSize][BlockSize];
        }
        else
        {
            textDivided= new byte[(text.length/ BlockSize)+1][BlockSize];
        }
        int mod=text.length% BlockSize ;
        int div=text.length/ BlockSize;
        int j=0;
        for (int i=0; i<div;i++)
        {
            for (int f=0; f<BlockSize;f++)
            {
                // textDivided[i] = textDivided[i]+text.charAt(j);
                textDivided[i][f]=text[j];
                j++;
            }
        }
        if ( mod!=0)
        {
            for (int i=0; i<mod;i++)
            {
                textDivided[div][i]=text[j];
                j++;
            }
            for (int i = mod;i<BlockSize;i++)
            {
                textDivided[div][i]= 0;
            }
        }
        return textDivided;
    }
    public byte[] StringToUtf8(String PlanText)
    {
            byte[] b = PlanText.getBytes(StandardCharsets.UTF_8);

            if (b.length<BlockSize)
            {
               return Arrays.copyOf(b,BlockSize);
            }
            return b;
    }

    public String Uf8ToString(byte[] bytePlanText)
    {
             return new String(bytePlanText, StandardCharsets.UTF_8);
    }

    public byte[] xor(byte[] plaintextByte, byte[] IV)
    {
        byte[] postXorArray = new byte[BlockSize];

        for (int i = 0; i<BlockSize;i++)
        {
            int xor = (int)plaintextByte[i] ^ (int)IV[i];
            postXorArray[i] = (byte)(0xff & xor);
        }
        return postXorArray;
    }

    public byte[][] CBCEncryption(byte[] IV,byte[] plainText,SubstitutionCipherED Encryption)
    {
        byte[][] textDivided;
        textDivided = Divided(plainText);
        byte[][] cipherTextByte= new byte[textDivided.length][BlockSize];
        byte[] xor= new byte[BlockSize];
        for (int i=0;i<textDivided.length;i++)
        {
            xor=xor(textDivided[i],IV);
            cipherTextByte[i] = Encryption.Encrypt(xor);
            IV = cipherTextByte[i];
        }
        return cipherTextByte;
    }

    public String CBCDecryption(byte[] IV,byte[] cipherText,SubstitutionCipherED Encryption)
    {
        byte[][] cipherDivided=Divided(cipherText);
        byte[] xor;
        String[] plaintext=new String[cipherDivided.length];
        for (int i=0;i<cipherDivided.length;i++)
        {
            byte[] DecryptedCipher = Encryption.Decrypt(cipherDivided[i]);
            xor = xor(DecryptedCipher,IV);
            plaintext[i]=Uf8ToString(xor);
            IV=cipherDivided[i];
        }
        String lastPlaintext="";
        for (int i=0;i<BlockSize;i++)
        {
            if((int)(plaintext[cipherDivided.length-1].charAt(i))!=0)
            {
                lastPlaintext=lastPlaintext+plaintext[cipherDivided.length-1].charAt(i);
            }
        }
        plaintext[cipherDivided.length-1]=lastPlaintext;
        return String.join("", plaintext);
    }
}
