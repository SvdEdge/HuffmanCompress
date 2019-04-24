package com.alevel;


import java.util.Scanner;

import static com.alevel.Configs.COMPRESSED_FILENAME;
import static com.alevel.Configs.COMPRESSED_FILE_EXTENSION;
import static com.alevel.Configs.METADATA_TABLE_FILENAME;
import static com.alevel.IOUtils.checkFilenameExtension;
import static com.alevel.IOUtils.getDecompressionFileName;


public class Main {



    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the initial file name: ");
        String initialFileName = scanner.nextLine();
        String inputFileExtension = checkFilenameExtension(initialFileName);
        Processor processor;
        if (COMPRESSED_FILE_EXTENSION.equals(inputFileExtension)) {
            String decompressionFileName = getDecompressionFileName();
            processor = new Decompressor(initialFileName, METADATA_TABLE_FILENAME, decompressionFileName);
        } else {
            processor = new Comperssor(initialFileName, COMPRESSED_FILENAME);
        }
        processor.process().save();
    }

}