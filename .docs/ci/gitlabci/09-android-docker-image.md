# Урок 9: Создание Docker-образа для сборки Android

Чтобы GitLab CI мог собирать и тестировать Android-проект, ему нужно специальное окружение, в котором установлены Java (JDK) и Android SDK. Вместо того чтобы устанавливать все это на сервер с Runner'ом, мы создадим кастомный Docker-образ.

Это дает несколько преимуществ:
*   **Изоляция:** Зависимости для сборки не "засоряют" хост-систему.
*   **Воспроизводимость:** Сборка всегда происходит в идентичном, предсказуемом окружении, что исключает ошибки вида "у меня работает, а на сервере нет".
*   **Версионирование:** Вы можете легко переключаться между разными версиями JDK или SDK, просто меняя тег образа.

## Шаг 1: Создание `Dockerfile`

В корне нашего проекта находится файл `Dockerfile`, который описывает, как построить наш образ.

**Ключевые моменты в `Dockerfile`:**
1.  **`FROM ubuntu:22.04`**: Мы начинаем с чистого и стабильного базового образа Ubuntu.
2.  **`apt-get install -y openjdk-17-jdk ...`**: Устанавливаем JDK 17, так как проект настроен на эту версию Java.
3.  **`ENV ANDROID_SDK_ROOT...`**: Задаем стандартные переменные окружения, чтобы система знала, где находится Android SDK.
4.  **`wget ... && unzip ...`**: Скачиваем и распаковываем официальные `command-line tools` от Google.
5.  **`yes | sdkmanager --licenses`**: Автоматически принимаем все лицензионные соглашения Android SDK. Без этого шага сборка в CI будет падать.
6.  **`sdkmanager "platform-tools" "platforms;android-34" ...`**: С помощью `sdkmanager` устанавливаем необходимые компоненты: `platform-tools`, `build-tools` и нужную версию Android API.

## Шаг 2: Настройка CI/CD для сборки и публикации образа

GitLab предоставляет встроенный `Container Registry` — приватное хранилище для Docker-образов. Нам нужно создать задачу (job) в `.gitlab-ci.yml`, которая будет собирать образ по нашему `Dockerfile` и публиковать его в этот Registry.

### Важный аспект: Docker-out-of-Docker (DooD)

Наш GitLab Runner настроен по методу **Docker-out-of-Docker**. Это означает, что он имеет доступ к Docker-серверу хост-машины через проброшенный сокет (`/var/run/docker.sock`). Поэтому наша CI-задача **не требует** использования `services` и `docker:dind`. Она будет напрямую использовать Docker хоста.

> Подробнее о настройке Runner'а см. в Уроке 11: Установка и настройка GitLab Runner.

### Задача для `.gitlab-ci.yml`

Вот как выглядит задача, которая собирает и публикует наш образ. Ее можно временно добавить в `.gitlab-ci.yml` для первоначальной сборки.

```yaml
build_android_image:
  stage: build_builder_image
  # Используем официальный образ Docker, чтобы выполнять docker-команды
  image: docker:latest
  tags:
    - docker
  script:
    # 1. Логинимся в наш GitLab Container Registry (безопасный способ)
    - echo $CI_REGISTRY_PASSWORD | docker login -u $CI_REGISTRY_USER --password-stdin $CI_REGISTRY
    # 2. Собираем образ из Dockerfile в корне проекта
    - docker build -t $CI_REGISTRY_IMAGE/android-builder:latest .
    # 3. Публикуем собранный образ в Registry
    - docker push $CI_REGISTRY_IMAGE/android-builder:latest
  rules:
    # Запускать эту задачу только вручную из веб-интерфейса GitLab
    - if: $CI_PIPELINE_SOURCE == "web"
```

### Разбор задачи:
*   `image: docker:latest`: Задача будет выполняться в контейнере, где уже есть клиент `docker`.
*   `script`:
    *   `docker login ...`: Безопасная аутентификация в вашем GitLab Container Registry с использованием предопределенных переменных CI/CD.
    *   `docker build ...`: Собирает образ из `Dockerfile` в корне проекта и присваивает ему тег `$CI_REGISTRY_IMAGE/android-builder:latest`. Переменная `$CI_REGISTRY_IMAGE` автоматически подставляется GitLab и выглядит как `gitlab.your_domain.xyz:5050/username/project`.
    *   `docker push ...`: Загружает собранный образ в Registry.
*   `rules: - if: $CI_PIPELINE_SOURCE == "web"`: Это правило позволяет запускать задачу только вручную через интерфейс GitLab (**Build -> Pipelines -> Run pipeline**). Это удобно, так как сборка базового образа не требуется при каждом коммите.

## Шаг 3: Запуск пайплайна и проверка

1.  Добавьте приведенный выше код в ваш `.gitlab-ci.yml` и отправьте изменения в репозиторий.
2.  Перейдите в вашем проекте GitLab в раздел **Build -> Pipelines**.
3.  Нажмите кнопку **Run pipeline**. В открывшемся окне, не меняя параметров, снова нажмите **Run pipeline**.
4.  Дождитесь успешного завершения задачи `build_android_image`.
5.  Перейдите в раздел **Deploy -> Container Registry**. Вы должны увидеть там ваш новый образ `android-builder` с тегом `latest`.

Теперь у вас есть все необходимое для создания чистого и контролируемого окружения для CI/CD. В последующих задачах вы сможете использовать этот образ, указав в секции `default` или `image`:

```yaml
default:
  image: $CI_REGISTRY_IMAGE/android-builder:latest
```