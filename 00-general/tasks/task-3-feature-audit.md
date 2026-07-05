# Аудит реализованных функций (пункт 3)

Сверка чек-листов из `02-development/frontend/check-list.md` и `02-development/backend/check-list.md` с фактически реализованным кодом в `app/src/main/` и `02-development/backend/src/main/`.

---

## Реализованные функции

### Frontend (Android-приложение)

**Ф1** — Экран регистрации (`RegistrationActivity`, `RegistrationViewModel`, `activity_registration.xml`) — реализован с валидацией email, телефона (маска +7), пароля; подсветка ошибок ввода; отправка запроса на backend.

**Ф2** — Экран входа (`LoginActivity`, `LoginViewModel`, `activity_login.xml`) — реализован с входом по email/телефону и паролю; обработка ошибок сети и сервера.

**Ф3** — Главный экран со списком ближайших классов (`HomeFragment`, `HomeViewModel`, `fragment_home.xml`) — загрузка классов на ближайшие 7 дней, отображение ближайшего бронирования, переход к «Мои бронирования».

**Ф4** — Экран расписания с фильтром по шефу (`ScheduleFragment`, `ScheduleViewModel`, `fragment_schedule.xml`) — загрузка всех классов, фильтр по шефу через выпадающий список, сброс фильтров.

**Ф5** — Экран деталей класса (`ClassDetailFragment`, `ClassDetailViewModel`, `fragment_class_detail.xml`) — отображение шефа, программы, времени, длительности, цены, свободных мест; переход к бронированию.

**Ф6** — Экран подтверждения бронирования (`BookingConfirmationFragment`, `BookingViewModel`, `fragment_booking.xml`) — создание бронирования, выбор пакета проката (Basic/Standard/Premium/«Со своим»), указание аллергий, загрузка описания пакетов проката из БД.

**Ф7** — Экран «Мои бронирования» (`MyBookingsFragment`, `MyBookingsViewModel`, `fragment_my_bookings.xml`) — список бронирований с фильтрами (все/предстоящие/прошедшие/отменённые), отмена бронирования с правилом 24 часа, кнопка для написания отзыва.

**Ф8** — Экран профиля (`ProfileFragment`, `ProfileViewModel`, `fragment_profile.xml`) — отображение и редактирование имени, фамилии, email, телефона, аллергий; обновление данных на backend.

**Ф9** — Экран уведомлений (`NotificationsFragment`, `NotificationsViewModel`, `fragment_notifications.xml`) — список уведомлений с адаптером.

**Ф10** — Диалог отзыва о шефе (`ReviewDialogFragment`, `ReviewViewModel`, `dialog_review.xml`) — оценка 1–5, комментарий; отзыв только о шефе, нельзя изменить после сохранения.

**Ф11** — Навигация через Navigation Component (`nav_graph.xml`) — переходы между фрагментами, BottomNavigationView, защита авторизованных экранов через `MainActivity` (проверка токена).

**Ф12** — Безопасное хранение токена (`SessionManager`) — `EncryptedSharedPreferences` с AES256_GCM для access и refresh токенов; автоматическое добавление токена в запросы через `AuthInterceptor`; обновление токена через `TokenAuthenticator`.

**Ф13** — Сетевой слой (Retrofit + OkHttp) — `ApiService` с эндпоинтами для авторизации, классов, бронирований, профиля, отзывов, проката; `NetworkModule` с настройкой Koin; логирование запросов; централизованная обработка ошибок.

**Ф14** — Dependency Injection через Koin (`AppModule`) — регистрация всех ViewModel, Repository, SessionManager, сетевых компонентов.

**Ф15** — Валидация ввода (`Validator`) — валидация email, телефона (+7 формат), пароля; `PhoneMaskWatcher` для маски ввода номера.

**Ф16** — Состояния UI (`UiState`) — sealed-класс для паттерна Loading/Success/Empty/Error; обработка пустых состояний и ошибок сети на всех экранах.

**Ф17** — Адаптеры для списков (`ClassAdapter`, `BookingAdapter`, `NotificationAdapter`) — карточки классов, бронирований, уведомлений с корректным отображением и исправленным наложением на BottomNavigationView.

