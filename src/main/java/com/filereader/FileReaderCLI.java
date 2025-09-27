package com.filereader;

import java.io.*;
import java.nio.file.*;
import java.util.List;

/**
 * Command-line version of the file reader for testing purposes
 */
public class FileReaderCLI {
    
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java com.filereader.FileReaderCLI <folder-path>");
            System.out.println("This is a test version to verify the file reading functionality.");
            return;
        }
        
        String folderPath = args[0];
        File folder = new File(folderPath);
        
        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("Error: The specified path is not a valid directory.");
            return;
        }
        
        System.out.println("Reading files from folder: " + folder.getAbsolutePath());
        System.out.println("=" + "=".repeat(50));
        
        listFilesRecursively(folder, "");
    }
    
    private static void listFilesRecursively(File directory, String indent) {
        File[] files = directory.listFiles();
        if (files != null) {
            java.util.Arrays.sort(files, (f1, f2) -> {
                if (f1.isDirectory() && !f2.isDirectory()) {
                    return -1;
                } else if (!f1.isDirectory() && f2.isDirectory()) {
                    return 1;
                } else {
                    return f1.getName().compareToIgnoreCase(f2.getName());
                }
            });
            
            for (File file : files) {
                System.out.println(indent + (file.isDirectory() ? "[DIR] " : "[FILE] ") + file.getName());
                
                if (file.isFile() && file.length() < 1024) { // Show content of small files
                    try {
                        List<String> lines = Files.readAllLines(file.toPath());
                        if (!lines.isEmpty()) {
                            System.out.println(indent + "  Content preview:");
                            lines.stream().limit(3).forEach(line -> 
                                System.out.println(indent + "    " + line));
                            if (lines.size() > 3) {
                                System.out.println(indent + "    ... (" + (lines.size() - 3) + " more lines)");
                            }
                        }
                    } catch (IOException e) {
                        System.out.println(indent + "  (Binary or unreadable file)");
                    }
                }
                
                if (file.isDirectory()) {
                    listFilesRecursively(file, indent + "  ");
                }
            }
        }
    }
}