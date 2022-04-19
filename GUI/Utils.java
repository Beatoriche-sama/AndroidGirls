package GUI;

import Producers.Resource;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Utils {

    public static BufferedImage getScaledImage(Image srcImg, int w, int h) {
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TRANSLUCENT);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();
        return resizedImg;
    }

    public static String getColouredValue(Resource resource, int neededValue) {
        int resCurrentValue = (int) resource.getValue();
        String redColor = "<font color='red'>";
        String redColorEnd = "</font>";
        return resCurrentValue < neededValue ?
                redColor + resCurrentValue + redColorEnd
                        + " / " + neededValue
                : String.valueOf(resCurrentValue);
    }

}
