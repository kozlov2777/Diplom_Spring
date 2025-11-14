# 🐘 Налаштування PostgreSQL з тестовими даними

## 🚀 Швидкий старт (Автоматичне налаштування)

### Метод 1: Один скрипт для всього (РЕКОМЕНДОВАНО)

```bash
./setup_postgresql.sh
```

Цей скрипт автоматично:
- ✅ Створить базу даних `diplom_master`
- ✅ Використовує стандартного користувача `postgres`
- ✅ Надасть всі необхідні права
- ✅ Оновить Maven залежності
- ✅ Запустить Spring Boot для створення таблиць
- ✅ Заповнить БД тестовими даними

**Після виконання скрипта просто запустіть:**
```bash
mvn spring-boot:run
```

**І відкрийте в браузері:**
```
http://localhost:8080/login
```

---

## 📋 Ручне налаштування (крок за кроком)

### Крок 1: Встановлення PostgreSQL

#### macOS:
```bash
brew install postgresql@15
brew services start postgresql@15
```

#### Ubuntu/Debian:
```bash
sudo apt update
sudo apt install postgresql postgresql-contrib
sudo systemctl start postgresql
sudo systemctl enable postgresql
```

#### Windows:
Завантажте інсталятор з https://www.postgresql.org/download/windows/

### Крок 2: Створення бази даних

```bash
# Увійдіть в PostgreSQL
psql postgres  # macOS
# або
sudo -u postgres psql  # Linux
```

Виконайте в psql:
```sql
-- Користувач postgres вже існує
-- Створення БД
CREATE DATABASE diplom_master OWNER postgres;

-- Надання прав
GRANT ALL PRIVILEGES ON DATABASE diplom_master TO postgres;

-- Вихід
\q
```

### Крок 3: Налаштування прав на схему

```bash
psql -d diplom_master
```

```sql
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON ALL TABLES IN SCHEMA public TO postgres;
GRANT ALL ON ALL SEQUENCES IN SCHEMA public TO postgres;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO postgres;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO postgres;

\q
```

### Крок 4: Оновлення залежностей

```bash
mvn clean install
```

### Крок 5: Перший запуск (створення таблиць)

```bash
mvn spring-boot:run
```

Зачекайте ~10 секунд поки Hibernate створить всі таблиці, потім зупиніть (Ctrl+C).

### Крок 6: Заповнення даними

```bash
psql -U student -d diplom_master -f populate_database.sql
```

### Крок 7: Запуск програми

```bash
mvn spring-boot:run
```

Відкрийте: http://localhost:8080/login

---

## 📊 Що містять тестові дані

### 👥 Користувачі (всі паролі: **123**)

| Логін | Роль | Ім'я |
|-------|------|------|
| `admin` | Адміністратор | Олександр Коваленко |
| `maria_admin` | Адміністратор | Марія Шевченко |
| `ivan_cook` | Кухар | Іван Петренко |
| `andrii_cook` | Кухар | Андрій Мельник |
| `tetiana_cook` | Кухар | Тетяна Бондаренко |
| `natalia_waiter` | Офіціант | Наталія Іваненко |
| `dmitro_waiter` | Офіціант | Дмитро Ковальчук |
| `olena_waiter` | Офіціант | Олена Савченко |
| `serhii_waiter` | Офіціант | Сергій Мороз |

### 🍽️ Меню (34 страви)

**Супи:** Борщ, Солянка, Курячий бульйон  
**Салати:** Цезар, Грецький, Олів'є, та інші  
**Основні страви:** Котлета по-київськи, Стейк, Шашлик, Голубці, та інші  
**Гарніри:** Картопля (різні види), Гречка, Рис, Овочі гриль  
**Десерти:** Тірамісу, Чізкейк, Наполеон, Морозиво  
**Напої:** Кава (різні види), Чай, Соки, Вода, Компот  
**Закуски:** Сирна тарілка, М'ясна тарілка, Брускети, Оселедець  

### 📦 Інгредієнти (30 позицій)
Всі необхідні інгредієнти для приготування страв з реалістичними залишками на складі.

### 📋 Замовлення (25 штук)
- Останні 30 днів історії
- Різні статуси (В обробці, Готується, Готово, Доставлено)
- Розподілені між офіціантами

### ⭐ Відгуки (10 штук)
Реалістичні відгуки клієнтів з оцінками 4-5 зірок.

### 🪑 Столики (15 штук)
Всі вільні та готові до роботи.

### 📅 Графік роботи
Поточний тиждень для всіх офіціантів та кухарів.

---

## 🔧 Корисні команди PostgreSQL

### Підключення до БД
```bash
psql -U postgres -d diplom_master
```

### Перегляд таблиць
```sql
\dt
```

### Кількість записів у таблиці
```sql
SELECT COUNT(*) FROM employees;
SELECT COUNT(*) FROM orders;
SELECT COUNT(*) FROM menu_items;
```

### Очистка таблиці
```sql
TRUNCATE TABLE order_items CASCADE;
```

