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

	// Spring이 빈(Bean) 생성 후 자동으로 실행하는 메서드
	@PostConstruct
	public void startRasaServer() throws IOException {
		String rasaPath = System.getenv("RASA_PATH");
		if (rasaPath == null) {
			rasaPath = ".";  // 기본값: 프로젝트 루트 (배치파일 위치)
		}

		// 배치파일 실행 (Windows 환경)
		ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", "start", "start_chatbot.bat");
		pb.directory(new File(rasaPath));
		pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
		pb.redirectError(ProcessBuilder.Redirect.INHERIT);
		pb.start();

		System.out.println("Rasa server starting (via batch) at directory: " + rasaPath);
	}
}
