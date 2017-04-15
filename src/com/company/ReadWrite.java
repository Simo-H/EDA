package com.company;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;import java.util.HashMap;
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
            e.printStackTrace();

        } finally {
            if(reader !=null) {
                try
                {
                    reader.close();
                }
                catch (IOException ex){}
            }
        }

        return content;

    }
    /**Write encrypted or unencrypted text*/
    public void WriteText(String filePathDestination, String text){
        BufferedWriter bw = null;
        FileWriter fw = null;
        try {

            fw = new FileWriter(filePathDestination);
            bw = new BufferedWriter(fw);
            bw.write(text);

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            try {

                if (bw != null)
                    bw.close();

                if (fw != null)
                    fw.close();

            } catch (IOException ex) {

                ex.printStackTrace();

            }

        }


    }
   /**Read key from text file */
    public Map<Character,Character> ReadKey(String filePath)
    {

            Map<Character ,Character> KeyMap = new HashMap<Character, Character>();
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
        for (int i=0;i<KeyString.length()-3;i++){
            KeyToWrite=KeyToWrite+KeyString.charAt(i)+" "+KeyString.charAt(i+2);
            i=i+4;
            if (i!=KeyString.length()-4 ){
               KeyToWrite+="\r\n";
             }
        }
        WriteText(filePath,KeyToWrite);
    }
}
