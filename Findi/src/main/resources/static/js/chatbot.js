const inputField = document.getElementById("userInput");
const sendBtn = document.getElementById("sendBtn");
const chatbox = document.getElementById("chatbox");

function addMessage(text, sender) {
  const messageDiv = document.createElement("div");
  messageDiv.className = `message ${sender}`;

  // 링크 자동 변환 처리
  const linkified = text.replace(
    /(https?:\/\/[^\s]+)/g,
    '<a href="$1" target="_blank" rel="noopener noreferrer" style="color:#0d6efd; text-decoration: underline;">$1</a>'
  );

  messageDiv.innerHTML = linkified;
  chatbox.appendChild(messageDiv);
  chatbox.scrollTop = chatbox.scrollHeight;

  // ⭐ 챗봇 메시지일 때 quickReplies 위치 이동
  if (sender === "bot") {
    moveQuickReplies();
  }
}
document.addEventListener("DOMContentLoaded", () => {
  addMessage("궁금한 게 있으면 언제든 물어보세요! 😊", "bot");
  addQuickReplies(); // ✅ 버튼 목록 생성 함수 호출

});
async function sendMessage() {
  const input = inputField.value.trim();
  if (!input) return;

  addMessage(input, "user");
  inputField.value = "";
  inputField.focus();

  sendBtn.disabled = true;
  sendBtn.classList.add("loading");

  try {
    // 타임아웃 처리용 Promise.race 사용 (5초)
    const response = await Promise.race([
      fetch("http://localhost:5005/webhooks/rest/webhook", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ sender: "user", message: input }),
      }),
      new Promise((_, reject) => setTimeout(() => reject(new Error("Timeout")), 5000))
    ]);

    const data = await response.json();

    if (!data.length) {
      addMessage("🤖 챗봇이 응답하지 않았습니다.", "bot");
    } else {
      for (let i = 0; i < data.length; i++) {
        const msg = data[i];
        if (msg.text) addMessage(msg.text, "bot");

        // ✅ 마지막 메시지 출력 후 버튼 다시 생성
        if (i === data.length - 1) {
          addQuickReplies();
        }
      }
    }
  } catch (err) {
    addMessage("⚠️ 서버 연결 오류가 발생했습니다. 잠시 후 다시 시도해주세요.", "bot");
    console.error(err);
  } finally {
    sendBtn.disabled = false;
    sendBtn.classList.remove("loading");
  }
}

function checkEnter(event) {
  if (event.key === "Enter") {
    sendMessage();
  }
}

const quickReplies = [
  { label: "학교 정보", message: "학교 정보 궁금해" },
  { label: "단과대학 정보", message: "단과대학 정보 궁금해" },
  { label: "학과 정보", message: "전공 관련 정보 궁금해" },
  { label: "교수 정보", message: "교수 정보 궁금해" },
  { label: "학사일정", message: "학사일정 궁금해" },
  { label: "건물 정보", message: "건물 정보 궁금해" },
  { label: "사용자 링크 정보", message: "사용자 링크 정보 궁금해" },
  { label: "공지사항", message: "공지사항 궁금해" }
];

function moveQuickReplies() {
  const container = document.getElementById("quickReplies");
  const chatbox = document.getElementById("chatbox");
  const botMessages = chatbox.querySelectorAll(".message.bot");
  if (botMessages.length > 0) {
    botMessages[botMessages.length - 1].after(container);
  } else {
    chatbox.appendChild(container);
  }
}

function addQuickReplies() {
  const container = document.getElementById("quickReplies");
  container.innerHTML = ""; // 초기화

  quickReplies.forEach(({ label, message }) => {
    const btn = document.createElement("button");
    btn.className = "quick-reply-btn";
    btn.textContent = label;
    btn.onclick = () => {
      inputField.value = label;
      sendMessageWithCustomMessage(message);

      // ✅ 버튼들 숨기기
      container.innerHTML = "";
    };
    container.appendChild(btn);
  });
  // ⭐ 위치 이동 함수 호출
  moveQuickReplies();

}

async function sendMessageWithCustomMessage(customMsg) {
  if (!customMsg) return;

  addMessage(inputField.value, "user"); // inputField.value는 버튼 라벨

  inputField.value = "";
  inputField.focus();
  sendBtn.disabled = true;
  sendBtn.classList.add("loading");

  try {
    const response = await fetch("http://localhost:5005/webhooks/rest/webhook", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ sender: "user", message: customMsg }),
    });

    const data = await response.json();
    if (data.length === 0) {
      addMessage("챗봇이 응답하지 않았습니다.", "bot");
    } else {
      data.forEach((msg) => {
        if (msg.text) addMessage(msg.text, "bot");
      });
      addQuickReplies();
    }
  } catch (err) {
    addMessage("⚠️ Error connecting to Rasa server.", "bot");
  }

  sendBtn.disabled = false;
  sendBtn.classList.remove("loading");
}
