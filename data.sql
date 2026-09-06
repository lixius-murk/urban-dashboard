
DELETE FROM recommendations;
DELETE FROM commands;
DELETE FROM events;
DELETE FROM telemetry;
DELETE FROM sensors;
DELETE FROM plant_instances;
DELETE FROM plant_species;

ALTER SEQUENCE plant_instances_id_seq RESTART WITH 1;
ALTER SEQUENCE sensors_id_seq RESTART WITH 1;
ALTER SEQUENCE telemetry_id_seq RESTART WITH 1;
ALTER SEQUENCE events_id_seq RESTART WITH 1;
ALTER SEQUENCE recommendations_id_seq RESTART WITH 1;
ALTER SEQUENCE plant_species_id_seq RESTART WITH 1;

INSERT INTO plant_species (id, name, description, temp_min, temp_max, soil_moisture_min, soil_moisture_max, light_min, recommended_pot_size) VALUES
                                                                                                                                                 (1, 'Monstera', ' ', 18.0, 27.0, 30, 60, 2000, 30),
                                                                                                                                                 (2, 'Moraceae', ' ', 16.0, 26.0, 25, 55, 3000, 35),
                                                                                                                                                 (3, 'Asparagaceae', ' ', 15.0, 30.0, 15, 40, 1000, 20),
                                                                                                                                                 (4, 'Araceae', ' ', 18.0, 25.0, 35, 65, 1500, 25);

INSERT INTO plant_instances (id, name, id_species, height, pot_size, state, active, last_watered, last_checked, temp_min, soil_moisture_min, light_min) VALUES
                                                                                                                                                            (1, 'Monstera alcirana', 1, 45.0, 25, 0, true, NOW() - INTERVAL '2 days', NOW() - INTERVAL '1 hour', 18.0, 30, 2000),
                                                                                                                                                            (2, 'Fiddle Leaf Fig', 2, 60.0, 30, 0, true, NOW() - INTERVAL '1 day', NOW() - INTERVAL '1 hour', 16.0, 25, 3000),
                                                                                                                                                            (3, 'Snake Plant', 3, 35.0, 15, 0, true, NOW() - INTERVAL '10 days', NOW() - INTERVAL '1 hour', 15.0, 15, 1000),
                                                                                                                                                            (4, 'Peace Lily', 4, 25.0, 18, 1, true, NOW() - INTERVAL '5 days', NOW() - INTERVAL '1 hour', 18.0, 45, 1500);

INSERT INTO sensors (id, id_plant, type, label, active) VALUES
                                                            (1, 1, 'TEMPERATURE', 'Monstera #1 - Temperature', true),
                                                            (2, 1, 'HUMIDITY_AIR', 'Monstera #1 - Humidity', true),
                                                            (3, 1, 'SOIL_MOISTURE', 'Monstera #1 - Soil Moisture', true),
                                                            (4, 1, 'LIGHT', 'Monstera #1 - Light', true),
                                                            (5, 1, 'EC', 'Monstera #1 - EC', true),

                                                            (6, 2, 'TEMPERATURE', 'Fiddle Leaf Fig - Temperature', true),
                                                            (7, 2, 'HUMIDITY_AIR', 'Fiddle Leaf Fig - Humidity', true),
                                                            (8, 2, 'SOIL_MOISTURE', 'Fiddle Leaf Fig - Soil Moisture', true),
                                                            (9, 2, 'LIGHT', 'Fiddle Leaf Fig - Light', true),
                                                            (10, 2, 'EC', 'Fiddle Leaf Fig - EC', true),

                                                            (11, 3, 'TEMPERATURE', 'Snake Plant - Temperature', true),
                                                            (12, 3, 'HUMIDITY_AIR', 'Snake Plant - Humidity', true),
                                                            (13, 3, 'SOIL_MOISTURE', 'Snake Plant - Soil Moisture', true),
                                                            (14, 3, 'LIGHT', 'Snake Plant - Light', true),
                                                            (15, 3, 'EC', 'Snake Plant - EC', true),

                                                            (16, 4, 'TEMPERATURE', 'Peace Lily - Temperature', true),
                                                            (17, 4, 'HUMIDITY_AIR', 'Peace Lily - Humidity', true),
                                                            (18, 4, 'SOIL_MOISTURE', 'Peace Lily - Soil Moisture', true),
                                                            (19, 4, 'LIGHT', 'Peace Lily - Light', true),
                                                            (20, 4, 'EC', 'Peace Lily - EC', true);

INSERT INTO telemetry (id, id_plant, id_sensor, temp, humidity, soil_moisture, light, timestamp, source) VALUES
                                                                                                             (1, 1, 1, 23.5, 55, 42, 2500, NOW() - INTERVAL '2 hours', 'SIMULATOR'),
                                                                                                             (2, 2, 6, 22.0, 48, 38, 3200, NOW() - INTERVAL '1 hour', 'SIMULATOR'),
                                                                                                             (3, 3, 11, 25.0, 35, 28, 1800, NOW() - INTERVAL '3 hours', 'SIMULATOR'),
                                                                                                             (4, 4, 16, 24.0, 65, 32, 1600, NOW() - INTERVAL '30 minutes', 'SIMULATOR');

INSERT INTO events (id, id_plant, type, action, time, status) VALUES
                                                                  (1, 1, 'WATERING', 'Automatic watering triggered', NOW() - INTERVAL '1 day', 2),
                                                                  (2, 2, 'HEATING', 'Temperature adjusted', NOW() - INTERVAL '2 days', 2),
                                                                  (3, 3, 'LIGHT_CONTROL', 'Light intensity adjusted', NOW() - INTERVAL '3 days', 2),
                                                                  (4, 4, 'WATERING', 'Low soil moisture detected', NOW() - INTERVAL '1 hours', 0);

INSERT INTO recommendations (id, id_plant, message, severity, resolved, created_at) VALUES
                                                                                        (1, 4, 'Soil moisture is low. Please water the plant.', 'WARNING', false, NOW() - INTERVAL '1 day'),
                                                                                        (2, 1, 'Plant is growing well. Consider repotting soon.', 'INFO', false, NOW() - INTERVAL '3 days'),
                                                                                        (3, 2, 'Temperature is below optimal range.', 'CRITICAL', false, NOW() - INTERVAL '2 days');

SELECT setval('plant_instances_id_seq', COALESCE((SELECT MAX(id) FROM plant_instances), 4));
SELECT setval('sensors_id_seq', COALESCE((SELECT MAX(id) FROM sensors), 20));
SELECT setval('telemetry_id_seq', COALESCE((SELECT MAX(id) FROM telemetry), 4));
SELECT setval('events_id_seq', COALESCE((SELECT MAX(id) FROM events), 4));
SELECT setval('recommendations_id_seq', COALESCE((SELECT MAX(id) FROM recommendations), 3));
SELECT setval('plant_species_id_seq', COALESCE((SELECT MAX(id) FROM plant_species), 4));