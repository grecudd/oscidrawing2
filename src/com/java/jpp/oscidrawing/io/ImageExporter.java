package com.java.jpp.oscidrawing.io;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class ImageExporter {
    public static boolean writeToPNG(String pathWithoutSuffix, BufferedImage img) {
        try {
            String path = pathWithoutSuffix + ".pgn";
            File outputImage = new File(path);

            if(outputImage.exists()){
                throw new Exception("Existent file");
            }

            ImageIO.write(img, ".pgn", outputImage);
        } catch(Exception e){
            return false;
        }

        return true;
    }
}
