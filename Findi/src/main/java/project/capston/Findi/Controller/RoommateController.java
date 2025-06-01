package project.capston.Findi.Controller;

import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;
import project.capston.Findi.dto.RoommateRequestDto;

@Controller
public class RoommateController {
    @GetMapping("/roommate/match")
    public String roommate() {
        return "roommate";
    }

    @PostMapping("/roommate/match")
    public ResponseEntity<?> matchRoommates(@RequestBody RoommateRequestDto requestDto) {
        String pythonUrl = "http://192.168.0.18:5000/handleMatch";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<RoommateRequestDto> entity = new HttpEntity<>(requestDto, headers);
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(pythonUrl, entity, String.class);

            // Flask 응답 코드가 200이 아닌 경우 직접 전달
            if (!response.getStatusCode().is2xxSuccessful()) {
                System.err.println("❌ Flask 응답 오류: " + response.getBody());
                return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
            }

            return ResponseEntity.ok(response.getBody());

        } catch (Exception e) {
            e.printStackTrace(); // 콘솔 로그
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("❌ Python 서버와 통신 실패: " + e.getMessage());
        }
    }

}
