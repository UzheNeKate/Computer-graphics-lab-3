package Filters;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

public class BarGraph {
    BufferedImage srcImg;

    public BarGraph(BufferedImage srcImg) {
        this.srcImg = srcImg;
    }

    BufferedImage getGrayscaleImage() {
        BufferedImage gImg = new BufferedImage(srcImg.getWidth(), srcImg.getHeight(),
                BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster wr = srcImg.getRaster();
        WritableRaster gr = gImg.getRaster();
        for (int i = 0; i < wr.getWidth(); i++) {
            for (int j = 0; j < wr.getHeight(); j++) {
                gr.setSample(i, j, 0, wr.getSample(i, j, 0));
            }
        }
        gImg.setData(gr);
        return gImg;
    }

    public BufferedImage equalizeRgb() {
        var src = getGrayscaleImage();
        BufferedImage nImg = new BufferedImage(src.getWidth(), src.getHeight(),
                BufferedImage.TYPE_BYTE_GRAY);
        int pixelCount = src.getWidth() * src.getHeight();
        var histogram = new int[3][256];

        for (int x = 0; x < src.getWidth(); x++) {
            for (int y = 0; y < src.getHeight(); y++) {
                var rgb = new Color(src.getRGB(x, y));
                histogram[0][rgb.getRed()]++;
                histogram[1][rgb.getGreen()]++;
                histogram[2][rgb.getBlue()]++;
            }
        }

        var chistogram = new int[3][256];
        chistogram[0][0] = histogram[0][0];
        chistogram[1][0] = histogram[1][0];
        chistogram[2][0] = histogram[2][0];
        for (int i = 1; i < 256; i++) {
            chistogram[0][i] = chistogram[0][i - 1] + histogram[0][i];
            chistogram[1][i] = chistogram[1][i - 1] + histogram[1][i];
            chistogram[2][i] = chistogram[2][i - 1] + histogram[2][i];
        }

        var arr = new float[3][256];
        for (int i = 0; i < 256; i++) {
            arr[0][i] = (float) ((chistogram[0][i] * 255.0) / (float) pixelCount);
            arr[1][i] = (float) ((chistogram[1][i] * 255.0) / (float) pixelCount);
            arr[1][i] = (float) ((chistogram[2][i] * 255.0) / (float) pixelCount);
        }

        var filtered = new int[pixelCount];
        for (int k = 0; k < pixelCount; k++) {
            int i = k % src.getWidth();
            int j = k / src.getWidth();
            var rgb = new Color(src.getRGB(i, j));
            var hsb = Color.RGBtoHSB((int) arr[0][rgb.getRed()], (int) arr[0][rgb.getGreen()],
                    (int) arr[0][rgb.getBlue()], null);
            filtered[k] = Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
        }
        return Util.createImageFromPixels(filtered, src.getWidth(), src.getHeight(), src.getType());
    }

    public BufferedImage equalizeV() {
        var src = getGrayscaleImage();
        BufferedImage nImg = new BufferedImage(src.getWidth(), src.getHeight(),
                BufferedImage.TYPE_BYTE_GRAY);
        int pixelCount = src.getWidth() * src.getHeight();
        var histogram = new int[256];

        var hsb = Util.getHsb(src);

        for (int i = 0; i < pixelCount; i++) {
            histogram[Math.round(hsb.get(2).get(i) * 255)]++;
        }

        var chistogram = new int[256];
        chistogram[0] = histogram[0];
        for (int i = 1; i < 256; i++) {
            chistogram[i] = chistogram[i - 1] + histogram[i];
        }

        var arr = new float[256];
        for (int i = 0; i < 256; i++) {
            arr[i] = (float) ((chistogram[i] * 255.0) / (float) pixelCount);
        }

        var filtered = new int[pixelCount];
        for (int k = 0; k < pixelCount; k++) {
            hsb.get(2).set(k, arr[Math.round(hsb.get(2).get(k) * 255)]);
            filtered[k] = Color.HSBtoRGB(hsb.get(0).get(k),
                    hsb.get(1).get(k),
                    hsb.get(2).get(k));
        }
        return Util.createImageFromPixels(filtered, src.getWidth(), src.getHeight(), src.getType());
    }
}
