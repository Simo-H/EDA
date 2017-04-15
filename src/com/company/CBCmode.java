package com.company;

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
            for (int f=0; f<=10;i++)
            {
                textDivided[j] = textDivided[j]+Text.charAt(f);

            }
            j++;
        }
        for (int i=0; i<=mod;i++)
        {
            textDivided[j] = textDivided[j]+Text.charAt(i);
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
}
