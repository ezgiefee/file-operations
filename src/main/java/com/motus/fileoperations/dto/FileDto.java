package com.motus.fileoperations.dto;

import jakarta.persistence.Lob;
import lombok.Setter;

@Setter
public class FileDto {
    private String fileName;
    private Long fileSize;
    private String fileExtension;
    @Lob
    private byte[] fileContent;
}
