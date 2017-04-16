package com.company;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Handling the entire process of encryption and decoding by cbc mode
 */
public class CBCmode {

    private int BlockSize;
    private String[] textDivided;
    private String Text;
    private SubstitutionCipherED SubstitutionC_ED;
    private String[] cipertext;


    public CBCmode(int blockSize,String text,HashMap substitutionCipherED)
    {

        Text=text;
        BlockSize=blockSize;
        SubstitutionC_ED=new SubstitutionCipherED(substitutionCipherED);
        if ((text.length()% blockSize)== 0)
        {
            textDivided= new String[text.length()/ blockSize];
        }
        else
        {
            textDivided= new String[(text.length()/ blockSize)+1];
        }
        textDivided =new String[BlockSize];
        Divided();
        cipertext=new String[textDivided.length];

    }
    public void Divided()
    {
        int j=0;
        int mod=Text.length()% BlockSize ;
        int div=Text.length()/ BlockSize;
        for (int i=0; i<=div;i++)
        {
            for (int f=0; f<=BlockSize;i++)
            {
                textDivided[j] = textDivided[j]+Text.charAt(f);

            }
            j++;
        }
        if ( mod!=0)
        {
            for (int i=0; i<=mod;i++)
            {
                textDivided[j] = textDivided[j]+Text.charAt(i);
            }

        }

    }
    public byte[] StringToUtf8(String PlanText)
    {
        try
        {
            byte[] b = PlanText.getBytes("UTF-8");

            for ( int i= b.length;i<BlockSize;i++ )
            {
                b[i]=0;
            }
            return b;
        }
        catch (IOException ex)
        {
            System.out.print("utf8 Translation did not succeed");
            return null;
        }

    }
    public String Uf8ToString(byte[] bytePlanText)
    {
        try
        {
            String Plantext= new String(bytePlanText,"UTF-8");

            return Plantext;
        }
        catch (IOException ex)
        {
            System.out.print("String translation did not succeed");
            return null;
        }

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

    public String CBCEncryption(String IVS, Map<Character,Character> Key)
    {
        byte[] IV= StringToUtf8(IVS);
        byte[] plaintextByte= new byte[BlockSize];
        byte[] xor= new byte[BlockSize];

        for (int i=0;i<textDivided.length;i++)
        {
            plaintextByte= StringToUtf8(textDivided[i]);
            xor=xor( plaintextByte,IV) ;
            String encrypt=Uf8ToString(xor);
            cipertext[i]= SubstitutionC_ED.Encrypt(encrypt);
            IV= StringToUtf8(cipertext[i]);
        }
        String complete;
        complete=String.join("", cipertext);
        return  complete;
    }
}
