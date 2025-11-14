-- Ініціалізація налаштувань зарплати
-- Виконайте цей скрипт один раз після запуску додатку

-- Налаштування для Офіціантів (role_id = 3)
INSERT INTO salary_settings (role_id, hourly_rate, order_bonus, review_bonus_coefficient, absence_penalty) 
VALUES (3, 100.0, 10.0, 20.0, 200.0);

-- Налаштування для Кухарів (role_id = 4)
INSERT INTO salary_settings (role_id, hourly_rate, order_bonus, review_bonus_coefficient, absence_penalty) 
VALUES (4, 120.0, 15.0, 25.0, 250.0);

-- Пояснення:
-- hourly_rate - ставка за годину роботи (грн)
-- order_bonus - бонус за кожне замовлення (грн)
-- review_bonus_coefficient - коефіцієнт для розрахунку премії за відгуки (грн)
-- absence_penalty - штраф за прогул (грн)

