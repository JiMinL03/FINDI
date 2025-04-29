package project.capston.Findi.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RoommateController {
    @GetMapping("/roommate")
    public String roommate() {
        return "roommate";
    }
}
