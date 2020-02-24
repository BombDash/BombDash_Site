package net.bombdash.core.other;

import ch.qos.logback.core.rolling.helper.CompressionMode;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Component
public class ProfileWindowGenerator {
    public byte[] generateProfileWindow(String nickname) {
        BufferedImage image;
        try {
            image = getImage(nickname);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new byte[0];
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
            ImageOutputStream out = ImageIO.createImageOutputStream(bos);
            writer.setOutput(out);
            ImageWriteParam param = writer.getDefaultWriteParam();
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(1F);
            writer.write(null, new IIOImage(image, null, null), param);
        } catch (IOException e) {
            return new byte[0];
        }
        return bos.toByteArray();
    }

    public BufferedImage getImage(String nickname) throws Exception {
        BufferedImage window = ImageIO.read(new ClassPathResource("/other/profile_window/window.png")
                .getInputStream());
        InputStream fontStream = new ClassPathResource("/other/profile_window/font.ttf").getInputStream();
        Font font = Font.createFont(Font.TRUETYPE_FONT, fontStream);
        font = font.deriveFont(Font.PLAIN, 40);
        Graphics2D g = (Graphics2D) window.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setFont(font);
        g.drawString(nickname, 374, 249);
        return window;
    }
}
