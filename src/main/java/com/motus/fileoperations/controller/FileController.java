package com.motus.fileoperations.controller;

import com.motus.fileoperations.dto.FileDto;
import com.motus.fileoperations.exception.FileSizeLimitExceededException;
import com.motus.fileoperations.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Files", description = "Manage file operations")
@RestController
@RequestMapping(value = "/v1/files", produces = "application/json")
public class FileController {

    @Autowired
    FileService fileService;

    @Operation(summary = "Save Files to Database")
    @PostMapping("/all")
    public ResponseEntity<Void> saveFilesToDatabase(
            @Parameter(description = "Server url for reading files from", example = "/users/files")
            @RequestParam String folderPath) {

        fileService.saveAllFiles(folderPath);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @Operation(summary = "Get Files From Database")
    @Schema(title = "Get Files From Database", description = "Returning the list of files that saved to database")
    @GetMapping("/all")
    public ResponseEntity<List<FileDto>> getFilesFromDatabase() {
        return new ResponseEntity<>(fileService.getAllFiles(), HttpStatus.OK);
    }

    @Operation(summary = "Get A File By Its Id")
    @Schema(title = "Get A File By Its Id", description = "Returning a file by its id from database")
    @GetMapping("/{id}")
    public ResponseEntity<FileDto> getFileById(
            @Parameter(description = "The file's id to retrieve", example = "12345")
            @PathVariable Long id) {
        return new ResponseEntity<>(fileService.getFileById(id), HttpStatus.OK);
    }

    @Operation(summary = "Get Content of A Specific File")
    @Schema(title = "Get Content of A Specific File", description = "Return the content of a file as a byte array")
    @GetMapping("/content/{id}")
    public ResponseEntity<byte[]> getFileContent(
            @Parameter(description = "The file's id to get its content", example = "12345")
            @PathVariable Long id) {
        return new ResponseEntity<>(fileService.getFileContent(id), HttpStatus.OK);
    }

    @Operation(summary = "Update a file's name")
    @Schema(title = "Update a File's Name", description = "Update the file's name on the database")
    @PutMapping("/{id}")
    public ResponseEntity<FileDto> updateFile(
            @Parameter(description = "The file's id to update", example = "12345")
            @PathVariable Long id,
            @Parameter(description = "The new name to change the file's name", example = "Oedipus")
            @RequestParam String fileName) {
        try {
            return new ResponseEntity<>(fileService.updateFile(id, fileName), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @Operation(summary = "Delete a file from database")
    @Schema(title = "Delete a File From Database", description = "Delete a file that is stored on database")
    @DeleteMapping("/{id}")
    public ResponseEntity deleteFile(
            @Parameter(description = "The file's id to delete", example = "12345")
            @PathVariable Long id) {

        fileService.deleteFile(id);
        return new ResponseEntity<>(HttpStatus.OK);

    }
}
