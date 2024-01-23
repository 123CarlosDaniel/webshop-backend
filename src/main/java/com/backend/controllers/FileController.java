package com.backend.controllers;

import com.backend.models.entities.File;
import com.backend.response.ResponseFile;
import com.backend.response.ResponseMessage;
import com.backend.services.interfaces.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/v1/file-manager")
public class FileController {
    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseMessage> upload(@RequestParam("file") MultipartFile file) throws IOException {
        fileService.store(file);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Item uploaded successfully"));
    }

    @GetMapping("/files/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable UUID id, @RequestHeader(value = HttpHeaders.IF_MODIFIED_SINCE, required = false) String ifModifiedSince) throws FileNotFoundException {
        File fileEntity = fileService.getFile(id).orElseThrow();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(fileEntity.getType()));
        headers.setContentDisposition(ContentDisposition.inline().filename(fileEntity.getName()).build());

        CacheControl cacheControl = CacheControl.maxAge(1, TimeUnit.DAYS);
        headers.setCacheControl(cacheControl.getHeaderValue());
        headers.setLastModified(fileEntity.getCreationDate().getTime());

        if (ifModifiedSince != null) {
            try {
                Date ifModifiedSinceDate = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US).parse(ifModifiedSince);
                if (!fileEntity.getCreationDate().after(ifModifiedSinceDate)) {
                    return ResponseEntity.status(HttpStatus.NOT_MODIFIED).headers(headers).build();
                }
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        return ResponseEntity.status(HttpStatus.OK)
                .headers(headers)
                .body(fileEntity.getData());
    }

    @GetMapping("/files")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ResponseFile>> getListFiles() {
        List<ResponseFile> files = fileService.getAllFiles();
        return ResponseEntity.status(HttpStatus.OK).body(files);
    }
}









