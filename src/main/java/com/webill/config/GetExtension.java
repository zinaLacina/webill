package com.webill.config;

public class GetExtension {

    public static String getValue(String fileName, int what){
        String extension = "";
        String nomFichier ="";

        int i = fileName.lastIndexOf('.');
        if (i >= 0) {
            extension = fileName.substring(i+1);
            nomFichier = fileName.substring(0,i);
        }
        if(what==0){
            return nomFichier;
        }
        return extension;
    }
}
