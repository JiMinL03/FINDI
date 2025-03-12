document.getElementById('file-input').addEventListener('change', function(e) {
    const fileList = document.querySelector('.file-list');
    fileList.innerHTML = ''; // 기존 목록 초기화
    
    if (this.files.length > 0) {
        const fileNames = Array.from(this.files)
            .map(file => file.name)
            .join(', ');
        fileList.textContent = fileNames;
    }
});