package com.company;

/**
 * Created by Simo on 15/04/2017.
 * 
 */
enum Action{
    Encrypt, Decrypt, Attack
}
enum EncryptionAlgorithm{
    sub_cbc_10, sub_cbc_52
}
public class CommandReader {

    private String textPath;
    private String keyPath;
    private String IVPath;
    private String outputFilePath;
    private Action action;
    private EncryptionAlgorithm EA;
    private String knownPlaintext;
    private String knownCiphertext;

    public CommandReader(String[] args)
    {

        if(args[0].equals("-a"))
        {
            switch (args[1])
            {
                case "sub_cbc_10": EA = EncryptionAlgorithm.sub_cbc_10;
                    break;
                case "sub_cbc_52": EA = EncryptionAlgorithm.sub_cbc_52;
                    break;

                default:
                    System.out.print("Invalid arguments. Terminating..");
                    System.exit(1);
                    break;
            }
        }
        else {
            System.out.print("Invalid arguments. Terminating..");
            System.exit(1);
        }
        if(args[2].equals("-c"))
        {
            switch (args[3])
            {
                case "encryption": action = Action.Encrypt;
                    break;
                case "decryption": action = Action.Decrypt;
                    break;
                case "attack": action = Action.Attack;
                    break;
                default:
                    System.out.print("Invalid arguments. Terminating..");
                    System.exit(1);
                    break;
            }
        }
        else
        {
            System.out.print("Invalid arguments. Terminating..");
            System.exit(1);
        }
        if(args[4].equals("-t"))
            textPath = args[5];
        else
        {
            System.out.print("Invalid arguments. Terminating..");
            System.exit(1);
        }
        if (action == Action.Attack)
        {
            if (EA == EncryptionAlgorithm.sub_cbc_10)
            {
                if(args.length != 10)
                {
                    System.out.print("Number of passed arguments is invalid. Terminating..");
                    System.exit(1);
                }
                if(args[6].equals("-v"))
                    IVPath = args[7];
                else
                {
                    System.out.print("Invalid arguments. Terminating..");
                    System.exit(1);
                }
                if(args[8].equals("-o"))
                    outputFilePath = args[9];
                else
                {
                    System.out.print("Invalid arguments. Terminating..");
                    System.exit(1);
                }
            }
            if (EA == EncryptionAlgorithm.sub_cbc_52)
            {
                if(args.length != 14)
                {
                    System.out.print("Number of passed arguments is invalid. Terminating..");
                    System.exit(1);
                }
                if(args[6].equals("-kp"))
                    knownPlaintext = args[7];
                else
                {
                    System.out.print("Invalid arguments. Terminating..");
                    System.exit(1);
                }
                if(args[8].equals("-kc"))
                    knownCiphertext = args[9];
                else
                {
                    System.out.print("Invalid arguments. Terminating..");
                    System.exit(1);
                }
                if(args[10].equals("-v"))
                    IVPath = args[11];
                else
                {
                    System.out.print("Invalid arguments. Terminating..");
                    System.exit(1);
                }
                if(args[12].equals("-o"))
                    outputFilePath = args[13];
                else
                {
                    System.out.print("Invalid arguments. Terminating..");
                    System.exit(1);
                }
            }
        }
        else
        {
            if(args.length != 12)
            {
                System.out.print("Number of passed arguments is invalid. Terminating..");
                System.exit(1);
            }
            if(args[6].equals("-k"))
                keyPath = args[7];
            else
            {
                System.out.print("Invalid arguments. Terminating..");
                System.exit(1);
            }
            if(args[8].equals("-v"))
                IVPath = args[9];
            else
            {
                System.out.print("Invalid arguments. Terminating..");
                System.exit(1);
            }
            if(args[10].equals("-o"))
                outputFilePath = args[11];
            else
            {
                System.out.print("Invalid arguments. Terminating..");
                System.exit(1);
            }

        }

    }

}
