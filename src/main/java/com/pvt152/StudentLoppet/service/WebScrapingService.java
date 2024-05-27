package com.pvt152.StudentLoppet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import com.pvt152.StudentLoppet.dto.RunnerInfoDTO;
import com.pvt152.StudentLoppet.model.MidnattsloppRunner;
import com.pvt152.StudentLoppet.model.User;
import com.pvt152.StudentLoppet.repository.MidnattsloppRunnerRepository;
import com.pvt152.StudentLoppet.repository.UserRepository;

@Service
public class WebScrapingService {

    @Autowired
    private MidnattsloppRunnerRepository runnerRepository;

    @Autowired
    private UserRepository userRepository;

    public RunnerInfoDTO getRunnerInfo(String email) {
        Optional<User> userOptional = userRepository.findById(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String fullName = user.getLastName().trim() + " " + user.getFirstName().trim();
            int yearOfBirth = user.getYearOfBirth();

            System.out.println("Searching, Name: '" + fullName + "', Birth year: " + yearOfBirth);

            Optional<MidnattsloppRunner> runnerOptional = runnerRepository.findByNameAndYearBorn(fullName, yearOfBirth);

            if (runnerOptional.isPresent()) {
                MidnattsloppRunner runner = runnerOptional.get();
                System.out.println("user match found: " + runner);
                return new RunnerInfoDTO(true, runner.getStartNumber(), runner.getClubOrCityOrCompany(),
                        runner.getStartGroup());
            } else {
                System.out.println("no runner was found with name: '" + fullName + "', and birth year: " + yearOfBirth);
            }
        } else {
            System.out.println("email not found: " + email);
        }
        return new RunnerInfoDTO(false, null, null, null);
    }
}