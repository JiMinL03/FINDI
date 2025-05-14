package project.capston.Findi.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping(value = {"/", "/home"})
    public String index() {
        return "forward:/index.html"; // React 메인 진입점
    }
}