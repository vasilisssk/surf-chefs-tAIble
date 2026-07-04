# Финальная ER-модель в нотации Mermaid

## Основная диаграмма

```mermaid
---
config:
  layout: elk
---
erDiagram
    CLIENT {
        string client_id PK
        string first_name
        string last_name
        string email
        string phone
        datetime registration_date
        int total_classes_attended
        string allergies
        boolean is_permanent_client
    }
    
    CHEF {
        string chef_id PK
        string name
        string specialization
        float rating
        int total_reviews
        string bio
    }
    
    COOKING_CLASS {
        string class_id PK
        string title
        string description
        datetime date_time
        int duration
        int max_participants
        int available_seats
        string chef_id FK
        string class_type
        decimal price
    }
    
    BOOKING {
        string booking_id PK
        string client_id FK
        string class_id FK
        datetime booking_date
        string status
        string rental_package_id FK
        datetime cancellation_date
        int penalty_points
    }
    
    RENTAL_EQUIPMENT {
        string rental_id PK
        string package_name
        string description
        decimal price
        int available_count
    }
    
    REVIEW {
        string review_id PK
        string client_id FK
        string chef_id FK
        int rating
        datetime review_date
        string comment
    }

    %% Relationships
    CLIENT ||--o{ BOOKING : creates
    COOKING_CLASS ||--o{ BOOKING : has
    CHEF ||--o{ COOKING_CLASS : leads
    CLIENT ||--o{ REVIEW : leaves
    CHEF ||--o{ REVIEW : receives
    RENTAL_EQUIPMENT ||--o{ BOOKING : assigned_to
```

## Описание изменений

### 1. Удаление связи между REVIEW и COOKING_CLASS
- **Проблема:** Ранее была добавлена связь REVIEW с COOKING_CLASS, что не соответствует брифу
- **Исправление:** Удалена связь REVIEW --|| COOKING_CLASS, так как по брифу оценки предназначены только шефам, а не классам

### 2. Упрощение сущности REVIEW
- **Проблема:** Ранее в REVIEW был атрибут class_id, что подразумевало оценку класса
- **Исправление:** Удален атрибут class_id из сущности REVIEW, оставлены только связи с клиентом и шефом

### 3. Добавление комментария к отзыву
- **Изменение:** Добавлен атрибут comment в сущность REVIEW, так как в брифе упоминается возможность оставлять отзывы

## Исправленное описание отношений:

- CLIENT --(создает)--> BOOKING (One-to-Many)
- COOKING_CLASS --(имеет)--> BOOKING (One-to-Many)
- CHEF --(ведет)--> COOKING_CLASS (One-to-Many)
- CLIENT --(оставляет)--> REVIEW (One-to-Many)
- CHEF --(получает)--> REVIEW (One-to-Many)
- RENTAL_EQUIPMENT --(назначается)--> BOOKING (One-to-Many)

## Классификация сущностей по типу доступа:

### Read-only сущности:
- Chef: информация о шефах поступает из существующего бэкенда, клиентское приложение только отображает
- CookingClass: расписание и информация о классах поступают из бэкенда, клиент не может их создавать или редактировать
- RentalEquipment: информация о доступном оборудовании для проката поступает из бэкенда

### Read/Write сущности:
- Client: клиент может регистрироваться, обновлять свой профиль
- Booking: клиент может создавать и отменять бронирования
- Review: клиент может создавать отзывы о шефах (один раз на посещенный класс)

### Write-once сущности:
- Review: после создания отзыв не может быть изменен или удален клиентом

## Соответствие брифу

Эта модель теперь точно соответствует брифу, где указано:
- "после класса клиент мог поставить оценку шефу"
- Оценки предназначены исключительно для шефов, а не для классов
- Цель - "видеть, кто как заходит людям" (о шефах, а не о классах)