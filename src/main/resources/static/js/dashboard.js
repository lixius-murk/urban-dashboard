let markers = [];

async function loadSensors() {

    markers.forEach(marker => window.map.removeLayer(marker));
    markers = [];

    const response = await fetch('/api/sensors');
    const sensors = await response.json();

    let activeEvent = 0;
    let noiseEvents = 0;
    let manholeEvent = 0;
    let overcrowdedEvent = 0;


    sensors.forEach(sensor => {

        let color = "green";


        if (sensor.status === "OPEN") {
            color = "red";
            manholeEvent++;
            activeEvent++;
        }

        if (sensor.status === "LOUD") {
            color = "orange";
            noiseEvents++;
            activeEvent++;
        }

        if (sensor.status === "CROWDED") {
            color = "purple";
            overcrowdedEvent++;
            activeEvent++;
        }

        const marker = L.circleMarker(
            [sensor.latitude, sensor.longitude],
            {
                radius: 10,
                color: color
            }
        ).addTo(window.map);

        marker.bindPopup(`
            <b>${sensor.type}</b><br>
            Status: ${sensor.status}<br>
            Value: ${sensor.value}
        `);

        markers.push(marker);
    });
    document.getElementById("sum-calls").innerText = activeEvent;
    document.getElementById("noise-calls").innerText = noiseEvents;
    document.getElementById("manholes-calls").innerText = manholeEvent;
    document.getElementById("overcrowded-calls").innerText = manholeEvent;

}

loadSensors();

setInterval(loadSensors, 3000);