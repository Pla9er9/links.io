package io.links.server.task;

import io.links.server.model.Image;
import io.links.server.model.User;
import io.links.server.repository.ImageRepository;
import io.links.server.repository.UserRepository;
import io.links.server.service.ImageService;
import io.links.server.worker.ImageTaskWorker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Optional;
import java.util.TimerTask;

@Slf4j
@RequiredArgsConstructor
@Component
public class ResizeAndSaveImageTask extends TimerTask {

    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final ImageService imageService;

    @Override
    public void run() {
        var imageID = ImageTaskWorker.queueOfImagesToProcess.poll();
        if (imageID == null) {
            return;
        }

        var imageOptional = imageRepository.findById(imageID);
        if (imageOptional.isEmpty()) {
            return;
        }

        Image image = imageOptional.get();
        User imageOwner = image.getUser();

        var bytes = image.getImage().getData();

        var resizedImageOptional = imageService.resizeImage(bytes);
        if (resizedImageOptional.isEmpty()) return;

        saveAvatar(resizedImageOptional.get(), imageOwner);
        imageRepository.deleteById(imageID);
    }

    public void saveAvatar(byte[] image, User user) {
        var binary = new Binary(
                BsonBinarySubType.BINARY,
                image
        );
        user.setAvatar(binary);
        userRepository.save(user);
    }
}
