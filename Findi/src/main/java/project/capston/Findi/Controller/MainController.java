package project.capston.Findi.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    // React의 모든 경로를 index.html로 포워딩
    @RequestMapping({
            "/", "/home","/register/**", "/roommate", "/roommate/**",
            "/chatbot", "/question","/member/**"
    })
    public String forwardReactRoutes() {
        return "forward:/index.html";
    }
}