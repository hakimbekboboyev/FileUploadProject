package com.example.fileuploadproject.repository;

import com.example.fileuploadproject.entity.FileStorage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileStorageRepository extends JpaRepository<FileStorage,Integer> {
    FileStorage findFileStorageByHashId(String hashId);
}
