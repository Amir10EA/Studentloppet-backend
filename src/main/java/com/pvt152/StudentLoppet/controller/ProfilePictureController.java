package com.pvt152.StudentLoppet.controller;

import com.pvt152.StudentLoppet.model.ProfilePicture;
import com.pvt152.StudentLoppet.service.ProfilePictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
// Ändra så att bilden hämtas med user email
@RestController
@RequestMapping("/api/profile-pictures")
public class ProfilePictureController {

    @Autowired
    private ProfilePictureService profilePictureService;


    @PostMapping("/upload")
    public @ResponseBody String uploadProfilePicture(@RequestParam("email") String email, @RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return "File is missing";
        }
        try {
            profilePictureService.saveProfilePicture(email, file);
            return "Profile picture uploaded successfully";
        } catch (IOException e) {
            return "Error uploading profile picture";
        }
    }

    @GetMapping("/by-email/{email}")
    public ResponseEntity<byte[]> getProfilePictureByEmail(@PathVariable String email) {
        ProfilePicture profilePicture = profilePictureService.getProfilePictureByEmail(email);
        if (profilePicture == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(profilePicture.getMimeType()));
        headers.setContentDisposition(ContentDisposition.inline().filename(profilePicture.getFilename()).build());

        return new ResponseEntity<>(profilePicture.getImage(), headers, HttpStatus.OK);
    }

    @DeleteMapping("/remove/{email}")
    public ResponseEntity<String> removeProfilePicture(@PathVariable String email) {
        try {
            profilePictureService.removeProfilePicture(email);
            return ResponseEntity.ok("Profile picture removed successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error removing profile picture");
        }
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateProfilePicture(@RequestParam("email") String email, @RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is missing");
        }
        try {
            profilePictureService.updateProfilePicture(email, file);
            return ResponseEntity.ok("Profile picture updated successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating profile picture");
        }

    }
}
