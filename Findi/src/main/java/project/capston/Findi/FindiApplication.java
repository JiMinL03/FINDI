package project.capston.Findi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;

@SpringBootApplication
public class FindiApplication {

	public static void main(String[] args) {
		SpringApplication.run(FindiApplication.class, args);
	}

	@PostConstruct
	public void startRasaServers() throws IOException {
		String rasaPath = "chatbot";    // Rasa 프로젝트 경로
		String venvPath = ".venv";      // 가상환경 폴더
		boolean isWindows = System.getProperty("os.name").toLowerCase(Locale.ROOT).contains("win");

		// 가상환경 안의 python 실행 파일 경로
		String pythonPath = isWindows
				? venvPath + "\\Scripts\\python.exe"
				: venvPath + "/bin/python";

		// Rasa 메인 서버 명령어
		ProcessBuilder rasaPb = new ProcessBuilder(
				pythonPath, "-m", "rasa", "run", "--enable-api", "--cors", "*", "--debug"
		);
		rasaPb.directory(new File(rasaPath));
		rasaPb.inheritIO();
		rasaPb.start();

		// Rasa 액션 서버 명령어
		ProcessBuilder actionPb = new ProcessBuilder(
				pythonPath, "-m", "rasa", "run", "actions"
		);
		actionPb.directory(new File(rasaPath));
		actionPb.inheritIO();
		actionPb.start();

		System.out.println("✅ Rasa and action servers started via Java using venv python.");
	}
}
