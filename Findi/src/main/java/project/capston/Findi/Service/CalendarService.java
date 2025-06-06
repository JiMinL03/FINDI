package project.capston.Findi.Service;

import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import project.capston.Findi.Entity.Calendar;
import project.capston.Findi.Repository.CalendarRepository;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class CalendarService {
    private final CalendarRepository calendarRepository;

    private final int baseYear = 2025;

    public List<Calendar> getSortedCalendarList() {
        return calendarRepository.findAll(Sort.by("startDate"));
    }

    public void fetchAndSaveCalendar() throws IOException {
        calendarRepository.deleteAll();
        Document doc = Jsoup.connect("https://www.deu.ac.kr/www/scheduleList.do").get();
        Elements rows = doc.select("table.tbl-basic-dleft tbody tr");

        for (Element row : rows) {
            try {
                String rawDate = row.selectFirst("th").text().trim();
                String title = row.selectFirst("td").text().trim();

                LocalDate[] dates = parseDateRange(rawDate);
                if (dates == null) continue;

                LocalDate startDate = dates[0];
                LocalDate endDate = dates[1];

                if (endDate.isBefore(startDate)) { // 날짜 뒤집힘 교정
                    LocalDate temp = startDate;
                    startDate = endDate;
                    endDate = temp;
                }

                if (isSeasonClass(title)) {
                    if (shouldSaveSeasonClass(startDate, endDate)) {
                        saveCalendar(startDate, endDate, title);
                    } else {
                        log.info("10일 미만 계절수업 일정은 저장하지 않음: {} ({} ~ {})", title, startDate, endDate);
                    }
                } else {
                    saveCalendar(startDate, endDate, title);
                }

            } catch (Exception e) {
                log.error("날짜 파싱 오류: {} / 원본 날짜: {}", e.getMessage(), row.text());
            }
        }
    }

    private LocalDate[] parseDateRange(String rawDate) {
        if (rawDate.contains("~")) {
            String[] parts = rawDate.split("~");
            if (parts.length != 2) return null;

            LocalDate startDate = parseSingleDate(parts[0].trim(), baseYear);
            if (startDate == null) return null;

            // 종료일 파싱
            String endRaw = parts[1].trim();
            LocalDate endDate;
            if (endRaw.contains("월")) {
                // 월 정보 포함시 연도 넘어가는 경우 처리
                endDate = parseSingleDate(endRaw, startDate.getYear());
                if (endDate == null) return null;

                // 종료월이 시작월보다 작으면 연도 + 1
                if (endDate.getMonthValue() < startDate.getMonthValue()) {
                    endDate = endDate.plusYears(1);
                }
            } else {
                // 종료월 정보 없으면 시작월과 동일 월로 간주
                int endDay = Integer.parseInt(endRaw.replaceAll("[^0-9]", ""));
                endDate = LocalDate.of(startDate.getYear(), startDate.getMonthValue(), endDay);
            }
            return new LocalDate[]{startDate, endDate};
        } else {
            // 단일 날짜
            LocalDate date = parseSingleDate(rawDate, baseYear);
            if (date == null) return null;
            return new LocalDate[]{date, date};
        }
    }

    private LocalDate parseSingleDate(String dateStr, int year) {
        if (!dateStr.contains("월")) return null;

        String[] parts = dateStr.split("월");
        if (parts.length < 2) return null;

        try {
            int month = Integer.parseInt(parts[0].replaceAll("[^0-9]", ""));
            int day = Integer.parseInt(parts[1].replaceAll("[^0-9]", ""));

            return LocalDate.of(year, month, day);
        } catch (NumberFormatException | DateTimeException e) {
            log.error("날짜 포맷 오류: {}", e.getMessage());
            return null;
        }
    }

    private boolean isSeasonClass(String title) {
        return "동계계절수업".equals(title) || "하계계절수업".equals(title);
    }

    private boolean shouldSaveSeasonClass(LocalDate start, LocalDate end) {
        long diff = ChronoUnit.DAYS.between(start, end);
        return diff > 10;
    }

    private void saveCalendar(LocalDate startDate, LocalDate endDate, String title) {
        Calendar calendar = new Calendar();
        calendar.setStartDate(startDate);
        calendar.setEndDate(endDate);
        calendar.setTitle(title);
        calendarRepository.save(calendar);
    }
}
