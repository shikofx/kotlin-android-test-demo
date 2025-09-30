# Урок 9: Docker-образ для сборки Android
# Этот Dockerfile определяет окружение для сборки и тестирования Android-проекта.

# 1. Базовый образ. Используем стабильную версию Ubuntu.
FROM ubuntu:22.04

# 2. Установка переменных окружения для неинтерактивной установки пакетов.
ENV DEBIAN_FRONTEND=noninteractive

# 3. Установка зависимостей: JDK 17 (согласно настройкам проекта), git, unzip и т.д.
RUN apt-get update && \
    apt-get install -y openjdk-17-jdk wget unzip git && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# 4. Установка переменных окружения для Android SDK.
# Версию commandlinetools можно найти на официальном сайте Android Studio.
ENV ANDROID_SDK_VERSION=11076708
ENV ANDROID_SDK_ROOT=/opt/android-sdk
ENV PATH=$PATH:$ANDROID_SDK_ROOT/cmdline-tools/latest/bin:$ANDROID_SDK_ROOT/platform-tools

# 5. Загрузка и установка Android SDK.
RUN mkdir -p ${ANDROID_SDK_ROOT} && \
    wget -q https://dl.google.com/android/repository/commandlinetools-linux-${ANDROID_SDK_VERSION}_latest.zip -O /tmp/cmdline-tools.zip && \
    unzip -q /tmp/cmdline-tools.zip -d ${ANDROID_SDK_ROOT}/cmdline-tools && \
    # Новая структура требует перемещения в папку 'latest'
    mv ${ANDROID_SDK_ROOT}/cmdline-tools/cmdline-tools ${ANDROID_SDK_ROOT}/cmdline-tools/latest && \
    rm /tmp/cmdline-tools.zip

# 6. Принятие лицензий и установка компонентов SDK.
# Важно: Замените версии build-tools и platforms на те, что используются в вашем build.gradle.
RUN yes | sdkmanager --licenses > /dev/null && \
    sdkmanager "platform-tools" "platforms;android-34" "build-tools;34.0.0" && \
    # (Опционально) Добавьте другие необходимые компоненты, например, для эмулятора:
    # sdkmanager "system-images;android-34;google_apis;x86_64"
    apt-get clean

# 7. Установка рабочей директории по умолчанию для будущих команд.
WORKDIR /project