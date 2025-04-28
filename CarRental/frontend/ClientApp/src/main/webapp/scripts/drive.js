const DRIVE_SERVLET_URL = "http://localhost:8080/ClientApp/drive";
const HOME_PAGE_URL = "http://localhost:8080/ClientApp/home";
const START_TIME_KEY = 'startTime';
const TIMER_RUNNING_KEY = 'timerRunning';
const TIME_LEFT_KEY = 'timeLeft';
const DEFAULT_TIME_LEFT = initialDriveHours * 3600; // Convert hours to seconds
const WARNING_TIME_LIMIT = 300; // 5 minutes in seconds
const timerDisplay = document.getElementById('timer');

// Calculate initial time left
let timeLeft = calculateInitialTimeLeft();
let timer;
let timerRunning = isTimerRunning();

function startTimerOnLoad() {
    initTimer();
    startTimer();
}

// Initialize when page loads
window.addEventListener('load', startTimerOnLoad);

function calculateInitialTimeLeft() {
    // If we have a running timer in localStorage, use that
    if (localStorage.getItem(TIME_LEFT_KEY) && localStorage.getItem(TIME_LEFT_KEY) !== '0') {
        return parseInt(localStorage.getItem(TIME_LEFT_KEY));
    }

    // Otherwise, calculate based on driveBean duration
    return initialDriveHours * 3600;
}

function isTimerRunning() {
    // Check if we have a running timer in localStorage
    if (localStorage.getItem(TIMER_RUNNING_KEY) === 'true') {
        // Additional check to see if time hasn't expired
        const storedTimeLeft = parseInt(localStorage.getItem(TIME_LEFT_KEY) || DEFAULT_TIME_LEFT);
        return storedTimeLeft > 0;
    }
    return false;
}

function updateDisplay(seconds) {
    const hours = Math.floor(seconds / 3600);
    const minutes = Math.floor((seconds % 3600) / 60);
    const secs = seconds % 60;
    timerDisplay.textContent = `Duration ${String(hours).padStart(2, '0')}:${String(minutes).padStart(2, '0')}:${String(secs).padStart(2, '0')}`;

    // Visual warning when time is low
    if (seconds < WARNING_TIME_LIMIT) {
        timerDisplay.style.color = 'red';
        timerDisplay.style.fontWeight = 'bold';
    } else {
        timerDisplay.style.color = '';
        timerDisplay.style.fontWeight = '';
    }
}

async function updateVehicleStatus(){
    return await fetch(DRIVE_SERVLET_URL, {
        method: 'PUT',
    });
}

function startTimer() {
    if (timer || timeLeft <= 0) return; // Prevent multiple intervals or starting a finished timer

    // Record the start time if not already running
    if (!timerRunning) {
        localStorage.setItem(START_TIME_KEY, new Date().getTime());
    }

    timer = setInterval(() => {
        if (timeLeft <= 0) {
            clearInterval(timer);
            timer = null;
            localStorage.removeItem(TIMER_RUNNING_KEY);
            localStorage.removeItem(TIME_LEFT_KEY);
            localStorage.removeItem(START_TIME_KEY);
            alert('Time is up. Thank you for using our services!');

            // Disable the Finish button and update vehicle rental status
            document.getElementById("finishButton").disabled= true;
            updateVehicleStatus();
        } else {
            timeLeft--;
            localStorage.setItem(TIME_LEFT_KEY, timeLeft);
            updateDisplay(timeLeft);
        }
    }, 1000);

    timerRunning = true;
    localStorage.setItem(TIMER_RUNNING_KEY, 'true');
}

function stopTimer() {
    if (timer) {
        clearInterval(timer);
        timer = null;
    }
    timerRunning = false;
    if(driveActive) {
        localStorage.setItem(TIMER_RUNNING_KEY, 'false');
    }

    if(driveActive) {
        const button = document.getElementById("finishButton");
        button.disabled = true;
        const driveFinishedHeading = document.getElementById("driveFinishedHeading");
        driveFinishedHeading.attributes.removeNamedItem("hidden");
        removeKeysFromSessionStorage();
        deleteDriveBean().then(data => {
            alert(data.message);
        });
    }
}

function removeKeysFromSessionStorage() {
    localStorage.removeItem(START_TIME_KEY);
    localStorage.removeItem(TIMER_RUNNING_KEY);
    localStorage.removeItem(TIME_LEFT_KEY);
}

async function deleteDriveBean() {
    const response= await fetch(DRIVE_SERVLET_URL, {
        method: "DELETE",
        headers: {
            "Content-Type": "application/json"
        }
    });

    return response.json();
}

function resetTimer() {
    stopTimer();
    timeLeft = DEFAULT_TIME_LEFT;
    if(driveActive) {
        localStorage.setItem(TIME_LEFT_KEY, timeLeft);
    }
    updateDisplay(timeLeft);
    localStorage.removeItem(START_TIME_KEY);
}

// Calculate time passed when page is reloaded
function calculateTimePassed() {
    if (!timerRunning) return 0;

    const startTime = localStorage.getItem(START_TIME_KEY);
    if (!startTime) return 0;

    const now = new Date().getTime();
    const elapsedSeconds = Math.floor((now - parseInt(startTime)) / 1000);
    return elapsedSeconds;
}

// Initialize the timer
function initTimer() {
    if (driveActive) {
        // If timer was running, subtract the time passed while the page was closed
        if (timerRunning) {
            const timePassed = calculateTimePassed();
            timeLeft = Math.max(0, timeLeft - timePassed);
            localStorage.setItem(TIME_LEFT_KEY, timeLeft);

            if (timeLeft <= 0) {
                timerRunning = false;
                localStorage.setItem(TIMER_RUNNING_KEY, 'false');
            }
        }

        updateDisplay(timeLeft);

        if (timerRunning) {
            startTimer();
        }
    } else {
        resetTimer();
    }
}

// Force-save timeLeft when leaving the page
window.addEventListener('beforeunload', () => {
    if (timerRunning) {
        localStorage.setItem(TIME_LEFT_KEY, timeLeft);
    }
});

document.addEventListener('DOMContentLoaded', function() {
    const finishButton = document.getElementById('finishButton');

    finishButton.addEventListener('click', function() {
        // Check if driveBean exists
        if (driveActive === true) {
            stopTimer();  // Existing function
            updateVehicleStatus().then(r => {});
        } else {
            goBack();    // New function to navigate back
        }
    });
});

function goBack() {
    window.location.href=HOME_PAGE_URL;
}