package project.capston.Findi.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import project.capston.Findi.Entity.Roommate;
import project.capston.Findi.Service.RoommateService;
import java.util.Map;

@RestController
@RequestMapping("/roommate")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class RoommateController {

    private final RoommateService roommateService;

    // ✅ DB에 저장
    @PostMapping
    public ResponseEntity<Roommate> submitRoommate(@RequestBody Roommate form) {
        System.out.println("👉 POST /roommate 호출됨");
        Roommate roommate = new Roommate();
        roommate.setGender(form.getGender());
        roommate.setName(form.getName());
        roommate.setBirth(form.getBirth());
        roommate.setStudent_id(form.getStudent_id());
        roommate.setMbti(form.getMbti());
        roommate.setMajor(form.getMajor());
        roommate.setIs_Smoking(form.getIs_Smoking());
        roommate.setLife_pattern(form.getLife_pattern());

        Roommate saved = roommateService.saveRoommate(roommate);
        return ResponseEntity.ok(saved);
    }

    // ✅ Flask 매칭 요청
    @PostMapping("/match")
    public ResponseEntity<?> matchRoommate(@RequestBody Map<String, Object> payload) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(
                    "http://192.168.0.18:5000/handleMatch", request, String.class);

            return ResponseEntity.ok(response.getBody());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Flask 통신 오류: " + e.getMessage());
        }
    }
}