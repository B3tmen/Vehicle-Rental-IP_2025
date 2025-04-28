const PAYMENT_URL = 'http://localhost:8080/ClientApp/payment';
const DRIVE_URL = 'http://localhost:8080/ClientApp/drive';

// ----------------------------- MODAL --------------------------
// document.addEventListener("DOMContentLoaded", function () {
//     var toastContainer = document.getElementById("toastContainer");
//
//     document.getElementById("showToastBtn").addEventListener("click", function () {
//         createToast("This is a new toast notification!");
//     });
//
//     function createToast(message) {
//         // Create toast div
//         var toastDiv = document.createElement("div");
//         toastDiv.className = "toast align-items-center border-0";
//         toastDiv.setAttribute("role", "alert");
//         toastDiv.setAttribute("aria-live", "assertive");
//         toastDiv.setAttribute("aria-atomic", "true");
//
//         // Toast content
//         toastDiv.innerHTML = `
//                     <div class="toast-header">
//                             <img src="..." class="rounded me-2" alt="...">
//                             <strong class="me-auto">Title</strong>
//                             <small>11 mins ago</small>
//                             <button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Close"></button>
//                         </div>
//                     <div class="toast-body">
//                         Hello, world! This is a toast message.
//                     </div>
//             `;
//
//         // Append to toast container
//         toastContainer.appendChild(toastDiv);
//
//         // Initialize and show toast
//         var toast = new bootstrap.Toast(toastDiv, { delay: 3000 });
//         toast.show();
//
//         // Remove toast after hiding
//         toastDiv.addEventListener("hidden.bs.toast", function () {
//             toastDiv.remove();
//         });
//     }
// });

// ----------------------------- MODAL --------------------------
function checkNumberInput(e) {
    if (e.key < '0' || e.key > '9') {
        e.preventDefault();
    }
}
document.getElementById("cvv").addEventListener("keypress", checkNumberInput);
document.getElementById("cardNumber").addEventListener("keypress", checkNumberInput);

function checkFormValidity(form) {
    // Trigger Bootstrap validation
    if (form && !form.checkValidity()) {
        form.classList.add("was-validated");
        return false; // Stop form submission if validation fails
    }

    return true;
}

async function completePayment() {
    let localForm = document.getElementById("localForm");
    let foreignerForm = document.getElementById("foreignerForm");

    const localFormData = localForm ? new FormData(localForm) : null;
    const foreignerFormData = foreignerForm ? new FormData(foreignerForm) : null;
    const paymentFormData = new FormData(document.getElementById("paymentForm"));

    const foreignerPassport = foreignerFormData ? {
        passportNumber: foreignerFormData.get('passportNumber'),
        country: foreignerFormData.get('country'),
        validFrom: foreignerFormData.get('validFrom'),
        validTo: foreignerFormData.get('validTo'),
        driversLicence: foreignerFormData.get('driversLicenceForeigner')
    } : null;
    const startLocation = {
        latitude: paymentFormData.get('startLatitude'),
        longitude: paymentFormData.get('startLongitude'),
    }
    const endLocation = {
        latitude: paymentFormData.get('endLatitude'),
        longitude: paymentFormData.get('endLongitude'),
    }
    const paymentData = {
        rentDuration: paymentFormData.get('rentDuration'),
        cardType: paymentFormData.get('cardType'),
        cardNumber: paymentFormData.get('cardNumber'),
        expiryMonth: paymentFormData.get('expiryMonth'),
        expiryYear: paymentFormData.get('expiryYear'),
        cvv: paymentFormData.get('cvv'),
        holderFirstName: paymentFormData.get('holderFirstName'),
        holderLastName: paymentFormData.get('holderLastName')
    };
    const data = {
        foreignerPassport,
        startLocation,
        endLocation,
        paymentData
    }

    //console.log(JSON.stringify(data, null, 2)); // Pretty-print JSON

    const response = await fetch(PAYMENT_URL, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    });


    const result = await response.json();
    alert(result.message);

    if(response.status === 200){
        window.location.href=DRIVE_URL;
    }
}

function payRental() {
    let paymentForm = document.getElementById("paymentForm");
    let foreignerForm = document.getElementById("foreignerForm");
    let localForm = document.getElementById("localForm");
    const forms = [paymentForm, foreignerForm, localForm];
    let invalidCounter = 0;

    for(let form of forms) {
        if(!checkFormValidity(form)) invalidCounter++;
    }
    if(invalidCounter > 0) return;

    let startLat = document.getElementById("startLat").value;
    let startLng = document.getElementById("startLng").value;
    let endLat = document.getElementById("endLat").value;
    let endLng = document.getElementById("endLng").value;

    if (!startLat || !startLng || !endLat || !endLng) {
        alert("Please select a valid location!");
        return; // Stop form submission
    }

    completePayment();
}

document.addEventListener('DOMContentLoaded', function () {
    const popoverTriggerList = document.querySelectorAll('[data-bs-toggle="popover"]');
    const popoverList = [...popoverTriggerList].map(popoverTriggerEl => new bootstrap.Popover(popoverTriggerEl));
});
