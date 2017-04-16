package com.company;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Handling the entire process of encryption and decoding by cbc mode
 */
public class CBCmode {

    private int BlockSize;
    private SubstitutionCipherED Encryption;
   // private String[] cipertext;

   // private String[] textDivided;
   // private String Text;


    public CBCmode(int blockSize,SubstitutionCipherED encryption)
    {
        BlockSize=blockSize;
        Encryption=encryption;
    }

    public String[] Divided(String text)
    {
        String[] textDivided;
        if ((text.length()% BlockSize)== 0)
        {
            textDivided= new String[text.length()/ BlockSize];
        }
        else
        {
            textDivided= new String[(text.length()/ BlockSize)+1];
        }
        int mod=text.length()% BlockSize ;
        int div=text.length()/ BlockSize;
        int j=0;
        for (int i=0; i<=div;i++)
        {
            for (int f=0; f<BlockSize;f++)
            {
                textDivided[i] = textDivided[i]+text.charAt(j);
                j++;
            }
        }
        if ( mod!=0)
        {
            for (int i=0; i<=mod;i++)
            {
                textDivided[div+1] = textDivided[div+1]+text.charAt(j);
                j++;
            }

        }
        return textDivided;

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

    public String CBCEncryption(String IVS,String text)
    {
        String[] cipertext;
        String[] textDivided;
        textDivided=Divided(text);
        cipertext=new String[textDivided.length];
        byte[] IV= StringToUtf8(IVS);
        byte[] plaintextByte= new byte[BlockSize];
        byte[] xor= new byte[BlockSize];

        for (int i=0;i<textDivided.length;i++)
        {
            plaintextByte= StringToUtf8(textDivided[i]);
            xor=xor( plaintextByte,IV) ;
            String ToBeEncrypted=Uf8ToString(xor);
            cipertext[i]= Encryption.Encrypt(ToBeEncrypted);
            IV= StringToUtf8(cipertext[i]);
        }
        String complete;
        complete=String.join("", cipertext);
        return  complete;
    }
}
