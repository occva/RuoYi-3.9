package com.ruoyi.common.utils.file;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import com.ruoyi.common.exception.file.InvalidExtensionException;

class FileUploadUtilsTest {

    @Test
    void assertAllowedShouldRejectHtmlOnDefaultUpload() {
        MockMultipartFile file = new MockMultipartFile("file", "payload.html", "text/html", "<script>alert(1)</script>".getBytes());

        assertThrows(InvalidExtensionException.class,
                () -> FileUploadUtils.assertAllowed(file, MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION));
    }

    @Test
    void assertAllowedShouldAllowPngImage() {
        MockMultipartFile file = new MockMultipartFile("file", "cover.png", "image/png", new byte[] { 1, 2, 3 });

        assertDoesNotThrow(() -> FileUploadUtils.assertAllowed(file, MimeTypeUtils.IMAGE_EXTENSION));
    }
}
