# 🍽️ Система управління рестораном

Повнофункціональна інформаційна система для управління рестораном, розроблена на Spring Boot 3.1.3 з PostgreSQL.

## ✨ Основні можливості

### 👥 Управління персоналом
- Реєстрація та автентифікація працівників
- 3 ролі: Адміністратор, Кухар, Офіціант
- Автоматичний розрахунок зарплати (години, замовлення, відгуки, штрафи)
- Генерація та редагування графіку роботи
- Запити на зміну графіку

### 📋 Управління замовленнями
- Швидке створення замовлень (інтерфейс як в інтернет-магазині)
- Відстеження статусів замовлень
- Історія замовлень за період
- Автоматичне списання інгредієнтів зі складу
- Прив'язка до столиків та офіціантів

### 🍽️ Меню та інгредієнти
- Повне меню з 34 стравами в 7 категоріях
- Інформація про калорійність
- Управління інгредієнтами та складом
- Автоматичне відстеження залишків
- Сповіщення про низькі залишки

### ⭐ Система відгуків
- Клієнти можуть залишати відгуки за посиланням
- 5-зіркова система оцінювання
- Відгуки впливають на зарплату працівників

### 📊 Аналітика та звітність
- Дашборд менеджера з ключовими метриками
- Аналітика продажів з графіками (Chart.js)
- Топ страв та напоїв
- Статистика працівників
- Експорт графіку роботи в Excel

### 🪑 Управління столиками
- Візуальне відображення статусу столиків
- Автоматична зміна статусу при замовленні

## 🚀 Швидкий старт

### Автоматичне налаштування (1 команда!)

```bash
./setup_postgresql.sh
```

Цей скрипт:
- Створить БД PostgreSQL
- Налаштує користувача
- Створить всі таблиці
- Заповнить БД тестовими даними
- Готово до роботи! ✨

### Запуск програми

```bash
mvn spring-boot:run
```

Відкрийте в браузері: **http://localhost:8080/login**

### Логіни для входу (пароль для всіх: `123`)

| Роль | Логін | Пароль |
|------|-------|--------|
| Адміністратор | `admin` | `123` |
| Офіціант | `natalia_waiter` | `123` |
| Кухар | `ivan_cook` | `123` |

## 📦 Що включено в тестові дані

- 👥 **9 працівників** (2 адміни, 3 кухарі, 4 офіціанта)
- 🍽️ **34 страви** в 7 категоріях
- 📦 **30 інгредієнтів** з реалістичними залишками
- 📋 **25 замовлень** за останні 30 днів
- ⭐ **10 відгуків клієнтів**
- 🪑 **15 столиків**
- 📅 **Графік роботи** на поточний тиждень

## 🎨 Сучасний UI/UX

### Покращення інтерфейсу
- ✅ Єдина система стилів з градієнтами
- ✅ Візуальне створення замовлень (картки страв)
- ✅ Інтерактивні графіки (Chart.js)
- ✅ Пошук та фільтрація в реальному часі
- ✅ Адаптивний дизайн (mobile-friendly)
- ✅ Анімації та hover-ефекти

Детальніше: **[UI_IMPROVEMENTS.md](UI_IMPROVEMENTS.md)**

## 🛠️ Технології

- **Backend:** Spring Boot 3.1.3, Java 17
- **Database:** PostgreSQL
- **Security:** Spring Security, BCrypt
- **ORM:** Spring Data JPA, Hibernate
- **Frontend:** Thymeleaf, Bootstrap 4, jQuery
- **Charts:** Chart.js
- **Export:** Apache POI (Excel)

## 📁 Структура проекту

```
src/main/java/com/example/demo/
├── controllers/          # REST контролери
├── models/              # Entity класи
├── repositories/        # JPA репозиторії
├── services/           # Бізнес-логіка
├── dto/                # Data Transfer Objects
└── SecurityConfig.java # Конфігурація безпеки

src/main/resources/
├── templates/          # Thymeleaf HTML
├── static/css/        # Стилі
├── application.properties
├── init_shifts.sql    # Ініціалізація змін
└── init_salary.sql    # Ініціалізація зарплат

populate_database.sql   # Тестові дані
setup_postgresql.sh     # Автоматичне налаштування
```

## 🔧 Ручне налаштування

Якщо автоматичний скрипт не спрацював:

### 1. Встановіть PostgreSQL

