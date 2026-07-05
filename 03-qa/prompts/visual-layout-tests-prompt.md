# Тесты визуальной компоновки экранов (UI Layout QA)

**Приложение:** Chefs tAIble — Android, Material Components, XML-разметка, BottomNavigationView (5 табов).
**Ориентация:** портретная. **Мин. разрешение:** 360×640 (hdpi).

### Размерная сетка

| Токен | Значение |
|-------|----------|
| `spacing_xs` | 4 dp |
| `spacing_sm` | 8 dp |
| `spacing_md` | 16 dp |
| `spacing_lg` | 24 dp |
| `spacing_xl` | 32 dp |
| `card_radius` | 12 dp |
| `card_elevation` | 2 dp |
| `text_title` | 24 sp |
| `text_subtitle` | 18 sp |
| `text_body` | 14 sp |
| `text_caption` | 12 sp |

---

## Таблица проверок

| Экран | Компонент | Планируемые layout-проверки | Приоритет |
|-------|-----------|-----------------------------|-----------|
| **Все экраны** | BottomNavigationView | Видна на 5 табах авторизованной части; иконки и подписи соответствуют `bottom_nav_menu.xml`; активный таб подсвечен | Высокий |
| **Все экраны** | Контент фрагментов | Не обрезается под BottomNavigationView и статус-баром | Высокий |
| **Все экраны** | Клавиатура | Не перекрывает активное текстовое поле (ScrollView / fillViewport) | Высокий |
| **Все экраны** | Тёмная тема | Все экраны читаемы, цвета корректны (`values-night/themes.xml`) | Средний |
| **Все экраны** | Локализация | Все строки из `strings.xml`, захардкоженных нет | Средний |
| **Все экраны** | ProgressBar | Отображается по центру экрана (`layout_gravity=center`), контент скрыт | Высокий |
| **Все экраны** | Состояние ошибки | Текст ошибки (center) + кнопка «Обновить» (`marginTop=spacing_md`) | Высокий |
| **Все экраны** | Пустое состояние | Информационный текст по центру (`gravity=center`), `visibility=gone` при наличии данных | Средний |
| **Логин** | Заголовок «Вход» | 24sp, bold, `primary`, `marginBottom=spacing_xl` | Высокий |
| **Логин** | TextInputLayout — Email | OutlinedBox, hint «Email», `inputType=textEmailAddress`, maxLength=100, `boxStrokeColor=selector_box_stroke` | Высокий |
| **Логин** | TextInputLayout — Пароль | OutlinedBox, hint «Пароль», `inputType=textPassword`, maxLength=100, `passwordToggleEnabled=true` | Высокий |
| **Логин** | Отступ Email → Пароль | `marginBottom=spacing_md` у tilEmail | Высокий |
| **Логин** | Отступ Пароль → Кнопка | `marginBottom=spacing_lg` у tilPassword | Высокий |
| **Логин** | MaterialButton «Войти» | `match_parent` по ширине, `marginBottom=spacing_lg` | Высокий |
| **Логин** | Ссылка «Регистрация» | `accent`, bold, `marginStart=spacing_sm` от текста «Нет аккаунта?» | Средний |
| **Логин** | ScrollView (маленький экран) | На 320×480 контент прокручивается, ничего не обрезается | Средний |
| **Логин** | `boxStrokeColor` при фокусе | Меняет цвет на `primary` (selector_box_stroke) | Низкий |
| **Регистрация** | Заголовок «Регистрация» | 24sp, bold, `primary`, `marginBottom=spacing_xl` | Высокий |
| **Регистрация** | TextInputLayout — Имя | `textPersonName`, maxLength=25, `marginBottom=spacing_md` | Высокий |
| **Регистрация** | TextInputLayout — Email | `textEmailAddress`, maxLength=100, `marginBottom=spacing_md` | Высокий |
| **Регистрация** | TextInputLayout — Телефон | `inputType=phone`, maxLength=18, `marginBottom=spacing_md` | Высокий |
| **Регистрация** | TextInputLayout — Пароль | `textPassword`, maxLength=100, password toggle, `marginBottom=spacing_lg` | Высокий |
| **Регистрация** | MaterialButton «Зарегистрироваться» | `match_parent`, `marginBottom=spacing_lg` | Высокий |
| **Регистрация** | Ссылка «Войти» | `accent`, bold, `marginStart=spacing_sm` от «Уже есть аккаунт?» | Средний |
| **Регистрация** | ScrollView (маленький экран) | 4 поля + кнопка + ссылка — прокручивается на 320×480 | Средний |
| **Главная** | Заголовок «Ближайшее бронирование» | 18sp, bold, `text_dark`, `paddingHorizontal=spacing_md`, `paddingTop=spacing_md` | Высокий |
| **Главная** | RecyclerView — бронирования (горизонтальный) | `clipToPadding=false`, `paddingVertical=spacing_sm`, `layoutManager=LinearLayoutManager` (horizontal) | Высокий |
| **Главная** | TextView «Нет предстоящих бронирований» | `textAlignment=center`, `text_light`, `visibility=gone` при наличии данных | Средний |
| **Главная** | Заголовок «Рекомендуемые классы» | 18sp, bold, `text_dark`, `paddingHorizontal=spacing_md`, `paddingTop=spacing_md` | Высокий |
| **Главная** | RecyclerView — классы (вертикальный) | `nestedScrollingEnabled=false`, `paddingVertical=spacing_sm`, `paddingBottom=spacing_lg` | Высокий |
| **Главная** | TextView «Нет доступных классов» | `textAlignment=center`, `text_light`, `visibility=gone` при наличии данных | Средний |
| **Главная** | SwipeRefreshLayout | Pull-to-refresh оборачивает NestedScrollView | Высокий |
| **Главная** | ProgressBar | `layout_gravity=center`, `visibility=gone` при загрузке данных | Средний |
| **Расписание** | Панель фильтров (horizontal) | `paddingVertical=spacing_sm`, `paddingHorizontal=spacing_md`, `gravity=center` | Высокий |
| **Расписание** | MaterialAutoCompleteTextView — фильтр шефа | `weight=1`, `inputType=none`, `completionThreshold=0`, `focusable=true`, `focusableInTouchMode=true`, `paddingHorizontal=16dp`, `paddingVertical=12dp` | Высокий |
| **Расписание** | MaterialButton «Сбросить» | TextButton, `accent`, `padding=0dp`, `layout_width=wrap_content` | Высокий |
| **Расписание** | RecyclerView — классы | `paddingBottom=spacing_lg`, `clipToPadding=false` | Высокий |
| **Расписание** | SwipeRefreshLayout | Pull-to-refresh работает | Высокий |
| **Расписание** | Пустое состояние | `gravity=center`, `visibility=gone` при наличии данных | Средний |
| **Мои брони** | ChipGroup — фильтры | `singleSelection=true`, `paddingHorizontal=spacing_md`, `paddingVertical=spacing_sm` | Высокий |
| **Мои брони** | Chip «Все» | `checked=true` по умолчанию | Высокий |
| **Мои брони** | Chip «Предстоящие» / «Прошедшие» / «Отмененные» | Choice-стиль, горизонтальная прокрутка при переполнении | Средний |
| **Мои брони** | RecyclerView — бронирования | `paddingBottom=spacing_lg`, `clipToPadding=false` | Высокий |
| **Мои брони** | SwipeRefreshLayout | Pull-to-refresh работает | Высокий |
| **Профиль** | Заголовок «Профиль» | 24sp, bold, `text_dark`, `marginBottom=spacing_md` | Высокий |
| **Профиль** | TextInputLayout — Имя | `textPersonName`, `boxStrokeColor=primary` | Высокий |
| **Профиль** | TextInputLayout — Фамилия | `textPersonName`, `marginTop=spacing_sm` | Высокий |
| **Профиль** | TextInputLayout — Email | `textEmailAddress`, `enabled=false` (визуально заблокировано, серый текст) | Высокий |
| **Профиль** | TextInputLayout — Телефон | `inputType=phone`, `marginTop=spacing_sm` | Высокий |
| **Профиль** | TextInputLayout — Аллергии | `textMultiLine`, `minLines=2`, `gravity=top`, `marginTop=spacing_sm` | Средний |
| **Профиль** | TextView «Постоянный клиент» | `text_light`, `marginTop=spacing_md`, 14sp | Средний |
| **Профиль** | TextView «Посещено классов» | `text_light`, `marginTop=spacing_xs`, 14sp | Средний |
| **Профиль** | MaterialButton «Сохранить» | `match_parent`, `marginTop=spacing_lg` | Высокий |
| **Профиль** | MaterialButton «Выйти из аккаунта» | OutlinedButton, `match_parent`, `marginTop=spacing_sm`, `textColor=primary` | Высокий |
| **Профиль** | NestedScrollView (`scroll_content`) | `visibility=gone` до загрузки, `visible` после | Высокий |
| **Уведомления** | ChipGroup — фильтры | `singleSelection=true`, `layout_weight=1` | Высокий |
| **Уведомления** | Chip «Все» | `checked=true` по умолчанию | Высокий |
| **Уведомления** | MaterialButton «Очистить уведомления» | TextButton, `primary`, `textSize=text_caption` (12sp), `layout_width=wrap_content` | Средний |
| **Уведомления** | RecyclerView — уведомления | `paddingBottom=spacing_lg`, `clipToPadding=false` | Высокий |
| **Уведомления** | Пустое состояние | `gravity=center`, «Нет уведомлений», без ProgressBar и layout_error | Средний |
| **Детали класса** | MaterialCardView — карточка класса | `cardCornerRadius=card_radius`, `cardElevation=card_elevation`, `padding=spacing_md` | Высокий |
| **Детали класса** | Название класса | 24sp, bold, `text_dark` | Высокий |
| **Детали класса** | Дата/время | 14sp, `text_light`, `marginTop=spacing_sm` | Высокий |
| **Детали класса** | Длительность | 12sp, `text_light`, `marginTop=spacing_xs` | Средний |
| **Детали класса** | Свободные места | 12sp, `status_confirmed`, `marginTop=spacing_xs` | Высокий |
| **Детали класса** | Описание | 14sp, `text_dark`, `marginTop=spacing_md` | Высокий |
| **Детали класса** | Цена | 18sp, bold, `primary`, `marginTop=spacing_md` | Высокий |
| **Детали класса** | MaterialCardView — карточка шефа | `marginTop=spacing_sm`, те же `cardCornerRadius` и `cardElevation`, что и карточка класса | Высокий |
| **Детали класса** | Заголовок «Отзывы о шефе» | 18sp, bold, `text_dark` | Высокий |
| **Детали класса** | Имя шефа | 14sp, `accent`, `marginTop=spacing_sm` | Высокий |
| **Детали класса** | Специализация шефа | 12sp, `text_light`, `marginTop=spacing_xs` | Средний |
| **Детали класса** | Рейтинг шефа | 12sp, `text_light`, `marginTop=spacing_xs` | Средний |
| **Детали класса** | Био шефа | 14sp, `text_dark`, `marginTop=spacing_sm` | Средний |
| **Детали класса** | MaterialButton «Забронировать» | `match_parent`, `marginTop=spacing_lg` | Высокий |
| **Детали класса** | Layout «не найдено» | Отдельный `LinearLayout`, `gravity=center`, текст `error_generic`, `visibility=gone` | Средний |
| **Подтверждение бронирования** | Заголовок «Подтверждение бронирования» | 24sp, bold, `text_dark`, `marginBottom=spacing_md` | Высокий |
| **Подтверждение бронирования** | MaterialCardView — карточка класса | Компактная: название (18sp, bold), дата (14sp, `text_light`), шеф (12sp, `accent`), цена (14sp, bold, `primary`) | Высокий |
| **Подтверждение бронирования** | Заголовок «Пакет проката» | 18sp, bold, `marginTop=spacing_lg`, `marginBottom=spacing_xs` | Высокий |
| **Подтверждение бронирования** | AutoCompleteTextView — пакет проката | `ExposedDropdownMenu` стиль, `inputType=none` | Высокий |
| **Подтверждение бронирования** | Описание пакета (TextView) | 12sp, `text_light`, `marginTop=spacing_xs`, `visibility=gone` до выбора | Средний |
| **Подтверждение бронирования** | Цена пакета (TextView) | 12sp, `text_light`, `marginTop=spacing_xs`, `visibility=gone` до выбора | Средний |
| **Подтверждение бронирования** | Заголовок «Аллергии» | 18sp, bold, `marginTop=spacing_lg`, `marginBottom=spacing_xs` | Высокий |
| **Подтверждение бронирования** | TextInputEditText — аллергии | `textMultiLine`, `minLines=2`, `gravity=top` | Средний |
| **Подтверждение бронирования** | Заголовок «Оплата» | 18sp, bold, `marginTop=spacing_lg`, `marginBottom=spacing_xs` | Средний |
| **Подтверждение бронирования** | «Оплата на месте» | 14sp, `text_light` | Средний |
| **Подтверждение бронирования** | Итого (TextView) | 18sp, bold, `primary`, `marginTop=spacing_lg` | Высокий |
| **Подтверждение бронирования** | MaterialButton «Подтвердить бронирование» | `match_parent`, `marginTop=spacing_lg` | Высокий |
| **Диалог отзыва** | Заголовок «Оценить шефа» | 18sp, bold, `text_dark`, `marginBottom=spacing_md` | Высокий |
| **Диалог отзыва** | TextView «Оценка» | 14sp, `text_dark`, `marginBottom=spacing_xs` | Высокий |
| **Диалог отзыва** | RatingBar | `numStars=5`, `stepSize=1`, `rating=5` по умолчанию | Высокий |
| **Диалог отзыва** | TextInputEditText — комментарий | `textMultiLine`, `minLines=3`, `gravity=top`, `marginTop=spacing_md` | Высокий |
| **Диалог отзыва** | ProgressBar | `layout_gravity=center`, `marginTop=spacing_md`, `visibility=gone` | Средний |
| **Диалог отзыва** | MaterialButton «Отправить отзыв» | `match_parent`, `marginTop=spacing_md` | Высокий |
| **Диалог отзыва** | Клавиатура | Диалог не перекрывается (корректный resize окна) | Средний |
| **item_booking** | MaterialCardView | `marginHorizontal=spacing_md`, `marginVertical=spacing_xs`, `cardCornerRadius=card_radius`, `cardElevation=card_elevation` | Высокий |
| **item_booking** | Название класса | 18sp, bold, `text_dark` | Высокий |
| **item_booking** | Дата | 12sp, `text_light`, `marginTop=spacing_xs` | Высокий |
| **item_booking** | Шеф | 12sp, `accent`, `marginTop=spacing_xs` | Высокий |
| **item_booking** | Статус-бейдж | 12sp, bold, `layout_width=wrap_content`, выровнен по правому краю | Высокий |
| **item_booking** | Панель действий | `gravity=center`, `marginTop=spacing_sm` | Высокий |
| **item_booking** | Кнопка «Отменить» | TextButton, `primary`, `padding=0dp`, `gone` по умолчанию | Средний |
| **item_booking** | Кнопка «Оценить шефа» | TextButton, `accent`, `minHeight=0dp`, `padding=0dp`, `visible` | Высокий |
| **item_cooking_class** | MaterialCardView | `marginHorizontal=spacing_md`, `marginVertical=spacing_xs`, `cardCornerRadius=card_radius`, `cardElevation=card_elevation` | Высокий |
| **item_cooking_class** | Название класса | 18sp, bold, `text_dark` | Высокий |
| **item_cooking_class** | Дата/время | 12sp, `text_light`, `marginTop=spacing_xs` | Высокий |
| **item_cooking_class** | Шеф | 14sp, `accent`, `marginTop=spacing_xs` | Высокий |
| **item_cooking_class** | Свободные места | 12sp, `text_light`, `weight=1` (занимает оставшееся пространство) | Высокий |
| **item_cooking_class** | Цена | 14sp, bold, `primary`, `wrap_content`, выровнена по правому краю | Высокий |
| **item_notification** | MaterialCardView | `marginHorizontal=spacing_md`, `marginVertical=spacing_xs`, `cardCornerRadius=card_radius`, `cardElevation=card_elevation` | Высокий |
| **item_notification** | Индикатор непрочитанного | `View`, 4dp шириной, `background=primary`, `visibility=gone` по умолчанию | Высокий |
| **item_notification** | Текст (контейнер) | `marginStart=spacing_sm`, `weight=1` | Высокий |
| **item_notification** | Заголовок | 14sp, bold, `text_dark` | Высокий |
| **item_notification** | Сообщение | 12sp, `text_light`, `marginTop=spacing_xs` | Средний |
| **item_notification** | Время | 12sp, `text_hint`, `marginTop=spacing_xs` | Средний |

