const PROFILE_URL = "http://localhost:8080/ClientApp/profile";
const LOGIN_URL = "http://localhost:8080/ClientApp/login";

const deactivateButton = document.getElementById('confirmModalYesButton');
deactivateButton.addEventListener('click', onDeactivateAccount);

function loadModalBody(){
    document.getElementById('customModalTitle').innerHTML = 'Edit Profile';
    document.getElementById('customModalBody').innerHTML = `
        <form id="editProfileForm" method="POST" enctype="multipart/form-data" class="d-flex flex-column gap-3 needs-validation" novalidate>
            <div class="form-group has-validation">
                <label for="oldPassword">Old password: </label>
                <input type="password" id="oldPassword" name="oldPassword" minlength="8" class="form-control" required />
                <div class="invalid-feedback">
                    Your password must be at least 8 characters long.
                </div>
            </div>
            
            <div class="form-group has-validation">
                <label for="newPassword">New Password: </label>
                <input type="password" id="newPassword" name="newPassword" class="form-control" required />
                <div class="invalid-feedback">
                    Your password must be at least 8 characters long.
                </div>
            </div>
            
            <div class="form-group has-validation">
                <label for="confirmPassword">Repeat new Password: </label>
                <input type="password" id="confirmPassword" name="confirmPassword" class="form-control" required />
                <div class="invalid-feedback">
                    Your password must be at least 8 characters long.
                </div>
            </div>
            
            <label for="newAvatar">New Avatar:</label>
            <input type="file" id="newAvatar" name="newAvatar" accept="image/*" class="form-control" />
        </form>
    `;

    // Better way since onclick="onSubmitChangePass(event)" format is deprecated by passing the event object
    const submitButton = document.getElementById('modalSaveChangesButton');
    submitButton.addEventListener('click', onSubmitEditProfile);
}

function checkFormValidity(form) {
    // Trigger Bootstrap validation
    if (form && !form.checkValidity()) {
        form.classList.add("was-validated");
        return false; // Stop form submission if validation fails
    }

    return true;
}

async function onSubmitEditProfile(event){
    event.preventDefault();     // Prevent default behaviour with POST
    let editProfileForm = document.getElementById("editProfileForm");
    if(!checkFormValidity(editProfileForm)){
        return;
    }

    const formData = new FormData(editProfileForm);
    const response = await fetch(PROFILE_URL, {
        method: 'PUT',
        body: formData
    });

    const result = await response.json();
    alert(result.message);
}


async function onDeactivateAccount(event){
    event.preventDefault();

    const response = await fetch(PROFILE_URL, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        },
    });

    const result = await response.json();
    confirm(result.message)
    window.location.replace(LOGIN_URL);
}