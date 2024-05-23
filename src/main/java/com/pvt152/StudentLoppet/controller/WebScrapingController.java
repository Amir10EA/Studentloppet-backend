package com.pvt152.StudentLoppet.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import com.pvt152.StudentLoppet.CountdownExtractor;
import com.pvt152.StudentLoppet.dto.RunnerInfoDTO;

import com.pvt152.StudentLoppet.service.WebScrapingService;

@RestController
@RequestMapping("/scrape")
public class WebScrapingController {

    @Autowired
    private CountdownExtractor countdownExtractor;

    @Autowired
    private WebScrapingService runnerInfoService;

    @GetMapping("/days")
    public int getCountdownDays() {
        return countdownExtractor.extractCountdownDays();
    }

    @GetMapping("/runnerInfo/{email}")
    public ResponseEntity<RunnerInfoDTO> getRunnerInfo(@PathVariable String email) {
        RunnerInfoDTO runnerInfoDTO = runnerInfoService.getRunnerInfo(email);
        return ResponseEntity.ok(runnerInfoDTO);
    }
}
