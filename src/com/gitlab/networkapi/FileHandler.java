package com.gitlab.networkapi;

import java.io.File;
import java.io.FileNotFoundException;

public class FileHandler {
    private File file;

    public FileHandler(String filename) throws FileNotFoundException {
        file = new File(filename);

        /**
         * TODO: If a requested public and private keypair doesn't exist then depending on how the project is
         *  configured we should either create them or exit
         */
        if (!file.isFile()) {
            throw new FileNotFoundException("The file: " + filename + " couldn't be found.");
        }
    }

    public String getName() {
        return file.getAbsoluteFile().toString();
    }
}