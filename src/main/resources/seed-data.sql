-- 1. Sensor types (reference data)
INSERT INTO sensor_types (id, type, label) VALUES
                                               (1, 'TEMPERATURE', 'Temperature Sensor'),
                                               (2, 'HUMIDITY_AIR', 'Air Humidity Sensor'),
                                               (3, 'SOIL_MOISTURE', 'Soil Moisture Sensor'),
                                               (4, 'LIGHT', 'Light Sensor'),
                                               (5, 'EC', 'EC Sensor');

-- 2. Plant species (the defaults PlantInstance falls back to when custom_* is null)
INSERT INTO plant_species
(id, name, description, temp_min, temp_max, soil_moisture_min, soil_moisture_max, light_min, recommended_pot_size)
VALUES
    (1, 'Monstera deliciosa', 'Swiss Cheese Plant', 18.0, 27.0, 30, 60, 2000, 30),
    (2, 'Ficus lyrata', 'Fiddle Leaf Fig', 16.0, 26.0, 25, 55, 3000, 35),
    (3, 'Sansevieria trifasciata', 'Snake Plant', 15.0, 30.0, 15, 40, 1000, 20),
    (4, 'Spathiphyllum', 'Peace Lily', 18.0, 25.0, 35, 65, 1500, 25);

-- 3. Plant instances — one per species
INSERT INTO plant_instances
(id, name, species_id, height, pot_size, state, active, last_watered, last_checked)
SELECT
    nextval('plant_instances_id_seq'),
    'Monstera #1',
    id,
    45.0,
    25,
    0,
    true,
    NOW() - INTERVAL '2 days',
    NOW() - INTERVAL '1 hour'
FROM plant_species WHERE name = 'Monstera deliciosa';

INSERT INTO plant_instances
(id, name, species_id, height, pot_size, state, active, last_watered, last_checked)
SELECT
    nextval('plant_instances_id_seq'),
    'Fiddle Leaf Fig',
    id,
    60.0,
    30,
    0,
    true,
    NOW() - INTERVAL '1 day',
    NOW() - INTERVAL '1 hour'
FROM plant_species WHERE name = 'Ficus lyrata';

INSERT INTO plant_instances
(id, name, species_id, height, pot_size, state, active, last_watered, last_checked)
SELECT
    nextval('plant_instances_id_seq'),
    'Snake Plant',
    id,
    35.0,
    15,
    0,
    true,
    NOW() - INTERVAL '10 days',
    NOW() - INTERVAL '1 hour'
FROM plant_species WHERE name = 'Sansevieria trifasciata';

INSERT INTO plant_instances
(id, name, species_id, height, pot_size, state, active, last_watered, last_checked, soil_moisture_min)
SELECT
    nextval('plant_instances_id_seq'),
    'Peace Lily',
    id,
    25.0,
    18,
    1,  -- state 1 = needs attention
    true,
    NOW() - INTERVAL '5 days',
    NOW() - INTERVAL '1 hour',
    45  -- custom soil moisture min
FROM plant_species WHERE name = 'Spathiphyllum';

-- 4. Sensors — attach all 5 sensor types to every plant instance
INSERT INTO sensors (id, plant_id, type, label, active)
SELECT
    nextval('sensors_id_seq'),
    p.id,
    st.type,
    p.name || ' - ' || st.type,
    true
FROM plant_instances p
         CROSS JOIN sensor_types st;

-- 5. Generate initial telemetry data for each plant
INSERT INTO telemetry (id, plant_id, sensor_id, temp, humidity, soil_moisture, light, timestamp)
SELECT
    nextval('telemetry_id_seq'),
    p.id,
    s.id,
    CASE
        WHEN s.type = 'TEMPERATURE' THEN 22.5 + (random() * 5)
        ELSE NULL
        END,
    CASE
        WHEN s.type = 'HUMIDITY_AIR' THEN 45 + (random() * 30)
        ELSE NULL
        END,
    CASE
        WHEN s.type = 'SOIL_MOISTURE' THEN 35 + (random() * 30)
        ELSE NULL
        END,
    CASE
        WHEN s.type = 'LIGHT' THEN 1500 + (random() * 4000)
        ELSE NULL
        END,
    NOW() - (random() * INTERVAL '2 hours')
FROM plant_instances p
         CROSS JOIN sensors s
WHERE s.plant_id = p.id;

-- 6. Add some events for each plant
INSERT INTO events (id, plant_id, type, action, time, status)
SELECT
    nextval('events_id_seq'),
    p.id,
    'WATERING',
    'Automatic watering triggered',
    NOW() - INTERVAL '1 day',
    2  -- completed
FROM plant_instances p
WHERE p.name = 'Monstera #1';

INSERT INTO events (id, plant_id, type, action, time, status)
SELECT
    nextval('events_id_seq'),
    p.id,
    'HEATING',
    'Temperature adjusted',
    NOW() - INTERVAL '2 days',
    2  -- completed
FROM plant_instances p
WHERE p.name = 'Fiddle Leaf Fig';

INSERT INTO events (id, plant_id, type, action, time, status)
SELECT
    nextval('events_id_seq'),
    p.id,
    'LIGHT_CONTROL',
    'Light intensity adjusted',
    NOW() - INTERVAL '3 days',
    2  -- completed
FROM plant_instances p
WHERE p.name = 'Snake Plant';

INSERT INTO events (id, plant_id, type, action, time, status)
SELECT
    nextval('events_id_seq'),
    p.id,
    'WATERING',
    'Low soil moisture detected',
    NOW() - INTERVAL '12 hours',
    0  -- pending
FROM plant_instances p
WHERE p.name = 'Peace Lily';

-- 7. Add some recommendations
INSERT INTO recommendations (id, plant_id, message, severity, resolved, created_at)
SELECT
    nextval('recommendations_id_seq'),
    p.id,
    'Soil moisture is low. Please water the plant.',
    'WARNING',
    false,
    NOW() - INTERVAL '1 day'
FROM plant_instances p
WHERE p.name = 'Peace Lily';

INSERT INTO recommendations (id, plant_id, message, severity, resolved, created_at)
SELECT
    nextval('recommendations_id_seq'),
    p.id,
    'Plant is growing well. Consider repotting soon.',
    'INFO',
    false,
    NOW() - INTERVAL '3 days'
FROM plant_instances p
WHERE p.name = 'Monstera #1';

INSERT INTO recommendations (id, plant_id, message, severity, resolved, created_at)
SELECT
    nextval('recommendations_id_seq'),
    p.id,
    'Temperature is below optimal range.',
    'CRITICAL',
    false,
    NOW() - INTERVAL '2 days'
FROM plant_instances p
WHERE p.name = 'Fiddle Leaf Fig';

-- 8. Reset sequences to avoid conflicts
SELECT setval('plant_instances_id_seq', (SELECT MAX(id) FROM plant_instances));
SELECT setval('sensors_id_seq', (SELECT MAX(id) FROM sensors));
SELECT setval('telemetry_id_seq', (SELECT MAX(id) FROM telemetry));
SELECT setval('events_id_seq', (SELECT MAX(id) FROM events));
SELECT setval('recommendations_id_seq', (SELECT MAX(id) FROM recommendations));
SELECT setval('plant_species_id_seq', (SELECT MAX(id) FROM plant_species));