package tech.cdnl.goword.shared.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import tech.cdnl.goword.exceptions.AppErrorMessage;

public class FileUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);

    public static void writeMultipartFile(Path path, MultipartFile multipartFile) {
        try (OutputStream fileOutput = new FileOutputStream(path.toFile());) {
            LOGGER.info("[ %s ] Create file at path: %s".formatted(FileUtil.class.getSimpleName(), path + ""));
            if (!Files.exists(path)) {
                Files.createFile(path);
            }
            fileOutput.write(multipartFile.getBytes());
        } catch (IOException e) {
            LOGGER.error("[ %s ] %s".formatted(e.getClass().getSimpleName(), e.getMessage()));
            throw new RuntimeException(AppErrorMessage.FILE_CREATE_ERROR);
        }
    }
}
