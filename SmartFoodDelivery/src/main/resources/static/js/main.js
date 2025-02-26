// main.js

// Function to get the user's current location
function getLocation() {
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(showPosition, showError);
    } else {
        alert("Geolocation is not supported by this browser.");
    }
}

// Function to display the user's position
function showPosition(position) {
    const lat = position.coords.latitude;
    const long = position.coords.longitude;
    alert("Latitude: " + lat + "\nLongitude: " + long);

    // Here you can send the coordinates to your server or use them as needed
}

// Function to handle errors when retrieving the location
function showError(error) {
    switch(error.code) {
        case error.PERMISSION_DENIED:
            alert("User denied the request for Geolocation.");
            break;
        case error.POSITION_UNAVAILABLE:
            alert("Location information is unavailable.");
            break;
        case error.TIMEOUT:
            alert("The request to get user location timed out.");
            break;
        case error.UNKNOWN_ERROR:
            alert("An unknown error occurred.");
            break;
    }
}

// Function to send an email (modify this according to your backend implementation)
function sendEmail() {
    const emailInput = document.getElementById("emailInput").value;
    fetch(`/send-email?toEmail=${emailInput}`)
        .then(response => response.text())
        .then(data => {
            alert(data); // Show the server response
        })
        .catch(error => {
            console.error("Error sending email:", error);
            alert("Failed to send email. Please try again.");
        });
}

// Add event listeners after the DOM is loaded
document.addEventListener("DOMContentLoaded", () => {
    const orderButton = document.querySelector(".cta-button");
    const sendEmailButton = document.getElementById("sendEmailButton");

    // Show location on button click
    orderButton.addEventListener("click", () => {
        getLocation();
    });

    // Send email on button click
    sendEmailButton.addEventListener("click", () => {
        sendEmail();
    });
});


// Initialize and add the map
let map;
let marker;

// Function to get the user's current location
if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(function(position) {
        const lat = position.coords.latitude;
        const lng = position.coords.longitude;

        // Create a map centered at the user's location
        const map = new google.maps.Map(document.getElementById('map'), {
            center: { lat: lat, lng: lng },
            zoom: 15
        });

        // Place a marker on the user's location
        const marker = new google.maps.Marker({
            position: { lat: lat, lng: lng },
            map: map,
            title: 'You are here!'
        });

        // Reverse geocoding to get the address
        const geocoder = new google.maps.Geocoder();
        const latlng = { lat: lat, lng: lng };
        geocoder.geocode({ location: latlng }, (results, status) => {
            if (status === 'OK') {
                if (results[0]) {
                    const address = results[0].formatted_address;
                    document.getElementById('address').innerText = address; // Display the address
                } else {
                    window.alert('No results found');
                }
            } else {
                window.alert('Geocoder failed due to: ' + status);
            }
        });
    }, function() {
        handleLocationError(true);
    }, { timeout: 5000, maximumAge: 60000, enableHighAccuracy: true }); // Enhanced options
} else {
    // Browser doesn't support Geolocation
    handleLocationError(false);
}


// Function to display the user's position on the map
function showPosition(position) {
    const lat = position.coords.latitude;
    const long = position.coords.longitude;

    // Initialize the map centered at the user's location
    map = new google.maps.Map(document.getElementById("map"), {
        zoom: 15,
        center: { lat: lat, lng: long },
    });

    // Place a marker at the user's location
    marker = new google.maps.Marker({
        position: { lat: lat, lng: long },
        map: map,
        title: "You are here!",
    });
}

// Function to handle errors when retrieving the location
function showError(error) {
    switch(error.code) {
        case error.PERMISSION_DENIED:
            alert("User denied the request for Geolocation.");
            break;
        case error.POSITION_UNAVAILABLE:
            alert("Location information is unavailable.");
            break;
        case error.TIMEOUT:
            alert("The request to get user location timed out.");
            break;
        case error.UNKNOWN_ERROR:
            alert("An unknown error occurred.");
            break;
    }
}

// Function to initialize the map
function initMap() {
    // You can set an initial position here if needed
    const initialLocation = { lat: -34.397, lng: 150.644 }; // Example coordinates
    map = new google.maps.Map(document.getElementById("map"), {
        zoom: 15,
        center: initialLocation,
    });
}
