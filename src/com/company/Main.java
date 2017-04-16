package com.company;
import java.util.Map;

import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        // write your code here
        ReadWrite RW = new ReadWrite();
        String path = "C:\\Users\\Stav\\Desktop\\plainMsg_example.txt";
        String text = RW.ReadText(path);
        //char x = text.charAt(0);
        //RW.WriteText("C:\\Users\\Stav\\Desktop\\TEST.txt", "WHATS UP? ");
        Map<Character ,Character> KeyMap=new HashMap<Character, Character>();
        KeyMap=RW.ReadKey("C:\\Users\\Stav\\Desktop\\key.txt");
        RW.WriteKey(KeyMap,"C:\\Users\\Stav\\Desktop\\writekey.txt");
      //  CBCmode cbc=new CBCmode(10,"qrstabcdqr");
        byte[] bb=new byte[10];
       // bb=cbc.StringToUtf8("qrstabcdqr");
       // String A=cbc.Uf8ToString(bb);
    }
}
