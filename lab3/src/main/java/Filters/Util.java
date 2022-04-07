package Filters;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.function.Consumer;

public class Util {
    public static ArrayList<ArrayList<Float>> getHsb(BufferedImage srcImg){
        var hues = new ArrayList<Float>();
        var saturations = new ArrayList<Float>();
        var brightnesses = new ArrayList<Float>();

        for (int i = 0; i < srcImg.getHeight(); i++) {
            for (int j = 0; j < srcImg.getWidth(); j++) {
                var rgb = new Color(srcImg.getRGB(j, i));
                var hsb = Color.RGBtoHSB(rgb.getRed(), rgb.getGreen(), rgb.getBlue(), null);
                hues.add(hsb[0]);
                saturations.add(hsb[1]);
                brightnesses.add(hsb[2]);
            }
        }

        var hsb = new ArrayList<ArrayList<Float>>();
        hsb.add(hues);
        hsb.add(saturations);
        hsb.add(brightnesses);

        return hsb;
    }

    public static int getMedian(BufferedImage srcImg, int x, int y, int radius, Consumer<ArrayList<Float>> sort) {
        var hues = new ArrayList<Float>();
        var saturations = new ArrayList<Float>();
        var brightnesses = new ArrayList<Float>();

        for (int i = -radius; i <= radius; ++i) {
            for (int j = -radius; j <= radius; ++j) {
                if (x + i >= 0 && x + i < srcImg.getWidth() && y + j >= 0 && y + j < srcImg.getHeight()) {
                    var rgb = new Color(srcImg.getRGB(x + i, y + j));
                    var hsb = Color.RGBtoHSB(rgb.getRed(), rgb.getGreen(), rgb.getBlue(), null);
                    hues.add(hsb[0]);
                    saturations.add(hsb[1]);
                    brightnesses.add(hsb[2]);
                }
            }
        }
        sort.accept(hues);
        sort.accept(saturations);
        sort.accept(brightnesses);

        int mid = hues.size() / 2;
        return Color.HSBtoRGB(hues.get(mid), saturations.get(mid), brightnesses.get(mid));
    }

    public static BufferedImage createImageFromPixels(int[] pixels, int width, int height, int imgType) {
        var newImg = new BufferedImage(width, height, imgType);
        newImg.setRGB(0, 0, width, height, pixels, 0, width);
        return newImg;
    }
}
