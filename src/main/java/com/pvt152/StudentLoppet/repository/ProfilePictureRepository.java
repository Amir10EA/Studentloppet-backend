package com.pvt152.StudentLoppet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.pvt152.StudentLoppet.model.ProfilePicture;

public interface ProfilePictureRepository extends JpaRepository<ProfilePicture, Long> {
    ProfilePicture findByFilename(String filename);
}
