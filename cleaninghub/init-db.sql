use cleaninghub;

-- Insert initial categories
INSERT INTO category (description, name)
VALUES
('Cleaning services for homes', 'Home Cleaning'),
('Specialized office cleaning services', 'Office Cleaning'),
('Deep cleaning services for various spaces', 'Deep Cleaning'),
('Quick, short-duration cleaning tasks', 'Quick Cleaning'),
('Specialized carpet cleaning services', 'Carpet Cleaning');

-- Insert initial services
INSERT INTO service (base_price, create_date, description, name, status, update_date, category_id)
VALUES
(120.50, '2024-09-15', 'Standard house cleaning service', 'Standard Clean', 'active', '2024-09-15', 1),
(150.00, '2024-09-16', 'Deep cleaning for large houses', 'Deep Clean', 'active', '2024-09-16', 2),
(200.00, '2024-09-17', 'Specialized cleaning for offices', 'Office Clean', 'inactive', '2024-09-17', 3),
(75.00, '2024-09-18', 'Quick apartment cleaning service', 'Quick Clean', 'active', '2024-09-18', 1),
(180.00, '2024-09-19', 'Thorough carpet cleaning service', 'Carpet Clean', 'active', '2024-09-19', 4);

-- Insert durations for 'Standard Clean' (Service ID: 1)
INSERT INTO duration (area, duration_in_hours, number_of_worker, price, service_id)
VALUES
(50.0, 2, 2, 100.00, 1),  -- Small House (50 sqm)
(100.0, 4, 3, 150.00, 1),  -- Medium House (100 sqm)
(200.0, 6, 4, 200.00, 1);  -- Large House (200 sqm)

-- Insert durations for 'Deep Clean' (Service ID: 2)
INSERT INTO duration (area, duration_in_hours, number_of_worker, price, service_id)
VALUES
(70.0, 4, 3, 180.00, 2),  -- Small House (70 sqm)
(150.0, 6, 4, 230.00, 2), -- Medium House (150 sqm)
(300.0, 8, 5, 300.00, 2); -- Large House (300 sqm)

-- Insert durations for 'Office Clean' (Service ID: 3)
INSERT INTO duration (area, duration_in_hours, number_of_worker, price, service_id)
VALUES
(80.0, 3, 2, 180.00, 3),  -- Small Office (80 sqm)
(200.0, 5, 3, 250.00, 3), -- Medium Office (200 sqm)
(400.0, 8, 4, 350.00, 3); -- Large Office (400 sqm)

-- Insert durations for 'Quick Clean' (Service ID: 4)
INSERT INTO duration (area, duration_in_hours, number_of_worker, price, service_id)
VALUES
(40.0, 1, 1, 70.00, 4),   -- Small Apartment (40 sqm)
(70.0, 2, 2, 90.00, 4),   -- Medium Apartment (70 sqm)
(100.0, 3, 2, 110.00, 4); -- Large Apartment (100 sqm)

-- Insert durations for 'Carpet Clean' (Service ID: 5)
INSERT INTO duration (area, duration_in_hours, number_of_worker, price, service_id)
VALUES
(20.0, 2, 1, 150.00, 5),  -- Small Carpet (20 sqm)
(50.0, 4, 2, 200.00, 5),  -- Medium Carpet (50 sqm)
(100.0, 6, 3, 250.00, 5); -- Large Carpet (100 sqm)
