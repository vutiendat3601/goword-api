package tech.cdnl.goword.plan.services.impl;

import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import tech.cdnl.goword.plan.services.PlanServiceAsync;
import tech.cdnl.goword.shared.utils.FileUtil;

@Service
public class PlanServiceAsyncImpl implements PlanServiceAsync {
    @Value("${app.storage_dir}")
    private String storageDir;

    @Override
    public void saveAvatar(String filePath, MultipartFile avatar) {
        FileUtil.writeMultipartFile(Path.of(storageDir, filePath), avatar);
    }

}
