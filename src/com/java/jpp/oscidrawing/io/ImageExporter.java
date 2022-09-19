package com.java.jpp.oscidrawing.io;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.FileAlreadyExistsException;

public class ImageExporter {
    public static boolean writeToPNG(String pathWithoutSuffix, BufferedImage img) {
        try {
            String path = pathWithoutSuffix + ".png";
            File outputImage = new File(path);
            ImageIO.write(img, "PNG", outputImage);
        } catch(Exception e){
            return false;
        }
        return true;
    }
}