---

## Кросс-экранные проверки

| Экран | Компонент | Планируемые layout-проверки | Приоритет |
|-------|-----------|-----------------------------|-----------|
| **Все (авторизованные)** | BottomNavigationView | Контент фрагментов не перекрывается панелью навигации | Высокий |
| **Все** | MaterialCardView | Единый стиль: `cardCornerRadius=12dp`, `cardElevation=2dp` на всех экранах | Высокий |
| **Все** | MaterialButton | Filled — на всех экранах; Outlined — только «Выйти из аккаунта» | Высокий |
| **Все** | TextInputLayout | OutlinedBox — все поля; ExposedDropdownMenu — только dropdown | Высокий |
| **Главная → Детали → Бронирование** | Навигация | Тап по карточке класса → детали → «Забронировать» → подтверждение: layout не ломается | Высокий |
| **Мои брони → Диалог отзыва** | Навигация | «Оценить шефа» → диалог: клавиатура не ломает layout | Высокий |
| **Все** | Системная кнопка «Назад» | Корректное восстановление стека, layout не ломается | Средний |
| **Все** | Поворот экрана | Состояние сохраняется, layout адаптируется | Средний |
| **Все** | Маленький экран (320×480) | Весь контент доступен через скролл | Средний |
| **Все** | Крупный шрифт (accessibility) | Текст не обрезается, layout адаптируется | Низкий |
