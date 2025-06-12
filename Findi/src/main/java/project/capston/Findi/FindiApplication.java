package project.capston.Findi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;

@SpringBootApplication
public class FindiApplication {

	public static void main(String[] args) {
		SpringApplication.run(FindiApplication.class, args);
	}

	@PostConstruct
	public void startRasaServer() {
		String rasaPath = System.getenv("RASA_PATH");
		if (rasaPath == null || rasaPath.isBlank()) {
			rasaPath = ".";  // 기본값: 현재 디렉토리 (루트)
		}

		String os = System.getProperty("os.name").toLowerCase();
		ProcessBuilder pb;

		try {
			if (os.contains("win")) {
				// Windows: .bat 파일 실행
				pb = new ProcessBuilder("cmd.exe", "/c", "start", "start_chatbot.bat");
			} else {
				// Unix/Linux/macOS: .sh 스크립트 실행
				pb = new ProcessBuilder("sh", "start_chatbot.sh");
			}

			pb.directory(new File(rasaPath));
			pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
			pb.redirectError(ProcessBuilder.Redirect.INHERIT);
			pb.start();

			System.out.println("✅ Rasa server starting at directory: " + rasaPath);
		} catch (IOException e) {
			System.err.println("❌ Failed to start Rasa server: " + e.getMessage());
			// 필요 시 로그 파일 저장 또는 알림 기능 추가 가능
		}
	}
}
