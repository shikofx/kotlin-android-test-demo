# Урок 2: Установка GitLab CE на сервере

Этот документ описывает два способа установки GitLab Community Edition (CE) на арендованном сервере (например, на Beget) под управлением Ubuntu.

## 0. Базовая настройка и защита сервера (Рекомендуется)

Перед установкой GitLab настоятельно рекомендуется выполнить базовую настройку сервера для повышения безопасности:

1.  **Создать пользователя с правами `sudo`** и работать из-под него, а не из-под `root`.
    *   [Подробная инструкция: Управление пользователями в Linux](../linux/user-management.md)

2.  **Настроить SSH-сервер** для входа только по ключам, отключив вход по паролю.
    *   [Подробная инструкция: Настройка безопасного SSH-сервера](../ssh/secure-sshd-config.md)

3.  **Настроить брандмауэр (Firewall)**, открыв необходимые порты (SSH, HTTP, HTTPS).
    *   Подробная инструкция: Настройка брандмауэра UFW

Также не забудьте обновить пакеты в системе:
```bash
sudo apt update && sudo apt upgrade -y
```

## 1. Предварительные требования для GitLab

Перед началом установки убедитесь, что ваш сервер соответствует минимальным требованиям:

*   **ОС:** Ubuntu 20.04/22.04.
*   **Ресурсы:** Минимум **4 vCPU** и **4 ГБ RAM**. GitLab — ресурсоемкое приложение.
*   **Домен:** Зарегистрированное доменное имя, A-запись которого указывает на IP-адрес вашего сервера (например, `gitlab.your_domain.xyz`).
*   **Доступ:** SSH-доступ к серверу с правами `root` или `sudo`.

## 2. Способ 1: Установка с помощью Docker (Рекомендуемый)

Этот способ является предпочтительным, так как он изолирует GitLab и все его зависимости в контейнерах, упрощая управление и обновление.

### Шаг 1: Установка Docker и Docker Compose

Если Docker еще не установлен, выполните следующие команды:

```bash
# Обновляем список пакетов
sudo apt-get update

# Устанавливаем необходимые пакеты
sudo apt-get install -y ca-certificates curl

# Добавляем официальный GPG-ключ Docker
sudo install -m 0755 -d /etc/apt/keyrings
sudo curl -fsSL https://download.docker.com/linux/ubuntu/gpg -o /etc/apt/keyrings/docker.asc
sudo chmod a+r /etc/apt/keyrings/docker.asc

# Добавляем репозиторий Docker
echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.asc] https://download.docker.com/linux/ubuntu \
  $(. /etc/os-release && echo "$VERSION_CODENAME") stable" | \
  sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

# Устанавливаем Docker Engine и Compose
sudo apt-get update
sudo apt-get install -y docker-ce docker-ce-cli containerd.io docker-compose-plugin
```

### Шаг 2: Создание `docker-compose.yml`

Создайте директорию для хранения конфигурации и данных GitLab:

```bash
sudo mkdir -p /srv/gitlab
cd /srv/gitlab
```

Внутри этой директории создайте файл `docker-compose.yml`:

```yaml
version: '3.6'
services:
  web:
    image: 'gitlab/gitlab-ce:latest'
    restart: always
    hostname: 'gitlab.your_domain.xyz' # <-- Ваш домен
    environment:
      GITLAB_OMNIBUS_CONFIG: |
        external_url 'https://gitlab.your_domain.xyz' # <-- Ваш домен
        # Указываем GitLab, что SSH доступен по стандартному порту 22
        gitlab_rails['gitlab_shell_ssh_port'] = 22
        # Дополнительные настройки можно добавлять здесь
    ports:
      - '80:80'
      - '443:443'
      - '22:22' # <-- SSH-порт GitLab будет доступен на стандартном порту 22 хоста
    volumes:
      - '/srv/gitlab/config:/etc/gitlab'
      - '/srv/gitlab/logs:/var/log/gitlab'
      - '/srv/gitlab/data:/var/opt/gitlab'
```

**Важно:**
*   В примерах используется ваш домен `gitlab.your_domain.xyz`. Убедитесь, что именно для него настроена A-запись.
*   Мы пробрасываем SSH-порт GitLab (22) на порт `2222` хост-машины, чтобы избежать конфликта со стандартным SSH-сервером сервера.

### Шаг 3: Запуск GitLab

Запустите GitLab в фоновом режиме:

```bash
sudo docker compose up -d
```

Первый запуск может занять 5-10 минут, пока GitLab инициализируется. Вы можете посмотреть список запущенных контейнеров и их статус командой:

```bash
sudo docker ps
```

Имя контейнера (например, `srv-gitlab-web-1`) понадобится для просмотра логов (`sudo docker logs -f <container_name>`) и получения первоначального пароля на следующем шаге.

### Шаг 4: Первый вход и настройка

После установки выполните первый вход и смените пароль администратора.
*   Подробная инструкция: Первый вход в GitLab

## 3. Способ 2: Установка с помощью Omnibus-пакета

Этот метод устанавливает GitLab и все его компоненты непосредственно в операционную систему.

### Шаг 1: Установка зависимостей

```bash
sudo apt-get update
sudo apt-get install -y curl openssh-server ca-certificates
```

### Шаг 2: Добавление репозитория GitLab

Выполните скрипт для добавления официального репозитория GitLab:

```bash
curl https://packages.gitlab.com/install/repositories/gitlab/gitlab-ce/script.deb.sh | sudo bash
```

### Шаг 3: Установка GitLab

Запустите установку, указав ваше доменное имя в переменной окружения `EXTERNAL_URL`:

```bash
sudo EXTERNAL_URL="https://gitlab.your_domain.xyz" apt-get install gitlab-ce
```

Процесс установки и настройки займет несколько минут.

### Шаг 4: Первый вход и настройка

После установки выполните первый вход и смените пароль администратора.
*   Подробная инструкция: Первый вход в GitLab

Установка завершена! Теперь у вас есть собственный сервер GitLab.