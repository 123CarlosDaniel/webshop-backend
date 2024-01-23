package com.backend.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "file_tbl")
public class File {

    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    private String type;
    @Lob
    private byte[] data;

    private Date creationDate;
}