---

### Backend (Spring Boot REST API)

**Ф18** — Регистрация клиента (`AuthController`, `AuthService`) — по имени, email, телефону, парою; хеширование пароля; выдача JWT access + refresh токенов.

**Ф19** — Вход клиента (`AuthController`, `AuthService`) — по email/телефону и паролю; выдача JWT; refresh token endpoint.

**Ф20** — JWT-аутентификация (`JwtAuthenticationFilter`, `JwtService`, `SecurityConfig`) — фильтрация запросов, проверка валидности токена, защита эндпоинтов.

**Ф21** — API классов (`CookingClassController`, `CookingClassService`) — получение списка классов с фильтрацией по шефу, детали класса; только доступные классы.

**Ф22** — API шефов (`ChefController`, `ChefService`) — получение списка шефов для фильтра.

**Ф23** — Бронирование с бизнес-правилами (`BookingService`) — проверка свободных мест (`reserveSeat`), защита от двойного бронирования (`existsByClientIdAndCookingClassId`), запрет бронирования отменённого студией класса (`CookingClassStatus.CANCELLED`).

**Ф24** — Отмена бронирования (`BookingService.updateBooking`) — отмена клиентом с проверкой правила 24 часов; штрафные баллы при поздней отмене (`cancelByClient`); освобождение места (`releaseSeat`).

**Ф25** — Отмена класса студией (`BookingService.cancelClassByStudio`) — перевод всех подтверждённых бронирований в статус «отменено студией»; перевод класса в статус `CANCELLED`.

**Ф26** — Запрет повторной записи на отменённый студией слот — проверка `existsByClientIdAndCookingClassId` + статус класса `CANCELLED` блокируют повторное бронирование.

**Ф27** — API профиля (`ProfileController`, `ProfileService`) — получение и обновление профиля (имя, фамилия, email, телефон, аллергии).

**Ф28** — API отзывов (`ReviewController`, `ReviewService`) — создание отзыва о шефе (не о классе); проверка диапазона оценки 1–5; запрет изменения отзыва (`existsByBookingId`); привязка к бронированию.

**Ф29** — API прокатных пакетов (`RentalPackageController`, `RentalPackageService`) — пакеты Basic, Standard, Premium, «Со своим»; стоимость учитывается в бронировании.

**Ф30** — База данных PostgreSQL + Flyway миграции (`V1__init_schema.sql`, `V2__refresh_tokens_new_rental_package.sql`) — сущности: Client, Chef, CookingClass, Booking, RentalPackage, Review; все связи отражены.

**Ф31** — Логирование HTTP-запросов (`RequestLoggingFilter`) — логирование метода, URI, статуса, времени выполнения; маскирование паролей и токенов.

**Ф32** — Docker и docker-compose (`Dockerfile`, `docker-compose.yml`) — воспроизводимый локальный запуск backend + PostgreSQL.

**Ф33** — Обработка ошибок (`GlobalExceptionHandler`, `AppException` и наследники) — единый формат ответа об ошибках (`ErrorResponse`); понятные сообщения для клиента.

---

## Что НЕ реализовано (отмечено в чек-листах как `[ ]`)

| № | Функция | Источник | Причина / Статус |
|---|---------|----------|------------------|
| 1 | Проверить навигационные сценарии UI-тестами | frontend check-list | Не покрыто тестами |
| 2 | Постоянный клиент по количеству посещённых классов | backend check-list | Не реализовано |
| 3 | Push-уведомления и SMS | backend check-list | Не реализовано (нет интеграции с FCM/SMS-шлюзом) |
| 4 | Напоминания за 24ч и за 2ч до класса | backend check-list | Не реализовано |
| 5 | Мгновенное уведомление при отмене класса студией | backend check-list | Не реализовано |
| 6 | Интеграционные тесты критичных сценариев | backend check-list | Не покрыто тестами |
| 7 | Отдельные тесты бронирования, отмены и отзыва | backend check-list | Не покрыто тестами |
