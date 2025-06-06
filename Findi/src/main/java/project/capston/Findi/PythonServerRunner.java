package project.capston.Findi;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class PythonServerRunner {

    @PostConstruct
    public void startPythonServer() {
        try {
          String scriptPath = "Roommate.py";

            File script = new File(scriptPath);
            if (!script.exists()) {
                System.err.println("❌ Python 파일 없음: " + script.getAbsolutePath());
                return;
            }

            ProcessBuilder processBuilder = new ProcessBuilder("python", script.getAbsolutePath());
            processBuilder.inheritIO(); // 콘솔 로그 Spring에 출력
            processBuilder.start();

            System.out.println("✅ Flask 서버 실행 시작됨");
        } catch (IOException e) {
            System.err.println("❌ Python 서버 실행 실패: " + e.getMessage());
        }
    }
}
