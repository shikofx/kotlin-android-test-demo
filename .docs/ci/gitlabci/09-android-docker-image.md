# Урок 9: Создание Docker-образа для сборки Android

Чтобы GitLab CI мог собирать и тестировать Android-проект, ему нужно окружение, в котором установлены Java (JDK) и Android SDK. Вместо того чтобы устанавливать все это на сервер с GitLab Runner'ом, мы создадим специальный Docker-образ, который будет содержать все необходимые инструменты.

Это дает несколько преимуществ:
*   **Изоляция:** Зависимости для сборки не "засоряют" хост-систему.
*   **Воспроизводимость:** Сборка всегда происходит в одном и том же, предсказуемом окружении.
*   **Версионирование:** Вы можете легко переключаться между разными версиями JDK или SDK, просто меняя тег образа.

## Шаг 1: Создание Dockerfile

Мы создали файл `.docker/android/Dockerfile`, который описывает, как построить наш образ.

**Ключевые моменты в `Dockerfile`:**
1.  **`FROM ubuntu:22.04`**: Мы начинаем с чистого и стабильного базового образа Ubuntu.
2.  **`apt-get install -y openjdk-17-jdk ...`**: Устанавливаем JDK 17, так как проект настроен на эту версию Java.
3.  **`ENV ANDROID_SDK_ROOT...`**: Задаем стандартные переменные окружения, чтобы система знала, где искать Android SDK.
4.  **`wget ... && unzip ...`**: Скачиваем и распаковываем официальные `command-line tools` от Google.
5.  **`yes | sdkmanager --licenses`**: Автоматически принимаем все лицензионные соглашения Android SDK. Без этого шага сборка в CI будет падать.
6.  **`sdkmanager "platform-tools" "platforms;android-34" ...`**: С помощью `sdkmanager` устанавливаем необходимые компоненты: `platform-tools`, `build-tools` и нужную версию Android API.

## Шаг 2: Сборка и публикация образа в GitLab Container Registry

GitLab предоставляет встроенный `Container Registry` — приватное хранилище для ваших Docker-образов. Мы настроим CI/CD пайплайн, который будет автоматически собирать и публиковать наш образ.

Для этого нужно будет добавить новую `job` в ваш `.gitlab-ci.yml`.

### Пример задачи для `.gitlab-ci.yml`

```yaml
build_android_image:
  stage: build_image # Это должен быть один из первых этапов
  image: docker:24.0.5 # Используем официальный образ Docker для сборки
  services:
    - docker:24.0.5-dind # Запускаем Docker-in-Docker, чтобы можно было выполнять docker-команды
  variables:
    # Указываем, что нужно использовать TLS для подключения к Docker-демону
    DOCKER_TLS_CERTDIR: "/certs"
    IMAGE_TAG: $CI_REGISTRY_IMAGE/android-builder:$CI_COMMIT_SHA
  before_script:
    # Логинимся в Container Registry нашего GitLab
    - docker login -u $CI_REGISTRY_USER -p $CI_REGISTRY_PASSWORD $CI_REGISTRY
  script:
    # Собираем образ из нашего Dockerfile
    - docker build -t $IMAGE_TAG -f .docker/android/Dockerfile .
    # Публикуем образ в Container Registry
    - docker push $IMAGE_TAG
  rules:
    # Запускать эту задачу только при изменениях в Dockerfile
    - if: '$CI_PIPELINE_SOURCE == "merge_request_event"'
      changes:
        - .docker/android/Dockerfile
    - if: '$CI_COMMIT_BRANCH == "main"'
      changes:
        - .docker/android/Dockerfile
```

После того как эта задача выполнится, ваш образ будет доступен в **Packages & Registries -> Container Registry** вашего проекта. В последующих `jobs` вы сможете использовать его с помощью директивы `image: $CI_REGISTRY_IMAGE/android-builder:$CI_COMMIT_SHA`.

Теперь у вас есть все необходимое для создания чистого и контролируемого окружения для CI/CD.