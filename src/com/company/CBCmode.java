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
    public ArrayList<Byte> changedLettersAndSpaces;
    public String IVforAttack;


    public CBCmode(int blockSize)
    {
        BlockSize=blockSize;
        changedLettersAndSpaces = new ArrayList<Byte>();
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

    public String Uf8ToString(Byte[] bytePlanText)
    {
        int j = 0;
        byte[] bytes = new byte[bytePlanText.length];
        for(Byte b: bytePlanText)
            bytes[j++] = b.byteValue();
        return new String(bytes, StandardCharsets.UTF_8);
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

    public String CBCDecryption(byte[] IV,byte[] cipherText,SubstitutionCipherED Encryption,boolean knownPlainTextAttack)
    {
        byte[][] cipherDivided=Divided(cipherText);
        byte[] xor;
        IVforAttack = "";
        String[] plaintext=new String[cipherDivided.length];
        for (int i=0;i<cipherDivided.length-1;i++)
        {
            byte[] DecryptedCipher = Encryption.Decrypt(cipherDivided[i],knownPlainTextAttack);
            byte[] changedLetters = Encryption.changedLettersAndSpaces;
            xor = xor(DecryptedCipher,IV);
            //
            byte[] changedLettersAndSpaces = replaceSpaces(xor,changedLetters,knownPlainTextAttack);
            for (int j = 0; knownPlainTextAttack && j <  changedLettersAndSpaces.length; j++)
            {
                this.changedLettersAndSpaces.add(changedLettersAndSpaces[j]);
                switch (changedLettersAndSpaces[j])
                {
                    case 0: IVforAttack+=IV[j]+".";
                    break;
                    case 1: IVforAttack+=IV[j]+".";
                    break;
                    case 2: IVforAttack+=",";
                    break;
                }
            }
            //
            plaintext[i]=Uf8ToString(xor);
            IV=cipherDivided[i];
        }
        byte[] DecryptedCipher = Encryption.Decrypt(cipherDivided[cipherDivided.length-1],knownPlainTextAttack);
        byte[] changedLetters = Encryption.changedLettersAndSpaces;
        xor = xor(DecryptedCipher,IV);
        byte[] changedLettersAndSpaces = replaceSpaces(xor,changedLetters,knownPlainTextAttack);
        int countZero=0;
        for ( int i=BlockSize-1;i>0 && xor[i]==0 ;i--  )
        {
            countZero++;
        }
        for (int j = 0;knownPlainTextAttack &&  j < changedLettersAndSpaces.length - countZero ; j++)
        {
            this.changedLettersAndSpaces.add(changedLettersAndSpaces[j]);
            switch (changedLettersAndSpaces[j])
            {
                case 0: IVforAttack+=IV[j]+".";
                    break;
                case 1: IVforAttack+=IV[j]+".";
                    break;
                case 2: IVforAttack+=",";
                    break;
            }
        }
        byte[] dest=new byte[xor.length-countZero];
        System.arraycopy( xor, 0, dest, 0, xor.length-countZero);

        plaintext[cipherDivided.length-1]=Uf8ToString(dest);

        return String.join("", plaintext);
    }

    public byte[] replaceSpaces(byte[] xoredText,byte[] changedLetters,boolean knownPlainTextAttack)
    {
        for (int i=0;i<xoredText.length && knownPlainTextAttack;i++)
        {
            if (!((xoredText[i]<(0xff & 123) && xoredText[i]>(0xff & 96)) || (xoredText[i]<(0xff & 91) && xoredText[i]>(0xff & 64))))
                changedLetters[i] = 2;
        }
        return changedLetters;
    }
}
