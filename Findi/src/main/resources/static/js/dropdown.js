
function toggleDropdown(button) {
    // 이전에 열려있던 모든 드롭다운 메뉴를 닫습니다
    const allDropdowns = document.querySelectorAll('.dropdown-menu');
    allDropdowns.forEach(dropdown => {
        if (dropdown !== button.nextElementSibling) {
            dropdown.classList.remove('show');
        }
    });

    // 클릭한 버튼의 드롭다운 메뉴를 토글합니다
    const dropdown = button.nextElementSibling;
    dropdown.classList.toggle('show');

    // 문서의 다른 부분을 클릭하면 드롭다운이 닫히도록 합니다
    document.addEventListener('click', function closeDropdown(e) {
        if (!button.contains(e.target) && !dropdown.contains(e.target)) {
            dropdown.classList.remove('show');
            document.removeEventListener('click', closeDropdown);
        }
    });
}
