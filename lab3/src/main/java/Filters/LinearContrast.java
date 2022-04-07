package Filters;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public class LinearContrast {
    BufferedImage srcImg;

    float min;
    float max;

    public LinearContrast(BufferedImage srcImg) {
        this.srcImg = srcImg;
    }

    public BufferedImage filterImage() {
        assert (srcImg != null);

        int pixelCount = srcImg.getHeight() * srcImg.getWidth();
        var filtered = new int[pixelCount];
        var hsb = Util.getHsb(srcImg);

        var br = new Float[pixelCount];
        min = Arrays.stream(hsb.get(2).toArray(br)).min(Float::compare).get();
        max = Arrays.stream(hsb.get(2).toArray(br)).max(Float::compare).get();


        for (int k = 0; k < pixelCount; k++) {
            var b = hsb.get(2).get(k);
            hsb.get(2).set(k, 1 / (max - min) * (b - min));
        }

        for (int i = 0; i < pixelCount; i++) {
            filtered[i] = Color.HSBtoRGB(hsb.get(0).get(i),
                    hsb.get(1).get(i),
                    hsb.get(2).get(i));
        }
        return Util.createImageFromPixels(filtered, srcImg.getWidth(), srcImg.getHeight(), srcImg.getType());
    }
}
