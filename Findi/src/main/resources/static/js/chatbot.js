let inputField, sendBtn, chatbox;
let allNotices = [];
let allServices = [];
let currentPage = 1;
const pageSize = 5;
let currentContentType = ""; // "notice" 또는 "service"

document.addEventListener("DOMContentLoaded", () => {
  inputField = document.getElementById("userInput");
  sendBtn = document.getElementById("sendBtn");
  chatbox = document.getElementById("chatbox");

  addMessage("궁금한 게 있으면 언제든 물어보세요! 😊", "bot");
  addQuickReplies();
  addQuickReplies();

  inputField.addEventListener("keydown", (e) => e.key === "Enter" && sendMessage());
  sendBtn.addEventListener("click", sendMessage);
});

function addMessage(text, sender) {
  const messageDiv = document.createElement("div");
  messageDiv.className = `message ${sender}`;
  messageDiv.innerHTML = text.replace(
    /(https?:\/\/[^\s]+)/g,
    '<a href="$1" target="_blank" style="color:#0d6efd;">$1</a>'
  );
  chatbox.appendChild(messageDiv);
  chatbox.scrollTop = chatbox.scrollHeight;
  if (sender === "bot") moveQuickReplies();
}

function addQuickReplies() {
  const container = document.getElementById("quickReplies");
  container.innerHTML = "";
  quickReplies.forEach(({ label, message }) => {
    const btn = document.createElement("button");
    btn.className = "quick-reply-btn";
    btn.textContent = label;
    btn.onclick = () => {
      hideQuickReplies();
      addMessage(message, "user");
      sendMessageToRasa(message);
    };
    container.appendChild(btn);
  });
  moveQuickReplies();
}

function hideQuickReplies() {
  document.getElementById("quickReplies").innerHTML = "";
}

function moveQuickReplies() {
  const container = document.getElementById("quickReplies");
  const lastBotMessage = chatbox.querySelector(".message.bot:last-of-type");
  if (lastBotMessage) lastBotMessage.after(container);
  else chatbox.appendChild(container);
}

async function sendMessage() {
  const input = inputField.value.trim();
  if (!input) return;
  addMessage(input, "user");
  inputField.value = "";
  inputField.focus();
  hideQuickReplies();
  sendBtn.disabled = true;
  sendBtn.classList.add("loading");

  await sendMessageToRasa(input);

  sendBtn.disabled = false;
  sendBtn.classList.remove("loading");
}

async function sendMessageToRasa(message) {
  try {
    const response = await fetch("http://localhost:5005/webhooks/rest/webhook", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ sender: "user", message }),
    });
    const data = await response.json();

    for (let msg of data) {
      if (msg.text) {
        const cleanText = msg.text.replace("__END__", "").replace("\u2063", "").trim();
        addMessage(cleanText, "bot");
      }

      if (msg.custom?.notice) {
        allNotices = msg.custom.notice;
        currentPage = 1;
        renderNoticesPage(currentPage);
      }
      if (msg.custom?.services) {
        allServices = msg.custom.services;
        currentPage = 1;
        renderServicesPage(currentPage);
      }
    }
  } catch (err) {
    console.error(err);
    addMessage("⚠️ 서버 연결 오류가 발생했습니다. 잠시 후 다시 시도해주세요.", "bot");
    addQuickReplies();
  }
}

function renderNoticesPage(page) {
  const totalPages = Math.ceil(allNotices.length / pageSize);
  const start = (page - 1) * pageSize;
  const end = start + pageSize;
  const notices = allNotices.slice(start, end);

  const prevBox = document.querySelector(".notice-box");
  if (prevBox) prevBox.remove();

  const wrapper = document.createElement("div");
  wrapper.className = "message bot notice-box";

  wrapper.innerHTML = `
    <p>📢 공지사항 (${page}/${totalPages} 페이지)</p><ul>
      ${notices.map(n => `<li><a href="${n.link}" target="_blank">${n.title} (작성자: ${n.author})</a></li>`).join("")}</ul>
    <div class="pagination-buttons">
      ${page > 1 ? `<button class="prev-notice">이전</button>` : ""}
      ${page < totalPages ? `<button class="next-notice">다음</button>` : ""}
      <button class="clear-notice">그만보기</button>
    </div>
  `;

  chatbox.appendChild(wrapper);
  chatbox.scrollTop = chatbox.scrollHeight;

  wrapper.querySelector(".prev-notice")?.addEventListener("click", () => renderNoticesPage(page - 1));
  wrapper.querySelector(".next-notice")?.addEventListener("click", () => renderNoticesPage(page + 1));
  wrapper.querySelector(".clear-notice")?.addEventListener("click", clearNotices);
}

function renderServicesPage(page) {
  const totalPages = Math.ceil(allServices.length / pageSize);
  const start = (page - 1) * pageSize;
  const end = start + pageSize;
  const services = allServices.slice(start, end);

  const prevBox = document.querySelector(".service-box");
  if (prevBox) prevBox.remove();

  const wrapper = document.createElement("div");
  wrapper.className = "message bot service-box";

  console.log(services.map(s => `<li><a href="${s.link}" target="_blank">${s.name}</a></li>`).join(""));

  wrapper.innerHTML = `
    <p>🔗 사용자 링크 (${page}/${totalPages} 페이지)</p><ul>
      ${services.map(s => `<li><a href="${s.link}" target="_blank">${s.name}</a></li>`).join("")}</ul>
    <div class="pagination-buttons">
      ${page > 1 ? `<button class="prev-service">이전</button>` : ""}
      ${page < totalPages ? `<button class="next-service">다음</button>` : ""}
      <button class="clear-service">그만보기</button>
    </div>
  `;

  chatbox.appendChild(wrapper);
  chatbox.scrollTop = chatbox.scrollHeight;

  wrapper.querySelector(".prev-service")?.addEventListener("click", () => renderServicesPage(page - 1));
  wrapper.querySelector(".next-service")?.addEventListener("click", () => renderServicesPage(page + 1));
  wrapper.querySelector(".clear-service")?.addEventListener("click", clearServices);
}

function clearNotices() {
  currentPage = 1;
  allNotices = [];
  const prevBox = document.querySelector(".notice-box");
  if (prevBox) prevBox.remove();
  addQuickReplies();
}

function clearServices() {
  currentPage = 1;
  allServices = [];
  const prevBox = document.querySelector(".service-box");
  if (prevBox) prevBox.remove();
  addQuickReplies();
}

const quickReplies = [
  { label: "학교 정보", message: "학교 정보 알려줘" },
  { label: "단과대학 정보", message: "단과대학 정보 궁금해" },
  { label: "학과 정보", message: "학과 정보 궁금해" },
  { label: "교수 정보", message: "교수님 정보 궁금해" },
  { label: "학사일정", message: "학사일정 궁금해" },
  { label: "건물 정보", message: "건물 정보 궁금해" },
  { label: "사용자 링크 정보", message: "사용자 링크 정보 궁금해" },
  { label: "공지사항", message: "공지사항 궁금해" }
];

const buildingList = [
  "대학본관", "법정관", "상경관", "동의의료원", "국제관", "동의스포츠센터", "상영관(제2학생회관)",
  "수덕전(학생회관)", "제1인문관", "제2인문관", "효민체육관", "중앙도서관", "여대생커리어개발",
  "제2효민생활관", "제1효민생활관", "의료보건관", "생활과학관", "음악관", "창의관", "지천관",
  "산학협력관", "건윤관", "공학관", "정보공학관", "학생군사교육단", "행복기숙사(미래생활관)"
];