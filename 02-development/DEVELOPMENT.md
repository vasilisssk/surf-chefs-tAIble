## Использованные инструменты: Android Studio | Codex

## Промпты:
1. [src](app/src/) .gitignore [build.gradle.kts](app/build.gradle.kts) [proguard-rules.pro](app/proguard-rules.pro)  [conclusion](01-analysis/conclusion/) [er-model-mermaid-final.md](01-analysis/er-model/er-model-mermaid-final.md) [AndroidManifest.xml](app/src/main/AndroidManifest.xml) [build.gradle.kts](build.gradle.kts) [settings.gradle.kts](settings.gradle.kts) .gitignore [02-development](02-development/) можешь проанализировать ТЗ и описать доменные сущности
2. по сути бэкенда нет, его нужно реализовать. давай для начала продумаем архитектуру и инструменты. я работал с Java и Spring Boot, так что я смогу валидировать написанный код. по архитектуре, вроде, spring boot app использует model-view-controller. так же нужно будет подлючить docker. что скажешь по плану?
3. отличный стек, давай теперь перейдем к frontend, здесь я думаю все просто: kotlin + mvvm. насчет backend обязательно добавить логирование для сетевых вызовов, это очень важно для отладки
4. для DI давай использовать Koin - я с ним работал. насчет верстки здесь [layout](app/src/main/res/layout/) лежат уже готовые фрагметны и активность, над ними нужно будет поработать и добавить парочку новых активностей и фрагментов, так что xml оставляем. можешь в папке [backend](02-development/backend/) написать md-файл check list с ограничениями из тз для backend части приложения, то же самое в папке @02-development/frontend/ но уже для frontend
5. замечательно, забыл добавить, что не нужно писать классы на 1000 строк кода, каждый entity - отдельный класс, это касается и frontend (viewmodel, entity, dto ) и backend (сервисы, контроллеры), обязательно добавить это в оба checklist
6. /init
7. можешь приступать к реализации backend, обязательно отметь в чек-листе, что получилось реализовать [backend](C:/Users/Vasilisssk/AndroidStudioProjects/ChefsTable/02-development/backend/) , разбей все по соответсвующим директориям
# Закончились бесплатные промпты в Codex'е, так что перешел в Qoder
8. [backend](02-development/backend) у меня есть backend как отдельный проект в AndroidStudio, там есть docker-compose.yml и Dockerfile, что мне нужно сделать, чтобы пользоваться этим backend?
9. <код ошибки> ошибка при валидации схемы Hibernate, можешь исправить, что необходимо. И как мне заново пересобрать Image и контейнеры если нужно