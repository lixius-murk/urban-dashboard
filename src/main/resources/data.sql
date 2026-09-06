
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

INSERT INTO plant_species (id, name, temp_min, temp_max, soil_moisture_min, soil_moisture_max, light_min, recommended_pot_size) VALUES
                                                                                                                                                 (1, 'Монстера',  18.0, 27.0, 30, 60, 2000, 30),
                                                                                                                                                 (2, 'Тутовые',  16.0, 26.0, 25, 55, 3000, 35),
                                                                                                                                                 (3, 'Спаржевые', 15.0, 30.0, 15, 40, 1000, 15),
                                                                                                                                                 (4, 'Ароидные', 18.0, 25.0, 35, 65, 1500, 25);

INSERT INTO plant_instances (id, name, id_species, height, pot_size, state, active, last_watered, last_checked, temp_min, soil_moisture_min, light_min) VALUES
                                                                                                                                                            (1, 'Монстера альцирана', 1, 45.0, 25, 0, true, NOW() - INTERVAL '2 days', NOW() - INTERVAL '1 hour', 18.0, 30, 2000),
                                                                                                                                                            (2, 'Инжир', 2, 60.0, 30, 0, true, NOW() - INTERVAL '1 day', NOW() - INTERVAL '1 hour', 16.0, 25, 3000),
                                                                                                                                                            (3, 'Змеиное растение', 3, 35.0, 15, 0, true, NOW() - INTERVAL '10 days', NOW() - INTERVAL '1 hour', 15.0, 15, 1000),
                                                                                                                                                            (4, 'Лилия мира', 4, 25.0, 18, 1, true, NOW() - INTERVAL '5 days', NOW() - INTERVAL '1 hour', 18.0, 45, 1500);
INSERT INTO sensors (id, id_plant, type, label, active) VALUES
                                                            (1, 1, 'TEMPERATURE', 'Монстера альцирана - темп', true),
                                                            (2, 1, 'HUMIDITY_AIR', 'Монстера альцирана - влажн воздуха', true),
                                                            (3, 1, 'SOIL_MOISTURE', 'Монстера альцирана - влажн', true),
                                                            (4, 1, 'LIGHT', 'Монстера альцирана - свт', true),
                                                            (6, 2, 'TEMPERATURE', 'Инжир - темп', true),
                                                            (7, 2, 'HUMIDITY_AIR', 'Инжир - влажн воздуха', true),
                                                            (8, 2, 'SOIL_MOISTURE', 'Инжир - влажн', true),
                                                            (9, 2, 'LIGHT', 'Инжир - свт', true),
                                                            (11, 3, 'TEMPERATURE', 'Змеиное растение - темп', true),
                                                            (12, 3, 'HUMIDITY_AIR', 'Змеиное растение - влажн воздуха', true),
                                                            (13, 3, 'SOIL_MOISTURE', 'Змеиное растение - влажн', true),
                                                            (14, 3, 'LIGHT', 'Змеиное растение - свт', true),
                                                            (16, 4, 'TEMPERATURE', 'Лилия мира - темп', true),
                                                            (17, 4, 'HUMIDITY_AIR', 'Лилия мира - влажн воздуха', true),
                                                            (18, 4, 'SOIL_MOISTURE', 'Лилия мира - влаж', true),
                                                            (19, 4, 'LIGHT', 'Лилия мира - свт', true);
INSERT INTO recommendation_msg (id, msg) VALUES (1, 'Низкая температура!'),
                                                    (2, 'Низкий уровень влажности воздуха!'),
                                                    (3, 'Низкий уровень влажности почвы!'),
                                                    (4, 'Недостаточно света!'),
                                                    (5, 'Высокая температура!'),
                                                    (6, 'Высокий уровень влажности воздуха!'),
                                                    (7, 'Высокий уровень влажности почвы!'),
                                                    (8, 'Слишком много света!');



INSERT INTO telemetry (id, id_plant, id_sensor, temp, humidity, soil_moisture, light, timestamp, source) VALUES
                                                                                                             (1, 1, 1, 23.5, 55, 42, 2500, NOW() - INTERVAL '2 hours', 'SIMULATOR'),
                                                                                                             (2, 2, 6, 22.0, 48, 38, 3200, NOW() - INTERVAL '1 hour', 'SIMULATOR'),
                                                                                                             (3, 3, 11, 25.0, 35, 28, 1800, NOW() - INTERVAL '3 hours', 'SIMULATOR'),
                                                                                                             (4, 4, 16, 24.0, 65, 32, 1600, NOW() - INTERVAL '30 minutes', 'SIMULATOR');


INSERT INTO recommendations (id, id_plant, msg_id, severity, resolved, created_at) VALUES
                                                                                        (1, 4, 1, 'WARNING', false, NOW() - INTERVAL '1 day'),
                                                                                        (2, 2, 1, 'WARNING', false, NOW() - INTERVAL '1 day'),

                                                                                        (3, 2, 2, 'CRITICAL', false, NOW() - INTERVAL '2 days');

SELECT setval('plant_instances_id_seq', COALESCE((SELECT MAX(id) FROM plant_instances), 4));
SELECT setval('sensors_id_seq', COALESCE((SELECT MAX(id) FROM sensors), 20));
SELECT setval('telemetry_id_seq', COALESCE((SELECT MAX(id) FROM telemetry), 4));
SELECT setval('events_id_seq', COALESCE((SELECT MAX(id) FROM events), 4));
SELECT setval('recommendations_id_seq', COALESCE((SELECT MAX(id) FROM recommendations), 3));
SELECT setval('plant_species_id_seq', COALESCE((SELECT MAX(id) FROM plant_species), 4));