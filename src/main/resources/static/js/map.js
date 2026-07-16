document.addEventListener("DOMContentLoaded", function () {

    if (!navigator.geolocation) {
        alert("Geolocation is not supported.");
        return;
    }

    navigator.geolocation.getCurrentPosition(showMap, showError);

});

function showMap(position) {

    const latitude = position.coords.latitude;
    const longitude = position.coords.longitude;

    const map = L.map("map").setView([latitude, longitude], 14);

    L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
        attribution: "&copy; OpenStreetMap contributors"
    }).addTo(map);

    // User marker (default blue marker)
    L.marker([latitude, longitude])
        .addTo(map)
        .bindPopup("<b>📍 Your Location</b>")
        .openPopup();

    loadHospitals(map, latitude, longitude);

}

// Custom hospital icon
const hospitalIcon = L.icon({

    iconUrl: '/images/hospital.webp',

    iconSize: [32, 32],

    iconAnchor: [16, 32],

    popupAnchor: [0, -30]

});

function loadHospitals(map, lat, lon) {

    const query = `
[out:json];
(
  node["amenity"="hospital"](around:5000,${lat},${lon});
  way["amenity"="hospital"](around:5000,${lat},${lon});
  relation["amenity"="hospital"](around:5000,${lat},${lon});
);
out center;
`;

    fetch("https://overpass-api.de/api/interpreter", {
        method: "POST",
        body: query
    })
    .then(response => response.json())
    .then(data => {

        data.elements.forEach(hospital => {

            const hLat = hospital.lat || hospital.center.lat;
            const hLon = hospital.lon || hospital.center.lon;

            const hospitalName =
                hospital.tags.name || "Unnamed Hospital";

            L.marker([hLat, hLon], {
                icon: hospitalIcon
            })
            .addTo(map)
            .bindPopup("<b>🏥 " + hospitalName + "</b>");

        });

    })
    .catch(error => console.log(error));

}

function showError(error) {

    alert("Unable to fetch your location.");

}