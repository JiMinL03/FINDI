package project.capston.Findi;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

@Component
public class PythonServerRunner {

    @PostConstruct
    public void startPythonServer() {
        boolean isWindows = System.getProperty("os.name").toLowerCase(Locale.ROOT).contains("win");

        // OS별 경로 설정
        String pythonPath = isWindows
                ? "C:\\FINDI\\.venv\\Scripts\\python.exe" // Windows 경로
                : "/home/ubuntu/app/.venv/bin/python";    // 리눅스 서버 경로 (실제 환경에 맞게 수정 필요)

        String scriptPath = isWindows
                ? "C:\\FINDI\\Findi\\Roommate.py"
                : "/home/ubuntu/app/Roommate.py";

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(pythonPath, scriptPath);
            processBuilder.directory(new File(new File(scriptPath).getParent())); // 실행 디렉토리 설정
            processBuilder.inheritIO(); // 콘솔 로그 출력
            processBuilder.start();
            System.out.println("✅ Flask 서버 실행 시작됨");
        } catch (IOException e) {
            System.err.println("❌ Python 서버 실행 실패: " + e.getMessage());
        }
    }
}
