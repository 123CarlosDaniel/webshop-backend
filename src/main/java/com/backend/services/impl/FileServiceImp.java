package com.backend.services.impl;

import com.backend.models.entities.File;
import com.backend.repository.FileRepository;
import com.backend.response.ResponseFile;
import com.backend.services.interfaces.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FileServiceImp implements FileService {

    @Autowired
    private FileRepository fileRepository;

    @Override
    public File store(MultipartFile multipartFile) throws IOException {
        String fileName = multipartFile.getOriginalFilename();
        File file = File.builder()
                .name(fileName)
                .data(multipartFile.getBytes())
                .type(multipartFile.getContentType())
                .creationDate(new Date())
                .build();
        return fileRepository.save(file);
    }

    @Override
    public Optional<File> getFile(UUID id) throws FileNotFoundException {
        Optional<File> file = fileRepository.findById(id);
        if (file.isPresent()) {
            return file;
        }
        throw new FileNotFoundException();
    }

    @Override
    public List<ResponseFile> getAllFiles() {
        List<ResponseFile> files = fileRepository.findAll().stream().map(file -> {
                    String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                            .path("api/v1/file-manager/files/")
                            .path(file.getId().toString())
                            .toUriString();
                    return ResponseFile.builder()
                            .name(file.getName())
                            .url(fileDownloadUri)
                            .type(file.getType())
                            .size(file.getData().length)
                            .build();
                })
                .toList();

        return files;
    }
}






