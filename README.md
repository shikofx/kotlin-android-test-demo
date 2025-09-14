# kotlin-android-test-demo

[![Build and Test](https://github.com/d-parkheychuk/kotlin-android-test-demo/actions/workflows/ci.yml/badge.svg)](https://github.com/d-parkheychuk/kotlin-android-test-demo/actions/workflows/ci.yml)

Демонстрационный проект, показывающий эволюцию подходов к UI-тестированию в Android. Проект проходит путь от базовых тестов на **Espresso** до продвинутых сценариев с **Kaspresso**, включая рефакторинг и интеграцию с CI/CD.

## Цель проекта

Этот репозиторий — не просто пример кода, а практическое руководство и часть портфолио, демонстрирующая следующие навыки:
*   Владение различными фреймворками для UI-тестирования Android (Espresso, Kakao, Kaspresso).
*   Применение паттерна Page Object для создания поддерживаемых тестов.
*   Практика безопасного рефакторинга приложения под защитой тестов.
*   Настройка CI/CD пайплайнов с нуля на GitHub Actions.
*   Интеграция с системами отчетности (Allure Report).

## Эволюция проекта (Структура веток)

Проект развивается поэтапно, и каждый этап представлен в отдельной ветке. Это позволяет наглядно отследить прогресс и сравнить различные подходы.

| Ветка | Описание | Ключевые технологии |
| :--- | :--- | :--- |
| `1-espresso-initial` | **Базовый уровень:** Реализованы первые UI-тесты с использованием нативного фреймворка **Espresso**. | `Espresso`, `JUnit4` |
| `2-kakao` | **Улучшение читаемости:** Тесты переписаны с использованием DSL-обертки **Kakao**, внедрен паттерн Page Object. | `Kakao`, `Page Object` |
| `3-kaspresso` | **Стабильность и мощь:** Тесты мигрированы на фреймворк **Kaspresso** для повышения стабильности и работы со сложными сценариями. | `Kaspresso` |
| `4-kotlin-conversion` | **Рефакторинг:** Проведен масштабный рефакторинг исходного кода приложения (Java -> Kotlin, `findViewById` -> ViewBinding, Gradle Groovy -> Kotlin DSL) под защитой Kaspresso-тестов. | `Kotlin`, `ViewBinding`, `Gradle KTS` |
| `main` | **Основная ветка:** Содержит последнюю стабильную версию проекта. Все завершенные этапы сливаются сюда. | `Kaspresso`, `Allure Report`, `GitHub Actions` |

## Технологический стек

*   **Язык:** Kotlin
*   **Тестирование:**
    *   Espresso
    *   Kakao
    *   Kaspresso
    *   JUnit4
*   **CI/CD:** GitHub Actions
*   **Отчетность:** Allure Report
*   **Статический анализ:** Detekt, Ktlint

## Как запустить тесты

1.  Клонируйте репозиторий:
    ```bash
    git clone https://github.com/d-parkheychuk/kotlin-android-test-demo.git
    cd kotlin-android-test-demo
    ```

2.  Переключитесь на интересующую вас ветку, например `main` для финальной версии или `1-espresso-initial` для первого этапа:
    ```bash
    git checkout main
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