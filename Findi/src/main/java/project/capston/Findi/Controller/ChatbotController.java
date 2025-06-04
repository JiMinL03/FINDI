package project.capston.Findi.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ChatbotController {
    @GetMapping("/chatbot")
    public String chatbot() {
        return "chatbot";
    }
}
