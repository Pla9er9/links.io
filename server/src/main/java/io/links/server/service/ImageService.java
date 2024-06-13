package io.links.server.service;

import io.links.server.utils.LoggingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageService {

    @SuppressWarnings("FieldCanBeLocal")
    private final int targetHeight = 200;
    @SuppressWarnings("FieldCanBeLocal")
    private final int targetWidth = 200;
    private final LoggingUtils loggingUtils
            = LoggingUtils.getLoggingUtils(this.getClass());

    public Optional<byte[]> resizeImage(byte[] bytes) {
        try {
            var bufferedImage = bytesToBufferedImage(bytes);

            BufferedImage resizedImage = new BufferedImage(
                    targetWidth,
                    targetHeight,
                    BufferedImage.TYPE_INT_RGB
            );

            Graphics2D graphics2D = resizedImage.createGraphics();
            graphics2D.drawImage(
                    bufferedImage,
                    0, 0,
                    targetWidth,
                    targetHeight,
                    null
            );
            graphics2D.dispose();
            var byteArray = toByteArray(resizedImage);
            return Optional.of(byteArray);
        }
        catch (IOException e) {
            loggingUtils.logStackTraceAndMessage(e);
            return Optional.empty();
        }
    }

    private byte[] toByteArray(BufferedImage img) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ImageIO.write(img, "jpg", stream);
        return stream.toByteArray();
    }

    private BufferedImage bytesToBufferedImage(byte[] bytes) throws IOException {
        InputStream inputStream = new ByteArrayInputStream(bytes);
        return ImageIO.read(inputStream);
    }
}
