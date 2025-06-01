package project.capston.Findi;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class PythonServerRunner {

    @PostConstruct
    public void startPythonServer() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("python", "./Findi/Roommate.py");
            processBuilder.inheritIO(); // Flask 로그를 Spring 콘솔에 출력
            processBuilder.start();
            System.out.println("✅ Flask 서버 실행 시작됨");
        } catch (IOException e) {
            System.err.println("❌ Python 서버 실행 실패: " + e.getMessage());
        }
    }
}
