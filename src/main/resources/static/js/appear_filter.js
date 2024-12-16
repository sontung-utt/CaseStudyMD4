window.onscroll = function() {
    var filterSection = document.getElementById("filterSection");
    var scrollPosition = window.scrollY;

    if (scrollPosition > 480) {
        filterSection.style.display = "block";
    } else {
        filterSection.style.display = "none";
    }
};