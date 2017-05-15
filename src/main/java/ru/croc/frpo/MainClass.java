package ru.croc.frpo;

/**
 * Created by pingvindex on 03.05.17.
 */
public class MainClass {

    public static void main(String[] args) {

        CommandReader reader = new CommandReader();
        if (args.length == 1) {
            reader.execFile(args[0]);
        } else if (args.length == 0) {
            reader.start();
        } else {
            System.out.println("Error: too many global arguments");
        }
    }
}
