document.addEventListener('DOMContentLoaded', function () {
  // Detect user role from the body attribute
  const userRole = document.body.getAttribute('data-role');
  
  // Get form elements
  const updateForm = document.getElementById('update-form');
  const phoneInput = document.getElementById('phone');
  const addressInput = document.getElementById('address');
  const feedbackMessage = document.getElementById('feedback-message');
  const createStaffButton = document.getElementById('create-staff-btn');

  // Show the create staff button only if the user is an owner
  if (userRole !== 'OWNER') {
    createStaffButton.style.display = 'none';
  }

  // Handle form submission
  if (updateForm) {
    updateForm.addEventListener('submit', async (e) => {
      e.preventDefault(); // Prevent default form submission

      const phone = phoneInput.value;
      const address = addressInput.value;

      // Check if fields are not empty (basic validation)
      if (!phone || !address) {
        feedbackMessage.textContent = "Please fill out all fields.";
        feedbackMessage.style.color = "red";
        return;
      }

      // Prepare data for submission
      const formData = new FormData();
      formData.append('phone', phone);
      formData.append('address', address);

      // Send data to the backend using Fetch API
      try {
        const response = await fetch('/update-account', {
          method: 'POST',
          body: formData
        });

        const result = await response.json();

        // Display feedback based on the response
        if (response.ok) {
          feedbackMessage.textContent = result.message || "Account updated successfully!";
          feedbackMessage.style.color = "green";
        } else {
          feedbackMessage.textContent = result.error || "An error occurred while updating your account.";
          feedbackMessage.style.color = "red";
        }
      } catch (error) {
        feedbackMessage.textContent = "There was an error with the request.";
        feedbackMessage.style.color = "red";
      }
    });
  }
});