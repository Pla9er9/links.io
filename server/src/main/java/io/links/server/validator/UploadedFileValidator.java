package io.links.server.validator;

import io.links.server.exception.ValidationException;
import org.springframework.web.multipart.MultipartFile;

public class UploadedFileValidator {

    public static void validate(MultipartFile file, String expectedContentTypeStartsWith) {
        if (file == null) {
            throw new ValidationException("File not provided");
        } else if (
                file.getContentType() == null ||
                !file.getContentType().startsWith(
                        expectedContentTypeStartsWith
                ))
        {
            throw new ValidationException("File content type not provided or wrong");
        }
    }
}
