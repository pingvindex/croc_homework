package ru.croc.frpo;

import java.io.*;
import java.nio.file.*;

/**
 * Created by pingvindex on 13.05.17.
 */
public class CommandExecutor {

    private Path currentDirectory = null;

    public CommandExecutor() {
            currentDirectory = Paths.get(".").toAbsolutePath().normalize();
    }

    public String getCurrentDirectoryPath() {
        return currentDirectory.getFileName().toString();
    }

    public void help(String[] command) {

        if (command.length > 1) {
            System.out.println("Error: to many arguments");
            return;
        }
        System.out.print("help");
        System.out.println("\t\t\tPrint list of commands");
        System.out.print("ls");
        System.out.println("\t\t\tShow files in current directory");
        System.out.print("cd path");
        System.out.println("\t\t\tGo to the another directory");
        System.out.print("md dirName");
        System.out.println("\t\tCreate new directory");
        System.out.print("mf fileName");
        System.out.println("\t\tCreate new file");
        System.out.print("af fileName");
        System.out.println("\t\tAdd new strings in file. Expect input from user until get command \"/finish\"");
        System.out.print("cat fileName N");
        System.out.println("\t\tPrint first N lines in fileName in output stream");

    }

    public void printListOfFiles(String[] command) {

        if(command.length > 1) {
            System.out.println("Error: too many arguments");
        } else {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(currentDirectory)) {
                for (Path file : stream) {
                    System.out.println(file.getFileName());
                }
            } catch (IOException | DirectoryIteratorException x) {
                System.out.println("Error: can't get information about current directory content");
            }
        }
    }

    public void changeDirectory(String[] command) {
        if (command.length == 1) {
            System.out.println("Error: input directory name");
        } else if(command.length > 2) {
            System.out.println("Error: too many arguments");
        } else {
            Path newDirectory = null;
            newDirectory = Paths.get(command[1]);
            if (newDirectory.isAbsolute()) {
                if (Files.isDirectory(newDirectory)) {
                    currentDirectory = newDirectory;
                } else {
                    System.out.println("Error: can't find directory");
                }
            } else {
                Path tmpPath = currentDirectory.resolve(newDirectory).normalize();
                if (Files.isDirectory(tmpPath)) {
                    currentDirectory = tmpPath;
                } else {
                    System.out.println("Error: can't find directory");
                }
            }
        }
    }

    public void createDirectory(String[] command) {
        if (command.length == 2) {
            try {
                Files.createDirectory(currentDirectory.resolve(command[1]));
            } catch (IOException e) {
                System.out.println("Error: can't create directory with this name");
            }
        } else if (command.length == 1) {
            System.out.println("Error: input directory name");
        } else {
            System.out.println("Error: too many arguments");
        }

    }

    public void createFile(String[] command) {
        if (command.length == 2) {
            try {
                Files.createFile(currentDirectory.resolve(command[1]));
            } catch (IOException e) {
                System.out.println("Error: can't create file with this name");
            }
        } else if (command.length == 1) {
            System.out.println("Error: input file name");
        } else {
            System.out.println("Error: too many arguments");
        }

    }

    public void addToFile(String[] command) {
        if (command.length == 1) {
            System.out.println("Error: input file name");
        } else if (command.length == 2) {
            Path file = currentDirectory.resolve(command[1]);
            if (!Files.isRegularFile(file)) {
                System.out.println("Error: no matching file found");
            } else {
                writeToFile(file);
            }
        }
    }

    private void writeToFile(Path file) {

        System.out.println("Reading text until type \"/finish\":");
        try(BufferedWriter bw = Files.newBufferedWriter(file))
        {
            String text;
            while(!(text=CommandReader.readCommand()).equals("/finish")){
                bw.write(text + "\n");
                bw.flush();
            }
        }
        catch(IOException ex){
            System.out.println("Error: " + ex.getMessage());
        }

    }

    public void printFile(String[] command) {

        if (command.length == 1) {
            System.out.println("Error: input file name");
        } else if (command.length == 3) {
            Path file = currentDirectory.resolve(command[1]);
            int numOfStrings = Integer.parseInt(command[2]);
            if (!Files.isRegularFile(file)) {
                System.out.println("Error: no matching file found");
            } else {
                try(BufferedReader br = Files.newBufferedReader(file))
                {
                    String text;
                    int k = 0;
                    while (((text=br.readLine()) != null) && (k != numOfStrings)) {
                        System.out.println(text);
                        k++;
                    }
                }
                catch(IOException ex){
                    System.out.println("Error: " + ex.getMessage());
                }
            }
        }
    }
}
