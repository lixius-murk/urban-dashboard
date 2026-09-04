
INSERT INTO sensor_types (name) VALUES
    ('TEMPERATURE'),
    ('HUMIDITY_AIR'),
    ('SOIL_MOISTURE'),
    ('LIGHT'),
    ('EC');

-- 2. Plant species (the defaults PlantInstance falls back to when custom_* is null)
INSERT INTO plant_species
    (name, temp_min, temp_max, hum_min, hum_max, soil_moisture_min, soil_moisture_max, light_min, recommended_pot_size_cm, is_active)
VALUES
    ('Monstera deliciosa', 18.0, 27.0, 40, 70, 30, 60, 2000, 30, true),
    ('Ficus lyrata',       16.0, 26.0, 30, 60, 25, 55, 3000, 35, true),
    ('Sansevieria trifasciata', 15.0, 30.0, 20, 50, 15, 40, 1000, 20, true),
    ('Spathiphyllum',      18.0, 25.0, 50, 80, 35, 65, 1500, 25, true);

-- 3. Plant instances — one per species, referencing species by name
--    (avoids hardcoding species IDs, which depend on insert order/identity sequence)
INSERT INTO plant_instances
    (id_species, name, planted_at, current_height_cm, current_pot_size_cm, current_state,
     is_active, health_status, last_watered_at)
SELECT id_species, 'Monstera #1', now() - interval '90 days', 45.0, 25, 0, true, 'HEALTHY', now() - interval '2 days'
FROM plant_species WHERE name = 'Monstera deliciosa';

INSERT INTO plant_instances
    (id_species, name, planted_at, current_height_cm, current_pot_size_cm, current_state,
     is_active, health_status, last_watered_at)
SELECT id_species, 'Fiddle Leaf Fig', now() - interval '60 days', 60.0, 30, 0, true, 'HEALTHY', now() - interval '1 days'
FROM plant_species WHERE name = 'Ficus lyrata';

INSERT INTO plant_instances
    (id_species, name, planted_at, current_height_cm, current_pot_size_cm, current_state,
     is_active, health_status, last_watered_at)
SELECT id_species, 'Snake Plant', now() - interval '200 days', 35.0, 15, 0, true, 'HEALTHY', now() - interval '10 days'
FROM plant_species WHERE name = 'Sansevieria trifasciata';

INSERT INTO plant_instances
    (id_species, name, planted_at, current_height_cm, current_pot_size_cm, current_state,
     is_active, health_status, last_watered_at, custom_soil_moisture_min)
SELECT id_species, 'Peace Lily', now() - interval '30 days', 25.0, 18, 1, true, 'ATTENTION', now() - interval '5 days', 45
FROM plant_species WHERE name = 'Spathiphyllum';

-- 4. Sensors — attach all 5 sensor types to every plant instance
--    (DataCollection/DataSimulator need at least one sensor per plant to generate telemetry)
INSERT INTO sensors (id_plant, id_sensor_type, label, is_active)
SELECT p.id_plant, st.id_sensor_type, p.name || ' - ' || st.name, true
FROM plant_instances p
CROSS JOIN sensor_types st;
