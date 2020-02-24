package net.bombdash.core.other;

import lombok.SneakyThrows;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ProfilePhotoGenerator {
    private Set<ImageWrapper> images = Collections.newSetFromMap(new ConcurrentHashMap<>());

    private class ImageWrapper {
        private SoftReference<BufferedImage> image;
        private final String path;

        ImageWrapper(String path) {
            this.path = path;
            this.image = new SoftReference<>(null);
        }


        public BufferedImage getImage(int size) throws IOException {
            BufferedImage baseImage = image.get();
            if (baseImage == null) {
                baseImage = resizeImage(ImageIO.read(new ClassPathResource(path).getInputStream()), 256);
                image = new SoftReference<>(baseImage);
            }
            return resizeImage(baseImage, size);
        }
    }


    private ImageWrapper getBasicImage(String path) {
        ImageWrapper w = images
                .stream()
                .filter(i -> i.path.equals(path))
                .findFirst()
                .orElse(null);
        if (w == null) {
            w = new ImageWrapper(path);
            images.add(w);
        }
        return w;

    }


    public BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    private List<Integer> allowedSize = Arrays.asList(32, 64, 256);

    @SneakyThrows
    public BufferedImage generateImage(String character, String strColor, String strHighlight, String strSize) {
        Color color;
        Color highlight;
        int size;
        try {
            color = new Color(Integer.parseInt(strColor));
            highlight = new Color(Integer.parseInt(strHighlight));
            size = Integer.parseInt(strSize);
            if (!allowedSize.contains(size))
                size = 16;
        } catch (Exception ex) {
            size = 256;
            color = Color.RED;
            highlight = Color.YELLOW;
        }
        BufferedImage icon = null, mask = null;
        try {
            String basePath = "characters/" + character;
            icon = getBasicImage(basePath + "Icon.png").getImage(size);
            mask = getBasicImage(basePath + "IconColorMask.png").getImage(size);
        } catch (IOException ignored) {
        }
        if (icon == null || mask == null) {
            String basePath = "characters/neoSpaz";
            icon = getBasicImage(basePath + "Icon.png").getImage(size);
            mask = getBasicImage(basePath + "IconColorMask.png").getImage(size);
        }
        BufferedImage frame = getBasicImage("characters/frame.png").getImage(size);
        multiplyMask(icon, mask, color, true);
        multiplyMask(icon, mask, highlight, false);
        frame(icon, frame, color);
        return icon;

    }

    private void frame(BufferedImage icon, BufferedImage frame, Color color) {

        for (int x = 0; x < frame.getWidth(); x++) {
            for (int y = 0; y < frame.getHeight(); y++) {
                Color currentFramePixel = new Color(frame.getRGB(x, y), true);
                if (currentFramePixel.getAlpha() == 0) {
                    icon.setRGB(x, y, 0);
                    continue;
                }
                Color currentIconPixel = new Color(icon.getRGB(x, y));
                int alpha = currentFramePixel.getGreen();
                Color placeColor = overlay(
                        0, 0, 0, 255,
                        color.getRed(), color.getGreen(), color.getBlue(), alpha
                );
                int redAlpha = 255 - currentFramePixel.getRed();
                placeColor = overlay(
                        currentIconPixel.getRed(), currentIconPixel.getGreen(), currentIconPixel.getBlue(), 255,
                        placeColor.getRed(), placeColor.getGreen(), placeColor.getBlue(), redAlpha

                );
                int blueAlpha = currentFramePixel.getBlue();
                placeColor = overlay(
                        placeColor.getRed(), placeColor.getGreen(), placeColor.getBlue(), 255,
                        255, 255, 255, blueAlpha

                );
                placeColor = new Color(placeColor.getRed(), placeColor.getGreen(), placeColor.getBlue(), currentFramePixel.getAlpha());
                icon.setRGB(x, y, placeColor.getRGB());
            }
        }
    }

    public BufferedImage resizeImage(BufferedImage original, int size) {
        BufferedImage scaledImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = scaledImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.drawImage(original, 0, 0, size, size, null);
        graphics2D.dispose();
        return scaledImage;
    }

    public void multiplyMask(BufferedImage image, BufferedImage mask, Color color, boolean mainColor) {

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                Color iconPixel = new Color(image.getRGB(x, y));
                Color maskPixel = new Color(mask.getRGB(x, y));
                int alpha;
                if (mainColor)
                    alpha = maskPixel.getRed();
                else
                    alpha = maskPixel.getGreen();
                int r = (int) Math.floor(iconPixel.getRed() * ((double) color.getRed() / 255D));
                int g = (int) Math.floor(iconPixel.getGreen() * ((double) color.getGreen() / 255D));
                int b = (int) Math.floor(iconPixel.getBlue() * ((double) color.getBlue() / 255D));
                Color placeColor = overlay(
                        iconPixel.getRed(), iconPixel.getGreen(), iconPixel.getBlue(), iconPixel.getAlpha(),
                        r, g, b, alpha
                );
                image.setRGB(x, y, placeColor.getRGB());
            }
        }
    }

    private Color overlay(Color color1, Color color2) {
        return overlay(
                color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha(),
                color2.getRed(), color2.getGreen(), color2.getBlue(), color2.getAlpha()
        );
    }

    private Color overlay(int r1, int g1, int b1, int a1, int r2, int g2, int b2, int a2) {
        if (a1 == 0)
            return new Color(0, 0, 0, 0);
        else
            return new Color(
                    (r2 * a2 + r1 * (255 - a2)) / a1,
                    (g2 * a2 + g1 * (255 - a2)) / a1,
                    (b2 * a2 + b1 * (255 - a2)) / a1
            );
    }


    public byte[] generateImageByte(String size, String character, String color, String highlight) {
        BufferedImage image = generateImage(character, color, highlight, size);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", baos);
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }
}
