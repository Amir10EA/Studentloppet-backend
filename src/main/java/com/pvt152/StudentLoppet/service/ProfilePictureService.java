package com.pvt152.StudentLoppet.service;

import com.pvt152.StudentLoppet.model.ProfilePicture;
import com.pvt152.StudentLoppet.model.User;
import com.pvt152.StudentLoppet.repository.ProfilePictureRepository;
import com.pvt152.StudentLoppet.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class ProfilePictureService {
    @Autowired
    private ProfilePictureRepository profilePictureRepository;
    @Autowired
    private UserRepository userRepository;

    public User saveProfilePicture(String email, MultipartFile file) throws IOException {
        User user = userRepository.findById(email).orElseThrow(() -> new RuntimeException("User not found"));

        // Generate a unique filename
        String originalFilename = file.getOriginalFilename();
        String uniqueFilename = UUID.randomUUID().toString() + "_" + originalFilename;

        ProfilePicture profilePicture = new ProfilePicture(
                file.getBytes(),
                uniqueFilename,
                file.getContentType()
        );

        profilePicture = profilePictureRepository.save(profilePicture);

        user.setProfilePicture(profilePicture);
        return userRepository.save(user);
    }

    public ProfilePicture getProfilePictureByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            return user.getProfilePicture();
        }
        return null;
    }
    public void removeProfilePicture(String email) {
        User user = userRepository.findById(email).orElseThrow(() -> new RuntimeException("User not found"));
        ProfilePicture profilePicture = user.getProfilePicture();
        if (profilePicture != null) {
            user.setProfilePicture(null);
            userRepository.save(user);
            profilePictureRepository.delete(profilePicture);
        }
    }

    public User updateProfilePicture(String email, MultipartFile file) throws IOException {
        removeProfilePicture(email);
        return saveProfilePicture(email, file);
    }
}