package com.motus.fileoperations.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Schema(
        title = "File Model",
        description = "Parameters required to create or update a file",
        requiredMode = Schema.RequiredMode.REQUIRED
)
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    private Long fileSize;
    private String fileExtension;
    @Lob
    private byte[] fileContent;

}
