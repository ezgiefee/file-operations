package com.motus.fileoperations.repository;

import com.motus.fileoperations.model.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<FileEntity,Long> {

}