### Повне видалення всіх даних
```sql
TRUNCATE TABLE order_items, orders, reviews, ingredient_items, menu_items, 
              ingredients, categories, work_schedule, shift_change_requests,
              salary_calculation, employees, tables, shifts, schedule_settings, 
              salary_settings, roles, statuses, table_statuses 
RESTART IDENTITY CASCADE;
```

### Повторне заповнення даними
```bash
psql -U postgres -d diplom_master -f populate_database.sql
```

### Backup БД
```bash
pg_dump -U postgres diplom_master > backup.sql
```

### Restore БД
```bash
psql -U postgres -d diplom_master < backup.sql
```

---

## 🧪 Тестування функціоналу

Після запуску програми перевірте:

### 1. Вхід в систему
- Адмін: `admin / 123`
- Офіціант: `natalia_waiter / 123`
- Кухар: `ivan_cook / 123`

### 2. Створення замовлення
1. Перейдіть на "Нове замовлення"
2. Оберіть столик
3. Оберіть офіціанта
4. Додайте страви з меню
5. Підтвердіть замовлення

### 3. Перегляд даних
- **Список замовлень** - має бути 25 замовлень
- **Меню** - 34 страви в 7 категоріях
- **Статус столиків** - 15 вільних столиків
- **Відгуки** (тільки для адміна) - 10 відгуків

### 4. Адміністративні функції (тільки для адміна)
- **Дашборд** - статистика та графіки
- **Зарплата** - розрахунок зарплат працівників
- **Графік роботи** - поточний графік
- **Інвентар** - залишки інгредієнтів
- **Аналітика** - графіки продажів

---

## ❓ Часті проблеми та рішення

### Проблема 1: "password authentication failed for user postgres"
**Рішення:** Встановіть пароль для користувача postgres:
```bash
psql postgres
```
```sql
ALTER USER postgres WITH PASSWORD 'postgres';
\q
```

### Проблема 2: "psql: command not found"
**Рішення:** PostgreSQL не встановлено або не доданий до PATH
```bash
# macOS
brew install postgresql@15
echo 'export PATH="/opt/homebrew/opt/postgresql@15/bin:$PATH"' >> ~/.zshrc
source ~/.zshrc
```

### Проблема 3: "permission denied for schema public"
**Рішення:**
```bash
psql -U postgres -d diplom_master
```
```sql
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON ALL TABLES IN SCHEMA public TO postgres;
GRANT ALL ON ALL SEQUENCES IN SCHEMA public TO postgres;
```

### Проблема 4: "database 'diplom_master' does not exist"
**Рішення:**
```sql
CREATE DATABASE diplom_master OWNER postgres;
```

### Проблема 5: Порт 5432 зайнятий
**Рішення:** Змініть порт в `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5433/diplom_master
```

### Проблема 6: Hibernate не може створити таблиці
**Рішення:** Перевірте права користувача:
```sql
ALTER USER postgres CREATEDB;
GRANT ALL ON DATABASE diplom_master TO postgres;
```

---

## 📱 Структура проекту після налаштування

```
diplom_master (PostgreSQL Database)
├── employees (9 записів)
├── roles (3 записи)
├── statuses (4 записи)
├── table_statuses (2 записи)
├── categories (7 записів)
├── ingredients (30 записів)
├── menu_items (34 записи)
├── ingredient_items (зв'язки)
├── tables (15 записів)
├── orders (25 записів)
├── order_items (багато записів)
├── reviews (10 записів)
├── shifts (2 записи)
├── schedule_settings (2 записи)
├── salary_settings (2 записи)
└── work_schedule (графік на тиждень)
```

---

## 🎯 Наступні кроки

1. ✅ Налаштувати PostgreSQL
2. ✅ Заповнити тестовими даними
3. 🚀 Запустити програму
4. 🧪 Протестувати функціонал
5. 🎨 Насолодитись красивим UI!

---

## 💡 Додаткові можливості

### Автоматичний запуск PostgreSQL при старті системи

**macOS:**
```bash
brew services start postgresql@15
```

**Linux:**
```bash
sudo systemctl enable postgresql
```

### Зміна пароля користувача
```sql
ALTER USER postgres WITH PASSWORD 'новий_пароль';
```

Потім оновіть `application.properties`:
```properties
spring.datasource.password=новий_пароль
```

### Створення додаткового адміністратора
```sql
INSERT INTO employees (first_name, last_name, username, password, role_id) 
VALUES ('Твоє', 'Ім''я', 'твій_логін', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 1);
```

---

## 📞 Підтримка

Якщо виникли проблеми:
1. Перевірте логи Spring Boot
2. Перевірте з'єднання: `psql -U postgres -d diplom_master`
3. Спробуйте перезапустити PostgreSQL
4. Виконайте `mvn clean install` знову

---

## 🎉 Готово!

Тепер у вас повністю робоча система управління рестораном з:
- 🐘 PostgreSQL замість MariaDB
- 📊 Реалістичними тестовими даними
- 🎨 Красивим сучасним UI
- ⚡ Швидким та зручним функціоналом

**Приємної роботи!** 🍽️✨

