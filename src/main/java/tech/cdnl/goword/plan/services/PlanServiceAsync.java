package tech.cdnl.goword.plan.services;

import org.springframework.scheduling.annotation.Async;
import org.springframework.web.multipart.MultipartFile;

public interface PlanServiceAsync {
    @Async
    void saveThumb(String filePath, MultipartFile thumb);
}
