package tech.cdnl.goword.exercise.serivces;

import org.springframework.scheduling.annotation.Async;
import org.springframework.web.multipart.MultipartFile;

public interface ExerciseServiceAsync {
    @Async
    void saveThumb(String filePath, MultipartFile thumb);
}
