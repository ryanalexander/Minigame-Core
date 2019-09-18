package net.blockcade.Arcade.Utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

public class Utils {
    public static Image getImageFromURL(String url) throws MalformedURLException {
        File f = new File(url);
        BufferedImage image = null;

        if (!f.exists()) {
            System.out.println("FILE DOESN'T EXIST");
            return null;
        }

        try {
            image = ImageIO.read(f);
        }
        catch (IOException e) {
            System.out.println("I'm not smart enough to open the file");
        }

        return image;
    }
}
