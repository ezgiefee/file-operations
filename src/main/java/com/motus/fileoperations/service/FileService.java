package com.motus.fileoperations.service;

import com.motus.fileoperations.dto.FileDto;
import com.motus.fileoperations.exception.FileSizeLimitExceededException;
import com.motus.fileoperations.model.FileEntity;
import com.motus.fileoperations.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileService {
    private final FileRepository fileRepository;
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;

    @Autowired
    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }
    public void saveAllFiles(String folderPath) {
        File folder = new File(folderPath);
        File[] files = folder.listFiles();
        try {
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        if (file.length() > MAX_FILE_SIZE) {
                            throw new FileSizeLimitExceededException("File size exceeds the limit");
                        }
                        FileEntity fileEntity = new FileEntity();
                        String fileName = file.getName();
                        fileEntity.setFileName(file.getName());
                        int index = fileName.lastIndexOf('.');
                        fileEntity.setFileExtension(fileName.substring(index+1));
                        fileEntity.setFileSize(file.length());
                        fileEntity.setFileContent(Files.readAllBytes(file.toPath()));
                        fileRepository.save(fileEntity);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<FileDto> getAllFiles(){
        List<FileEntity> fileEntities = fileRepository.findAll();
        List<FileDto> fileDtos = new ArrayList<>();
        for (FileEntity fileEntity: fileEntities) {
            FileDto fileDto = new FileDto();
            fileDto.setFileName(fileEntity.getFileName());
            fileDto.setFileExtension(fileEntity.getFileExtension());
            fileDto.setFileSize(fileEntity.getFileSize());
            fileDto.setFileContent(fileEntity.getFileContent());
            fileDtos.add(fileDto);
        }
        return fileDtos;
    }

    public FileDto getFileById(Long fileId){
        FileEntity fileEntity = fileRepository.findById(fileId).orElse(null);
        FileDto fileDto = new FileDto();
        fileDto.setFileName(fileEntity.getFileName());
        fileDto.setFileExtension(fileEntity.getFileExtension());
        fileDto.setFileSize(fileEntity.getFileSize());
        fileDto.setFileContent(fileEntity.getFileContent());
        return fileDto;
    }

    public byte[] getFileContent(Long fileId){
        FileEntity fileEntity = fileRepository.findById(fileId).orElse(null);
        if(fileEntity != null){
            return fileEntity.getFileContent();
        }
        return null;
    }

    public FileDto updateFile(Long fileId, String fileName){
        FileEntity fileEntity = fileRepository.findById(fileId).orElse(null);
        if(fileEntity != null){
            fileEntity.setFileName(fileName);
            fileRepository.save(fileEntity);
            FileDto fileDto = new FileDto();
            fileDto.setFileName(fileEntity.getFileName());
            fileDto.setFileExtension(fileEntity.getFileExtension());
            fileDto.setFileSize(fileEntity.getFileSize());
            fileDto.setFileContent(fileEntity.getFileContent());
            return fileDto;
        }
        return null;
    }

    public void deleteFile(Long fileId){
        FileEntity fileEntity = fileRepository.findById(fileId).orElse(null);
        if(fileEntity != null){
            fileRepository.deleteById(fileId);
        }
    }
}
