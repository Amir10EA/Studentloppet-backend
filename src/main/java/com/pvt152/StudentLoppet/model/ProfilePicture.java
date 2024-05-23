package com.pvt152.StudentLoppet.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Base64;

@Entity
public class ProfilePicture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    @JsonIgnore
    private byte[] image;

    @Column(unique = true)
    private String filename;

    @Column(name = "mime_type")
    private String mimeType;

    public ProfilePicture() {}

    public ProfilePicture(byte[] image, String filename, String mimeType) {
        this.image = image;
        this.filename = filename;
        this.mimeType = mimeType;
    }

    public String getImageAsBase64() {
        return Base64.getEncoder().encodeToString(this.image);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
}
