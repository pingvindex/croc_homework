package ru.croc.frpo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.Stack;

/**
 * Created by pingvindex on 12.05.17.
 */
public class CommandReader {
    private ArrayList<String> history = null;
    private CommandExecutor executor = null;
    private Path startFolder = null;

    public CommandReader() {
        history = new ArrayList<>();
        executor = new CommandExecutor();
        startFolder = Paths.get(".").toAbsolutePath().normalize();
    }

    public void start() {

        String currentCommand = null;
        for(int i=1; i<100; i++) {

            System.out.print(executor.getCurrentDirectoryPath() + "-> ");
            currentCommand = readCommand();
            if (currentCommand.equals("exit")) {
                break;
            }

            history.add(currentCommand);
            parseCommand(currentCommand);
            System.out.println("===========================================================================");
        }

    }

    public static String readCommand() {
        Scanner input = new Scanner(System.in);
        return input.nextLine();
    }

    private boolean parseCommand(String currentCommand) {

        String[] argsOfCommand = currentCommand.split(" ", 3);

        switch (argsOfCommand[0]) {
            case "help":
                executor.help(argsOfCommand);
                break;
            case "ls":
                executor.printListOfFiles(argsOfCommand);
                break;
            case "cd":
                executor.changeDirectory(argsOfCommand);
                break;
            case "md":
                executor.createDirectory(argsOfCommand);
                break;
            case "mf":
                executor.createFile(argsOfCommand);
                break;
            case "af":
                executor.addToFile(argsOfCommand);
                break;
            case "cat":
                executor.printFile(argsOfCommand);
                break;
            default:
                System.out.println("Unknown command");
        }

        return false;
    }

    public void execFile(String arg) {
        Path script = startFolder.resolve(arg);
        if (!Files.isRegularFile(script)) {
            System.out.println("Error: no matching file found");
        } else {
            try(BufferedReader br = Files.newBufferedReader(script))
            {
                String command;
                while ((command=br.readLine()) != null) {
                    if (command.equals("exit")) {
                        break;
                    }
                    System.out.println("Command " + command + ":");
                    parseCommand(command);
                    System.out.println("===========================================================================");
                }
            }
            catch(IOException ex){
                System.out.println("Error: " + ex.getMessage());
            }
        }
    }
}
