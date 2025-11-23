#!/bin/bash
# ============================================
# Повна автоматизація налаштування PostgreSQL
# Використання: chmod +x setup_postgresql.sh && ./setup_postgresql.sh
# ============================================

set -e  # Зупинити виконання при помилці

# Кольори
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
PURPLE='\033[0;35m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

echo -e "${PURPLE}"
cat << "EOF"
╔═══════════════════════════════════════╗
║   🍽️  РЕСТОРАН - PostgreSQL Setup  🍽️   ║
╚═══════════════════════════════════════╝
EOF
echo -e "${NC}"

# Функція для виводу повідомлень
log_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

log_success() {
    echo -e "${GREEN}[✓]${NC} $1"
}

log_warning() {
    echo -e "${YELLOW}[!]${NC} $1"
}

log_error() {
    echo -e "${RED}[✗]${NC} $1"
}

# Перевірка PostgreSQL
log_info "Перевірка PostgreSQL..."
if ! command -v psql &> /dev/null; then
    log_error "PostgreSQL не встановлено!"
    echo ""
    echo "Встановіть PostgreSQL:"
    echo "  macOS:   brew install postgresql@15"
    echo "  Ubuntu:  sudo apt install postgresql postgresql-contrib"
    exit 1
fi
log_success "PostgreSQL встановлено"

# Перевірка чи запущений PostgreSQL
log_info "Перевірка сервісу PostgreSQL..."
if ! pg_isready -q; then
    log_warning "PostgreSQL не запущено. Спроба запустити..."
    
    # macOS
    if [[ "$OSTYPE" == "darwin"* ]]; then
        brew services start postgresql@15 2>/dev/null || brew services start postgresql 2>/dev/null
    # Linux
    else
        sudo systemctl start postgresql 2>/dev/null || sudo service postgresql start 2>/dev/null
    fi
    
    sleep 2
    if pg_isready -q; then
        log_success "PostgreSQL запущено"
    else
        log_error "Не вдалося запустити PostgreSQL"
        exit 1
    fi
else
    log_success "PostgreSQL працює"
fi

# Створення БД та користувача
log_info "Створення бази даних..."

# Визначення команди psql для різних ОС
if [[ "$OSTYPE" == "darwin"* ]]; then
    PSQL_CMD="psql postgres"
else
    PSQL_CMD="sudo -u postgres psql"
fi

# Створення БД (користувач postgres вже існує)
$PSQL_CMD << EOF 2>/dev/null || true
-- Видалення існуючої БД якщо потрібно
-- DROP DATABASE IF EXISTS diplom_master;

-- Створення БД
CREATE DATABASE diplom_master OWNER postgres;

-- Надання прав
GRANT ALL PRIVILEGES ON DATABASE diplom_master TO postgres;
EOF

log_success "База даних створена"

# Надання прав на схему
log_info "Налаштування прав доступу..."
$PSQL_CMD -d diplom_master << EOF 2>/dev/null || true
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON ALL TABLES IN SCHEMA public TO postgres;
GRANT ALL ON ALL SEQUENCES IN SCHEMA public TO postgres;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO postgres;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO postgres;
EOF

log_success "Права налаштовано"

# Maven clean install
log_info "Оновлення Maven залежностей..."
if command -v mvn &> /dev/null; then
    mvn clean install -DskipTests > /dev/null 2>&1
    log_success "Maven залежності оновлено"
else
    log_warning "Maven не знайдено, пропускаємо..."
fi

# Запуск Spring Boot для створення таблиць
log_info "Запуск програми для створення структури БД..."
log_warning "Програма запуститься і автоматично зупиниться через 15 секунд..."

if command -v mvn &> /dev/null; then
    # Запускаємо у фоні
    mvn spring-boot:run > /dev/null 2>&1 &
    SPRING_PID=$!
    
    # Чекаємо 15 секунд
    for i in {15..1}; do
        echo -ne "\r${YELLOW}Очікування: $i секунд...${NC}"
        sleep 1
    done
    echo ""
    
    # Зупиняємо Spring Boot
    kill $SPRING_PID 2>/dev/null || true
    wait $SPRING_PID 2>/dev/null || true
    log_success "Таблиці створено"
else
    log_warning "Maven не знайдено. Запустіть вручну: mvn spring-boot:run"
    log_warning "Після запуску зачекайте 10 секунд і зупиніть (Ctrl+C)"
    read -p "Натисніть Enter після запуску і зупинки програми..."
fi

# Заповнення даними
log_info "Заповнення бази даними..."
if [ -f "populate_database.sql" ]; then
    psql -U postgres -d diplom_master -f populate_database.sql > /dev/null 2>&1
    log_success "Дані завантажено"
else
    log_error "Файл populate_database.sql не знайдено!"
    exit 1
fi

# Підсумок
echo ""
echo -e "${GREEN}"
cat << "EOF"
╔═══════════════════════════════════════╗
║           ✓ ВСЕ ГОТОВО! ✓             ║
╚═══════════════════════════════════════╝
EOF
echo -e "${NC}"

echo -e "${CYAN}📊 Статистика:${NC}"
psql -U postgres -d diplom_master -t << EOF
SELECT '  • Працівників: ' || COUNT(*) FROM employees;
SELECT '  • Страв в меню: ' || COUNT(*) FROM menu_items;
SELECT '  • Інгредієнтів: ' || COUNT(*) FROM ingredients;
SELECT '  • Замовлень: ' || COUNT(*) FROM orders;
SELECT '  • Відгуків: ' || COUNT(*) FROM reviews;
SELECT '  • Столиків: ' || COUNT(*) FROM tables;
EOF

echo ""
echo -e "${CYAN}🔐 Логіни для входу в систему:${NC}"
echo -e "  ${GREEN}Адміністратор:${NC} admin / 123"
echo -e "  ${GREEN}Офіціант:${NC} natalia_waiter / 123"
echo -e "  ${GREEN}Кухар:${NC} ivan_cook / 123"
echo ""
echo -e "${CYAN}📊 База даних:${NC}"
echo -e "  ${YELLOW}Host:${NC} localhost:5432"
echo -e "  ${YELLOW}Database:${NC} diplom_master"
echo -e "  ${YELLOW}User:${NC} postgres"
echo -e "  ${YELLOW}Password:${NC} postgres"
echo ""
echo -e "${CYAN}🚀 Запуск програми:${NC}"
echo -e "  ${YELLOW}mvn spring-boot:run${NC}"
echo ""
echo -e "${CYAN}🌐 Відкрити в браузері:${NC}"
echo -e "  ${YELLOW}http://localhost:8080/login${NC}"
echo ""
log_success "Налаштування завершено успішно!"

