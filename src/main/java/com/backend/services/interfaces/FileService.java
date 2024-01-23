package com.backend.services.interfaces;

import com.backend.models.entities.File;
import com.backend.response.ResponseFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FileService {
    File store(MultipartFile multipartFile) throws IOException;

    Optional<File> getFile(UUID id) throws FileNotFoundException;

    List<ResponseFile> getAllFiles();
}
