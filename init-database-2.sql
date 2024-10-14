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
(150.00, '2024-09-16', 'Deep cleaning for large houses', 'Deep Clean', 'active', '2024-09-16', 3),
(200.00, '2024-09-17', 'Specialized cleaning for offices', 'Office Clean', 'inactive', '2024-09-17', 2),
(75.00, '2024-09-18', 'Quick apartment cleaning service', 'Quick Clean', 'active', '2024-09-18', 1),
(180.00, '2024-09-19', 'Thorough carpet cleaning service', 'Carpet Clean', 'active', '2024-09-19', 5);

-- Insert initial durations (1, 2, 4, and 7 hours)
INSERT INTO duration (area, duration_in_hours, price)
VALUES
(50.0, 1, 70.00),   -- 1 hour for small area (50 sqm)
(100.0, 2, 120.00), -- 2 hours for medium area (100 sqm)
(150.0, 4, 180.00), -- 4 hours for larger area (150 sqm)
(200.0, 7, 250.00); -- 7 hours for very large area (200 sqm)
