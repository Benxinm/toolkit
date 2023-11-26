package org.phenomenal.toolkit.utils;

public class FileUtil {
    static String[] imageExtension = {".jpg",".jpeg",".png"};
    public static String getImageExtension(String filename){
        for (int i = 0; i < imageExtension.length; i++) {
            if (filename.endsWith(imageExtension[i])){
                return imageExtension[i];
            }
        }
        return null;
    }
}
