package com.pvt152.StudentLoppet.controller;

import com.pvt152.StudentLoppet.model.ProfilePicture;
import com.pvt152.StudentLoppet.service.ProfilePictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
            throw new IllegalArgumentException("Error uploading profile picture", e);
        }
    }


    @GetMapping("/{filename}")
    public ResponseEntity<byte[]> getProfilePicture(@PathVariable String filename) {
        ProfilePicture profilePicture = profilePictureService.getProfilePicture(filename);
        if (profilePicture == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.parseMediaType(profilePicture.getMimeType()));
    headers.setContentDisposition(ContentDisposition.inline().filename(profilePicture.getFilename()).build());

    return new ResponseEntity<>(profilePicture.getImage(), headers, HttpStatus.OK);
        }
    }
