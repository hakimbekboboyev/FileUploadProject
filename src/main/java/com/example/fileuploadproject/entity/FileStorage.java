package com.example.fileuploadproject.entity;

import com.example.fileuploadproject.entity.enumFiles.FileStorageStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class FileStorage implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String extension;
    private Long fileSize;
    private String contentType;
    private String hashId;
    private String uploadFolder;

    private FileStorageStatus fileStorageStatus;
}
