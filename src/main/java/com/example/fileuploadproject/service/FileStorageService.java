package com.example.fileuploadproject.service;

import com.example.fileuploadproject.entity.FileStorage;
import com.example.fileuploadproject.entity.enumFiles.FileStorageStatus;
import com.example.fileuploadproject.repository.FileStorageRepository;
import lombok.Data;
import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;

@Service
public class FileStorageService {
    final private FileStorageRepository fileStorageRepository;

    private final Hashids hashids;

    @Value("${upload.server.folder}")
    private String ServerFolderPath;

    public FileStorageService(FileStorageRepository fileStorageRepository) {
        this.fileStorageRepository = fileStorageRepository;
        this.hashids = new Hashids(getClass().getName(),6);
    }

    public FileStorage save(MultipartFile multipartFile){
        FileStorage fileStorage = new FileStorage();
        fileStorage.setName(multipartFile.getName());
        fileStorage.setFileSize(multipartFile.getSize());
        fileStorage.setContentType(multipartFile.getContentType());
        fileStorage.setFileStorageStatus(FileStorageStatus.DRAFT);
        fileStorage.setExtension(getExt(multipartFile.getOriginalFilename()));
        fileStorage = fileStorageRepository.save(fileStorage);

        Date now = new Date();

        String path = String.format("%s/upload_file/%d/%d/%d",
                this.ServerFolderPath,
                now.getYear()+1900,
                now.getMonth()+1,
                now.getDate());

        File uploadFolder = new File(path);

        if(!uploadFolder.exists() && uploadFolder.mkdir()){
            System.out.println("Created Folder!");
        }

        fileStorage.setHashId(hashids.encode(fileStorage.getId()));
        fileStorage.setUploadFolder(path+"/"+ fileStorage.getHashId()+"."+fileStorage.getExtension());
        fileStorageRepository.save(fileStorage);

        uploadFolder = uploadFolder.getAbsoluteFile();
        File file = new File(uploadFolder,
                String.format("%s.%s",
                fileStorage.getHashId(),
                fileStorage.getExtension()));


        try {
            multipartFile.transferTo(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return fileStorage;
    }

    private String getExt(String originalName){
        String ext = null;

        if(originalName!=null && !originalName.isEmpty()){
            int dot = originalName.lastIndexOf('.');
            if(dot>0 && originalName.length()-2>=dot){
                ext = originalName.substring(dot+1);
            }
        }

        return ext;
    }
}
