#!/bin/bash
# ============================================
# Швидке заповнення БД тестовими даними
# Використання: ./load_test_data.sh
# ============================================

GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
CYAN='\033[0;36m'
NC='\033[0m'

echo -e "${CYAN}"
echo "╔═══════════════════════════════════════╗"
echo "║  Завантаження тестових даних в БД     ║"
echo "╚═══════════════════════════════════════╝"
echo -e "${NC}"

# Перевірка файлу
if [ ! -f "populate_database.sql" ]; then
    echo -e "${RED}[✗] Файл populate_database.sql не знайдено!${NC}"
    exit 1
fi

# Заповнення даними
echo -e "${YELLOW}[!] Завантаження даних...${NC}"
psql -U postgres -d diplom_master -f populate_database.sql

if [ $? -eq 0 ]; then
    echo ""
    echo -e "${GREEN}[✓] Дані успішно завантажені!${NC}"
    echo ""
    echo -e "${CYAN}🔐 Логіни для входу:${NC}"
    echo -e "  ${GREEN}Адміністратор:${NC} admin / 123"
    echo -e "  ${GREEN}Офіціант:${NC} natalia_waiter / 123"
    echo -e "  ${GREEN}Кухар:${NC} ivan_cook / 123"
    echo ""
    echo -e "${CYAN}🚀 Запустіть програму:${NC}"
    echo -e "  ${YELLOW}mvn spring-boot:run${NC}"
else
    echo -e "${RED}[✗] Помилка завантаження даних!${NC}"
    exit 1
fi

