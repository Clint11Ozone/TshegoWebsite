<script>
  var currentSlide = 0;
  var slides = document.querySelectorAll('.banner-slide');
  document.addEventListener('DOMContentLoaded', function() {
  // Your slideshow JavaScript code here
});

  function showSlide(n) 
    slides[currentSlide].style.display = 'none';
    currentSlide = (n + slides.length) % slides.length;
    slides[currentSlide].style.display = 'block';
  

  function nextSlide() 
    showSlide(currentSlide + 1);
  

  // Start the slideshow
  setInterval(nextSlide, 3000); // Change slide every 3 seconds (adjust as needed)
</script>