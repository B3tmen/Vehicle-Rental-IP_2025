const LOCATION_BL = [44.757959, 17.187796];
const BLUE_MARKER_URL = 'https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-blue.png';
const RED_MARKER_URL = 'https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-red.png';
let startMarker = null; // Marker for the start location
let endMarker = null;   // Marker for the end location
let startLocation = {lat: null, lng: null};
let endLocation = {lat: null, lng: null};
// Event listeners for manual selection
const byHandStartRadio = document.getElementById('radioBtnByHandStart');
const byHandEndRadio = document.getElementById('radioBtnByHandEnd');
const automaticStartRadio = document.getElementById('radioBtnAutomaticStart');
const map = L.map('map').setView(LOCATION_BL, 13);

const BLUE_ICON = L.icon({
    iconUrl: BLUE_MARKER_URL,
    iconAnchor: [12, 40],     // Center-bottom anchor (16px from left, 32px from top)
    popupAnchor: [0, -32]     // Popup opens above the marker
});
const RED_ICON = L.icon({
    iconUrl: RED_MARKER_URL,
    iconAnchor: [12, 40],
    popupAnchor: [0, -32]
});

// ----------------------------- LEAFLET OPEN STREET MAP ---------------------------------------
// Adding a tile layer (OpenStreetMap)
L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
}).addTo(map);
map.locate({setView: true, maxZoom: 16});

// Function to clear a specific marker
function clearMarker(marker) {
    if (marker) {
        map.removeLayer(marker);
    }
}

// Function to get address from coordinates
function getAddress(lat, lng, marker, label) {
    const url = `https://nominatim.openstreetmap.org/reverse?lat=${lat}&lon=${lng}&format=json&addressdetails=1`;

    fetch(url)
        .then(response => response.json())
        .then(data => {
            if (data && data.address) {
                const address = data.address;
                const fullAddress = `${address.road || ''}, ${address.city || ''}, ${address.state || ''}, ${address.country || ''}`;
                marker.bindPopup(`<b>${label} Location:</b><br>${fullAddress}`).openPopup();
            }
        })
        .catch(error => console.error("Error fetching address:", error));
}

// Select starting location by hand
function onStartLocationClick(e) {
    startLocation = {lat: e.latlng.lat, lng: e.latlng.lng};
    clearMarker(startMarker);
    startMarker = L.marker(e.latlng, {icon: BLUE_ICON}).addTo(map);
    getAddress(e.latlng.lat, e.latlng.lng, startMarker, 'Start');

    // Store values in hidden input fields
    document.getElementById("startLat").value = startLocation.lat;
    document.getElementById("startLng").value = startLocation.lng;
}

// Select end location by hand
function onEndLocationClick(e) {
    endLocation = {lat: e.latlng.lat, lng: e.latlng.lng};
    clearMarker(endMarker);
    endMarker = L.marker(e.latlng, {icon: RED_ICON}).addTo(map);
    getAddress(e.latlng.lat, e.latlng.lng, endMarker, 'End');

    // Store values in hidden input fields
    document.getElementById("endLat").value = endLocation.lat;
    document.getElementById("endLng").value = endLocation.lng;
}

// Automatic start location
map.on('locationfound', function (e) {
    clearMarker(startMarker);
    startMarker = L.marker(e.latlng, {icon: BLUE_ICON}).addTo(map)
        .bindPopup("You are within " + e.accuracy + " meters from this point").openPopup();
    L.circle(e.latlng, e.accuracy).addTo(map);
});

map.on('locationerror', function (e) {
    alert("Unable to access your location: " + e.message);
});

byHandStartRadio.addEventListener('change', function () {
    if (byHandStartRadio.checked) {
        map.off('locationfound');
        map.off('click');
        alert('Click on the map to set your start location!');
        map.on('click', onStartLocationClick);
    }
});

automaticStartRadio.addEventListener('change', function () {
    if (automaticStartRadio.checked) {
        map.off('click');
        clearMarker(startMarker);
        map.locate({setView: true, maxZoom: 16});
    }
});

byHandEndRadio.addEventListener('change', function () {
    if (byHandEndRadio.checked) {
        map.off('click');
        alert('Click on the map to set your end location!');
        map.on('click', onEndLocationClick);
    }
});