package io.links.server.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

@Service
@Slf4j
public class ImageService {

    private final int targetWidth = 200;
    private final int targetHeight = 200;

    public byte[] resizeImage(BufferedImage originalImage) {
        BufferedImage resizedImage = new BufferedImage(
                targetWidth,
                targetHeight,
                BufferedImage.TYPE_INT_RGB
        );
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(
                originalImage,
                0, 0,
                targetWidth,
                targetHeight,
                null
        );
        graphics2D.dispose();

        try {
            return toByteArray(resizedImage);
        } catch (IOException e) {
            log.info(
                    Arrays.toString(e.getStackTrace())
            );
            log.info(e.getMessage());
            return new byte[]{};
        }
    }

    private byte[] toByteArray(BufferedImage img) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ImageIO.write(img, "jpg", stream);
        return stream.toByteArray();
    }
}
