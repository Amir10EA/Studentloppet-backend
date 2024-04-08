package com.pvt152.StudentLoppet;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
@RequestMapping(path="/pet")
public class MainController {
    
    @GetMapping(value="/hello")
    public @ResponseBody String hello() {
        return "hello";
    }
    
}
