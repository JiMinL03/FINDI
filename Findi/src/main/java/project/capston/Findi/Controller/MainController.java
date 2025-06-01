package project.capston.Findi.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    @GetMapping(value = {"/", "/home"})
    @RequestMapping({"/", "/roommate", "/roommate/**", "/roommate/match"
    })
    public String index() {
        return "forward:/index.html"; // React 메인 진입점
    }
}