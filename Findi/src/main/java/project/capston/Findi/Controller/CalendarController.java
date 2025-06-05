package project.capston.Findi.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import project.capston.Findi.Entity.Calendar;
import project.capston.Findi.Service.CalendarService;

import java.io.IOException;
import java.util.List;


@Controller
@RequiredArgsConstructor
public class CalendarController {
    private final CalendarService calendarService;
    @GetMapping("/calendar")
    public String calendar(Model model) {
        List<Calendar> events = calendarService.getSortedCalendarList(); // 서비스에서 학사일정 가져오기
        model.addAttribute("events", events);
        return "calendar";
    }



    @GetMapping("/calendar/fetch")
    public ResponseEntity<String> fetchColleges() {
        try {
            calendarService.fetchAndSaveCalendar();
            return ResponseEntity.ok("학사일정 데이터를 성공적으로 저장했습니다.");
        } catch (IOException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("데이터 수집 실패: " + e.getMessage());
        }
    }

    @GetMapping("/calendar/test")
    public String test() {
        return "test";
    }
}
