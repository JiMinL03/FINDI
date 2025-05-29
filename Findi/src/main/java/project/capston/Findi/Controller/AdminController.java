package project.capston.Findi.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import project.capston.Findi.Service.AdminService;

import java.util.Collections;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/admin")
    public String admin(Model model) {
        List<MemberView> memberViews = adminService.getAllMembersForAdmin();
        model.addAttribute("members", memberViews != null ? memberViews : Collections.emptyList());
        return "admin";
    }

    @PostMapping("/admin/accept")
    public String accept(Model model, @RequestParam String id) {
        adminService.accept(id);
        return "redirect:/admin";
    }

    @PostMapping("/admin/reject")
    public String reject(Model model, @RequestParam String id) {
        adminService.reject(id);
        return "redirect:/admin";
    }


    public static class MemberView {
        private String id;
        private String username;
        private String job;
        private String base64Img;

        public MemberView(String id, String username, String job, String base64Img) {
            this.id = id;
            this.username = username;
            this.job = job;
            this.base64Img = base64Img;
        }

        // getter 만 생성
        public String getId() { return id; }
        public String getUsername() { return username; }
        public String getJob() { return job; }
        public String getBase64Img() { return base64Img; }
    }
}
