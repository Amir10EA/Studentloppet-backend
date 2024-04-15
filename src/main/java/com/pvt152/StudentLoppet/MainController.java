package com.pvt152.StudentLoppet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@Controller
@RequestMapping(path="/studentloppet")
@CrossOrigin
public class MainController {
    
    @Autowired
    private UserRepository userRepository;

    @GetMapping(value="/hello")
    public @ResponseBody String hello() {
        return "helloworld";
    }
    
    @GetMapping(path = "/add/{firstName}/{lastName}/{password}/{email}/{university}")
    public @ResponseBody String addNewUser (@PathVariable String firstName, @PathVariable String lastName, 
        @PathVariable String password, @PathVariable String email, @PathVariable String university){

        User u = new User();
        
        u.setFirstName(firstName);
        u.setLastName(lastName);
        
        userRepository.save(u);

        return "Saved";

    }

    @GetMapping(path = "/login/{email}/{password}")
    public @ResponseBody boolean login (@PathVariable String email, @PathVariable String password){
        try {

            User u = userRepository.findById(email).orElseThrow(IllegalArgumentException::new);

            if (u.getPassword().equals(password)){
                return true;
            }
            else{
                return false;
            }

            
        } catch (IllegalArgumentException userNotFoundException) {
            return false;
        }

    }

}
