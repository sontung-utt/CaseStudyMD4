const box = document.querySelector('.list-item');

// Thêm một trình lắng nghe sự kiện cho phần tử 'box'
box.addEventListener('click', function() {
    // Kiểm tra nếu phần tử đã có lớp 'clicked' chưa
    if (box.classList.contains('clicked')) {
        // Nếu có, gỡ bỏ lớp 'clicked'
        box.classList.remove('clicked');
    } else {
        // Nếu chưa, thêm lớp 'clicked'
        box.classList.add('clicked');
    }
});