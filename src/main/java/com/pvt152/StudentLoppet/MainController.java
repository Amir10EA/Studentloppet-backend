package com.pvt152.StudentLoppet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@Controller
@RequestMapping(path="/studentloppet")
public class MainController {
    
    @Autowired
    private UserRepository userRepository;

    @GetMapping(value="/hello")
    public @ResponseBody String hello() {
        return "helloworld";
    }
    
    @GetMapping(path = "/add/{firstName}/{lastName}/{university}")
    public @ResponseBody String addNewUser (@PathVariable String firstName, @PathVariable String lastName, @PathVariable University university){

        User u = new User(firstName, lastName, university);
        userRepository.save(u);
        return "Saved";

    }

}
