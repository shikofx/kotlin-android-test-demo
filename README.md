# kotlin-android-test-demo

[![GitHub Actions CI](https://github.com/d-parkheychuk/kotlin-android-test-demo/actions/workflows/ci.yml/badge.svg)](https://github.com/d-parkheychuk/kotlin-android-test-demo/actions/workflows/ci.yml)
[![GitLab CI](https://gitlab.com/d-parkheychuk/kotlin-android-test-demo/badges/develop/pipeline.svg)](https://gitlab.com/d-parkheychuk/kotlin-android-test-demo/-/pipelines)

Демонстрационный проект, показывающий эволюцию подходов к UI-тестированию в Android. Проект проходит путь от базовых тестов на **Espresso** до продвинутых сценариев с **Kaspresso**, включая рефакторинг и интеграцию с CI/CD.

## Цель проекта

Этот репозиторий — не просто пример кода, а практическое руководство и часть портфолио, демонстрирующая следующие навыки:
*   Владение современными фреймворками для UI-тестирования Android (Espresso, Kaspresso).
*   Применение паттерна Page Object для создания поддерживаемых тестов.
*   Практика безопасного рефакторинга приложения под защитой тестов.
*   Настройка CI/CD пайплайнов с нуля на **GitHub Actions** и **GitLab CI**.
*   Интеграция с системами отчетности (Allure Report).

## Эволюция проекта (Структура веток)

Проект развивается поэтапно, и каждый этап представлен в отдельной ветке. Это позволяет наглядно отследить прогресс и сравнить различные подходы.

| Ветка | Описание | Ключевые технологии |
| :--- | :--- | :--- |
| `feature/4-espresso-initial` | **Базовый уровень:** Реализованы первые UI-тесты с использованием нативного фреймворка **Espresso**. | `Espresso`, `JUnit4` |
| `feature/6-kaspresso` | **Стабильность и мощь:** Тесты мигрированы на фреймворк **Kaspresso** для повышения стабильности и работы со сложными сценариями. | `Kaspresso` |
| `feature/7-logic-refactoring` | **Рефакторинг бизнес-логики:** Исправление проблем, найденных статическим анализом, под защитой Unit-тестов. | `Java`, `Checkstyle`, `PMD` |
| `feature/10-kotlin-final-refactoring` | **Финальный рефакторинг:** Проведен масштабный рефакторинг исходного кода приложения (Java -> Kotlin, `findViewById` -> ViewBinding, Gradle Groovy -> Kotlin DSL) под защитой Kaspresso-тестов. | `Kotlin`, `ViewBinding`, `Gradle KTS` |
| `develop` / `main` | **Интеграция и релизы:** Содержат стабильные версии проекта. Все завершенные этапы сливаются в `develop`. | `Kaspresso`, `Allure Report`, `GitHub Actions`, `GitLab CI` |

## Технологический стек

*   **Язык:** Kotlin
*   **Тестирование:**
    *   Espresso
    *   Kakao
    *   Kaspresso
    *   JUnit4 / JUnit5
*   **CI/CD:** GitHub Actions, GitLab CI
*   **Отчетность:** Allure Report
*   **Статический анализ:** Checkstyle, PMD, Android Lint (для Java), Detekt, Ktlint (для Kotlin)
*   **Сборка:** Gradle KTS

## Как запустить тесты

1.  Клонируйте репозиторий:
    ```bash
    git clone https://github.com/d-parkheychuk/kotlin-android-test-demo.git
    cd kotlin-android-test-demo
    ```

2.  Переключитесь на интересующую вас ветку, например `main` для финальной версии или `feature/4-espresso-initial` для первого этапа:
    ```bash
    git checkout develop
    ```

3.  Запустите тесты из командной строки (требуется подключенное устройство или запущенный эмулятор):
    ```bash
    ./gradlew connectedCheck
    ```

## Отчеты о тестировании

После каждого прогона тестов в CI генерируется Allure-отчет. Для локальной генерации отчета выполните:
```bash
./gradlew allureReport
```
Отчет будет доступен в директории `app/build/reports/allure-report/index.html`.