```bash
# macOS
brew install postgresql@15
brew services start postgresql@15

# Ubuntu
sudo apt install postgresql postgresql-contrib
sudo systemctl start postgresql
```

### 2. Створіть БД

```bash
psql postgres
```

```sql
-- Користувач postgres вже існує, просто створюємо БД
CREATE DATABASE diplom_master OWNER postgres;
GRANT ALL PRIVILEGES ON DATABASE diplom_master TO postgres;
\q
```

### 3. Запустіть програму для створення таблиць

```bash
mvn clean install
mvn spring-boot:run
```

Зачекайте 10 секунд, потім зупиніть (Ctrl+C).

### 4. Заповніть тестовими даними

```bash
./load_test_data.sh
# або
psql -U postgres -d diplom_master -f populate_database.sql
```

### 5. Запустіть знову

```bash
mvn spring-boot:run
```

## 📊 Основні функції

### Для всіх ролей:
- 📋 Перегляд та створення замовлень
- 🍽️ Перегляд меню
- 🪑 Перегляд статусу столиків
- 📂 Перегляд категорій

### Тільки для адміністраторів:
- 💰 Розрахунок зарплат
- ⭐ Перегляд відгуків клієнтів
- 📅 Управління графіком роботи
- 📊 Дашборд з аналітикою
- 📦 Управління інвентарем
- 📈 Аналітика продажів
- ⚙️ Налаштування системи

## ❓ Часті питання

### Як змінити порт?
В `application.properties`:
```properties
server.port=8081
```

### Як змінити пароль БД?
1. В PostgreSQL: `ALTER USER postgres WITH PASSWORD 'новий_пароль';`
2. В `application.properties`: `spring.datasource.password=новий_пароль`

### Як очистити всі дані?
```bash
psql -U postgres -d diplom_master
```
```sql
TRUNCATE TABLE order_items, orders, reviews, ingredient_items, menu_items, 
              ingredients, categories, work_schedule, employees 
RESTART IDENTITY CASCADE;
```

### Як додати нові дані?
```bash
./load_test_data.sh
```

## 🐛 Troubleshooting

### Порт 8080 зайнятий
```bash
# Знайти процес
lsof -i :8080
# Вбити процес
kill -9 <PID>
```

### PostgreSQL не запускається
```bash
# macOS
brew services restart postgresql@15

# Linux
sudo systemctl restart postgresql
```

### Maven помилки
```bash
mvn clean install -U
```

## 📝 Розробка

### Додавання нової Entity
1. Створіть клас в `models/`
2. Створіть Repository в `repositories/`
3. Створіть Service в `services/`
4. Створіть Controller в `controllers/`
5. Створіть HTML template в `templates/`

### Додавання нового запиту
```java
@Query("SELECT ... FROM Entity e WHERE ...")
List<DTO> customQuery(Long param);
```

## 🎯 Особливості

### Автоматизація
- ✅ Автоматичне списання інгредієнтів
- ✅ Автоматична зміна статусу столиків
- ✅ Автоматична генерація графіку
- ✅ Автоматичний розрахунок зарплат

### Безпека
- ✅ BCrypt хешування паролів
- ✅ Spring Security авторизація
- ✅ Розмежування прав доступу
- ✅ CSRF захист

### Продуктивність
- ✅ JPQL запити (оптимізовано)
- ✅ Connection pooling (HikariCP)
- ✅ Lazy loading для зв'язків
- ✅ Індекси в БД

## 🚀 Production Deploy

### 1. Змініть application.properties
```properties
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
```

### 2. Створіть JAR
```bash
mvn clean package -DskipTests
```

### 3. Запустіть
```bash
java -jar target/demo2-0.0.1-SNAPSHOT.jar
```

### 4. З власним портом
```bash
java -jar target/demo2-0.0.1-SNAPSHOT.jar --server.port=8081
```

## 📄 Ліцензія

Навчальний проект.

## 👨‍💻 Автор

Розроблено як дипломний проект для системи управління командною взаємодію в ресторані.

## 🎉 Приємного користування!

Якщо виникли питання - перевірте документацію або створіть Issue.

---

**Корисні команди:**
```bash
./setup_postgresql.sh          # Повне налаштування
./load_test_data.sh           # Тільки завантаження даних
mvn spring-boot:run           # Запуск програми
mvn clean install             # Оновлення залежностей
psql -U postgres -d diplom_master # Підключення до БД
```

**URL:**
- Вхід: http://localhost:8080/login


