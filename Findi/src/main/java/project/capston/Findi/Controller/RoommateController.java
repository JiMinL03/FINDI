package project.capston.Findi.Controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import project.capston.Findi.Entity.Member;
import project.capston.Findi.Entity.Roommate;
import project.capston.Findi.Service.RoommateService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/roommate")
@RequiredArgsConstructor
public class RoommateController {

    private final RoommateService roommateService;

    @GetMapping("/all")
    public ResponseEntity<List<Roommate>> getAllRoommates() {
        List<Roommate> all = roommateService.findAll();
        return ResponseEntity.ok(all);
    }

    @PostMapping
    public ResponseEntity<String> save(@RequestBody Roommate roommate) {
        System.out.println(" POST 요청 도착: " + roommate);
        roommateService.save(roommate);
        return ResponseEntity.ok(" 저장 성공!");
    }

    @PostMapping("/match")
    public ResponseEntity<?> matchRoommates(@RequestBody Roommate roommate) {
        String pythonUrl = "http://192.168.0.18:5000/handleMatch";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Roommate> entity = new HttpEntity<>(roommate, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(pythonUrl, entity, String.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                System.err.println("❌ Flask 응답 오류: " + response.getBody());
                return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
            }

            return ResponseEntity.ok(response.getBody());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("❌ Python 서버와 통신 실패: " + e.getMessage());
        }
    }
    @GetMapping("/by-name/{name}")
    public ResponseEntity<Roommate> getByName(@PathVariable String name) {
        Roommate roommate = roommateService.findByName(name); // ✅ 수정!
        if (roommate == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(roommate);
    }

    @PostMapping("/test")
    public ResponseEntity<String> testPost() {
        return ResponseEntity.ok(" POST 잘 들어옴");
    }
}
