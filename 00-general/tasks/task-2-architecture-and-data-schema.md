# Задача 2: Архитектурный план и схема данных

## Цель
Получить от ИИ архитектурный план приложения (структура экранов, навигация, слои) и схему данных (ER-модель) для клиентского мобильного Android-приложения кулинарной студии.

## Симптом / Проблема
После формализации требований (US/UC) необходимо спроектировать:
- ER-модель с описанием сущностей, атрибутов и отношений
- API-контракт (OpenAPI-спецификация) для интеграции с существующим бэкендом
- Архитектуру UI: экраны, навигацию, потоки взаимодействия
- Технические спецификации на каждый экран и бизнес-логику

## Входные данные
- Бизнес-требования: `01-analysis/requirements/business-requirements.md`
- Функциональные требования: `01-analysis/requirements/functional-requirements.md`
- User Stories: `01-analysis/requirements/user-stories.md`
- Use Cases: `01-analysis/requirements/use-cases.md`
- Уточнения скоупа из брифа: `01-analysis/brief/brief-cooking.md`

## Выполнение

### Шаг 1. ER-модель (текстовая)
Описаны 6 сущностей:
- **Client** (Read/Write) — клиент приложения
- **Chef** (Read-only) — шеф-инструктор
- **CookingClass** (Read-only) — кулинарный класс
- **Booking** (Read/Write) — бронирование
- **RentalEquipment** (Read-only) — прокатное оборудование
- **Review** (Write-once) — отзыв клиента

Описаны отношения: One-to-Many между Client→Booking, CookingClass→Booking, Chef→CookingClass, Client→Review, Chef→Review, Many-to-Many через Booking для RentalEquipment.

Результат: `01-analysis/er-model/er-model.md`

### Шаг 2. ER-модель (Mermaid-диаграмма)
Визуальная диаграмма в формате Mermaid с валидацией против текстовой версии.

Результат: `01-analysis/er-model/er-model-mermaid-final.md`

### Шаг 3. Валидация ER-модели
Проверена согласованность текстовой и Mermaid-версий, устранены расхождения (связь Review→CookingClass, поле rental_package).

Результат: `01-analysis/er-model/er-model-validation.md`

### Шаг 4. OpenAPI-спецификация
Сформирован контракт API для интеграции с бэкендом:
- `/auth/register`, `/auth/login` — аутентификация
- `/profile` — профиль клиента
- `/classes` — расписание классов
- `/bookings` — бронирования
- `/chefs` — шефы
- `/reviews` — отзывы
- `/rental-packages` — прокат оборудования

Результат: `01-analysis/api/openapi-spec-final.yaml`

### Шаг 5. Дизайн экранов (Screen Registry)
Определён реестр экранов приложения с учётом обратной связи по дублированию логики и навигационной структуре.

Результат: `01-analysis/design/screen-registry-revised.md`

### Шаг 6. Архитектурный план приложения
Определена итоговая архитектура:
- **Аутентификация** — отдельные Activity (Registration, Login)
- **Основная навигация** — Bottom Navigation Activity с 4 вкладками:
  - Home (главная)
  - Schedule (расписание)
  - My Bookings (мои бронирования)
  - Profile (профиль)
- **Дополнительные экраны** — Class Detail, Booking (Bottom Sheet), Notifications

Результат: `01-analysis/conclusion/conclusion-overview.md`

### Шаг 7. Технические спецификации экранов
Составлены 9 спецификаций экранов (SCR-001 … SCR-009):
- SCR-001: Экран регистрации
- SCR-002: Экран входа
- SCR-003: Главный экран (Home)
- SCR-004: Экран расписания (Schedule)
- SCR-005: Экран деталей класса
- SCR-006: Экран бронирования
- SCR-007: Экран моих бронирований
- SCR-008: Экран профиля
- SCR-009: Экран уведомлений

Результаты: `01-analysis/conclusion/tech_specs/screen/*.md`

### Шаг 8. Технические спецификации бизнес-логики
Составлены 3 спецификации бизнес-логики (LOGIC-001 … LOGIC-003):
- LOGIC-001: Бизнес-логика аутентификации
- LOGIC-002: Бизнес-логика бронирования
- LOGIC-003: Бизнес-логика отзывов

Результаты: `01-analysis/conclusion/tech_specs/logic/*.md`

## Результат
Архитектурный план и схема данных полностью сформированы. ER-модель прошла валидацию, API-контракт готов к использованию, все экраны и бизнес-логика описаны в технических спецификациях.

## Связанные артефакты
| Файл | Описание |
|------|----------|
| `01-analysis/er-model/er-model.md` | ER-модель (текстовая) |
| `01-analysis/er-model/er-model-mermaid-final.md` | ER-модель (Mermaid) |
| `01-analysis/er-model/er-model-validation.md` | Валидация ER-модели |
| `01-analysis/api/openapi-spec-final.yaml` | OpenAPI-спецификация |
| `01-analysis/design/screen-registry-revised.md` | Реестр экранов |
| `01-analysis/conclusion/conclusion-overview.md` | Архитектурный обзор |
| `01-analysis/conclusion/specifications-summary.md` | Сводка спецификаций |
| `01-analysis/conclusion/tech_specs/screen/` | Спецификации экранов |
| `01-analysis/conclusion/tech_specs/logic/` | Спецификации бизнес-логики |
