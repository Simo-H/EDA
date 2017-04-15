package com.company;

import java.io.IOException;

/**
 * Handling the entire process of encryption and decoding by cbc mode
 */
public class CBCmode {

    private int BlockSize;
    private String[] textDivided;
    private String Text;

    public CBCmode(int blockSize,String text)
    {
        Text=text;
        BlockSize=blockSize;
        if ((text.length()% blockSize)== 0)
        {
            textDivided= new String[text.length()/ blockSize];
        }
        else
        {
            textDivided= new String[(text.length()/ blockSize)+1];
        }
        textDivided =new String[BlockSize];

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
    public byte[] StringToUf8(String PlanText)
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
    public void CBCEncryption()
    {

    }

}
