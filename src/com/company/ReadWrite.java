package com.company;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.nio.file.Files;
import java.io.File;
/**
 *
 */
public class ReadWrite {

    public ReadWrite(){}
    /*Legal and not empty*/
    public void FilePathLegal(String filePath)
    {
        File pat = new File(filePath);
        if (!pat.exists()) {
           System.out.print("Invalid text path. Terminating..");
           System.exit(1);
        }

    }

    /**Read encrypted or unencrypted text*/
    public String ReadText(String filePath){
        FilePathLegal(filePath);
        String content = null;
        File file = new File(filePath); //for ex foo.txt
        FileReader reader = null;
        try {
            reader = new FileReader(file);
            char[] chars = new char[(int) file.length()];
            reader.read(chars);
            content = new String(chars);
            reader.close();

        } catch (IOException e) {
            System.out.print("Invalid arguments. Terminating..");
            System.exit(1);
            //e.printStackTrace();

        } finally {
            if(reader !=null) {
                try
                {
                    reader.close();
                }
                catch (IOException ex)
                {
                    System.out.print("Invalid arguments. Terminating..");
                    System.exit(1);

                }
            }
        }
        return content;
    }
    /**Write encrypted or unencrypted text*/
    public void WriteEncryptedText(String filePathDestination,byte[][] text)
    {

        File temp = new File(filePathDestination);
        temp.getParentFile().mkdirs();
        if (temp.exists())
        {
            temp.delete();
        }
        try{
            Path path = Paths.get(filePathDestination);
            Files.write(path, text[0]);

            for (int i = 1; i<text.length;i++)
                Files.write(path, text[i], StandardOpenOption.APPEND);
        }
        catch (Exception e)
        {
            System.out.print("Invalid arguments. Terminating..");
            System.exit(1);
        }
    }
    public void WriteText(String filePathDestination, String text){
        File temp = new File(filePathDestination);
        temp.getParentFile().mkdirs();
        if (temp.exists())
        {
            temp.delete();
        }
        BufferedWriter bw = null;
        FileWriter fw = null;
        try {

            fw = new FileWriter(filePathDestination);
            bw = new BufferedWriter(fw);
            bw.write(text);

        } catch (IOException e) {
            System.out.print("Invalid arguments. Terminating..");
            System.exit(1);

        } finally {

            try {

                if (bw != null)
                    bw.close();

                if (fw != null)
                    fw.close();

            } catch (IOException ex) {
                System.out.print("Invalid arguments. Terminating..");
                System.exit(1);
            }
        }
    }
    public byte[] ReadTextbyte(String filePath)
    {
        Path path = Paths.get(filePath);
        try
        {
            byte[] data = Files.readAllBytes(path);
            return  data;
        }
        catch (IOException e) {
            System.out.print("Invalid arguments. Terminating..");
            System.exit(1);
        }
        return  null;
    }
   /**Read key from text file */
    public HashMap<Character,Character> ReadKey(String filePath)
    {
        FilePathLegal(filePath);

        HashMap<Character ,Character> KeyMap = new HashMap<Character, Character>();
            String keyString = ReadText(filePath);
            keyString = keyString.replaceAll("\r\n","");
            keyString = keyString.replaceAll(" ","");

            Integer length=keyString.length();
            for(int i=0; i<length-1; i++)
            {
                KeyMap.put(keyString.charAt(i),keyString.charAt(i+1));
                i++;
            }
            return KeyMap;

    }
    /**Write key to text file */
    public void WriteKey(Map<Character,Character> KeyMap,String filePath){
        String KeyString= KeyMap.toString();
        KeyString= KeyString.substring(1,KeyString.length()-1);
        String KeyToWrite="";
       // Integer sizeMap= KeyMap.size();
        for (int i=0;i<KeyString.length();i++){
            KeyToWrite=KeyToWrite+KeyString.charAt(i)+" "+KeyString.charAt(i+2);
            i=i+4;
            if (i!=KeyString.length()-3 ){
               KeyToWrite+="\r\n";
             }
        }
        WriteText(filePath,KeyToWrite);
    }
}
