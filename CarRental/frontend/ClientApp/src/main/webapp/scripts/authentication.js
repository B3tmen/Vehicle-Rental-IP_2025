const togglePassword = document.getElementById('toggle-password');
const toggleConfirmPassword = document.getElementById('toggle-confirm-password');
const passwordField = document.getElementById('password');
const repeatPasswordField = document.getElementById('confirm-password');

function togglePasswordField() {
    // Toggle the type attribute
    passwordField.type = passwordField.type === 'password' ? 'text' : 'password';

    // Toggle the icon
    this.classList.toggle('fa-eye');
    this.classList.toggle('fa-eye-slash');
}

function toggleRepeatPasswordField() {
    // Toggle the type attribute
    repeatPasswordField.type = repeatPasswordField.type === 'password' ? 'text' : 'password';

    // Toggle the icon
    this.classList.toggle('fa-eye');
    this.classList.toggle('fa-eye-slash');
}

function uploadImage(event, elementId){
    const selectedImage = document.getElementById(elementId);
    const fileInput = event.target;

    const file = fileInput.files[0];
    if(fileInput.files && file){
        // Validate file type
        if (!file.type.match('image.*')) {
            alert('Please select an image file (JPEG, PNG, etc.)');
            event.target.value = ''; // clear the file input
            return;
        }

        // Validate file size (e.g., 2MB max)
        if (file.size > 2 * 1024 * 1024) {
            alert('Image must be less than 2MB');
            event.target.value = '';
            return;
        }

        const reader = new FileReader();
        reader.onload = function(e){
            selectedImage.src = e.target.result;
        }

        reader.readAsDataURL(file);
    }
}

togglePassword.addEventListener('click', togglePasswordField);
toggleConfirmPassword.addEventListener('click', toggleRepeatPasswordField);

function checkFormValidity(form) {
    // Trigger Bootstrap validation
    if (form && !form.checkValidity()) {
        form.classList.add("was-validated");
        return false; // Stop form submission if validation fails
    }

    return true;
}

function registerUser() {
    let registerForm = document.getElementById("registerForm");
    if(!checkFormValidity(registerForm)){
        return;
    }

    registerForm.submit();
}