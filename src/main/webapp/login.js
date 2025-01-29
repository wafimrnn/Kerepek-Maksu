// Function to toggle between login and signup forms
window.toggleForm = function (formType) {
    console.log(`toggleForm called with formType: ${formType}`);
    const loginForm = document.getElementById('login-form');
    const signupForm = document.getElementById('signup-form');

    if (formType === 'signup') {
        loginForm.style.display = 'none';
        signupForm.style.display = 'block';
    } else {
        loginForm.style.display = 'block';
        signupForm.style.display = 'none';
    }
};

// Handle login form submission
document.getElementById("login-form-data").addEventListener("submit", function (e) {
    e.preventDefault();
    this.submit(); // Directly submit the form
});

// Handle signup form submission
document.getElementById("signup-form-data").addEventListener("submit", function (e) {
    e.preventDefault();
    this.submit(); // Directly submit the form
});

// Display feedback message for login or signup based on the page
window.onload = function () {
  const urlParams = new URLSearchParams(window.location.search);
  const message = urlParams.get('message');

  // Check if the login message element exists
  const loginMessageElement = document.getElementById('login-message');
  if (loginMessageElement && message) {
    loginMessageElement.textContent = message;
  }

  // Check if the signup message element exists
  const signupMessageElement = document.getElementById('signup-message');
  if (signupMessageElement && message) {
    signupMessageElement.textContent = message;
  }
};
