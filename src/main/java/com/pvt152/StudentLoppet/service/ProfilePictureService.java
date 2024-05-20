package com.pvt152.StudentLoppet.service;

import com.pvt152.StudentLoppet.model.ProfilePicture;
import com.pvt152.StudentLoppet.model.User;
import com.pvt152.StudentLoppet.repository.ProfilePictureRepository;
import com.pvt152.StudentLoppet.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ProfilePictureService {

    @Autowired
    private ProfilePictureRepository profilePictureRepository;

    @Autowired
    private UserRepository userRepository;

    public User saveProfilePicture(String email, MultipartFile file) throws IOException {
        User user = userRepository.findById(email).orElseThrow(() -> new RuntimeException("User not found"));

        ProfilePicture profilePicture = new ProfilePicture(
                file.getBytes(),
                file.getOriginalFilename(),
                file.getContentType()
        );

        profilePicture = profilePictureRepository.save(profilePicture);

        user.setProfilePicture(profilePicture);
        return userRepository.save(user);
    }

    public ProfilePicture getProfilePicture(String filename) {
        return profilePictureRepository.findByFilename(filename);
    }
}