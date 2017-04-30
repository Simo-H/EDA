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

    public String textPath;
    public String keyPath;
    public String IVPath;
    public String outputFilePath;
    public Action action;
    public EncryptionAlgorithm EA;
    public String knownPlaintext;
    public String knownCiphertext;

    public CommandReader(String[] args) {
        for (int i = 0; i < args.length; i = i + 2) {
            switch (args[i]) {
                case "-a": {
                    switch (args[i + 1]) {
                        case "sub_cbc_10":
                            EA = EncryptionAlgorithm.sub_cbc_10;
                            break;
                        case "sub_cbc_52":
                            EA = EncryptionAlgorithm.sub_cbc_52;
                            break;

                        default:
                            System.out.print("Invalid arguments. Terminating..");
                            System.exit(1);
                            break;
                    }
                    break;
                }
                case "-c": {
                    switch (args[i + 1]) {
                        case "encryption":
                            action = Action.Encrypt;
                            break;
                        case "decryption":
                            action = Action.Decrypt;
                            break;
                        case "attack":
                            action = Action.Attack;
                            break;
                        default:
                            System.out.print("Invalid arguments. Terminating..");
                            System.exit(1);
                            break;
                    }
                    break;
                }
                case "-t": {
                    textPath = args[i + 1];
                    break;
                }
                case "-v": {
                    IVPath = args[i + 1];
                    break;
                }
                case "-o": {
                    outputFilePath = args[i + 1];
                    break;
                }
                case "-kp": {
                    knownPlaintext = args[i + 1];
                    break;
                }
                case "-kc": {
                    knownCiphertext = args[i + 1];
                    break;
                }
                case "-k": {
                    keyPath = args[i + 1];
                    break;
                }
                default: {
                    System.out.print("Invalid arguments. Terminating..");
                    System.exit(1);
                }
            }

        }
    }

}