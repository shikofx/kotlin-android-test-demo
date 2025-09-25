# Настройка безопасного SSH-сервера (sshd_config)

Файл `/etc/ssh/sshd_config` — это главный конфигурационный файл для **сервера** SSH (`sshd`). Правильная настройка этого файла является одним из первых и самых важных шагов для обеспечения безопасности вашего сервера.

Ниже приведен пример надежной конфигурации, которая отключает небезопасные методы входа и разрешает только аутентификацию по SSH-ключам.

Чтобы открыть файл для редактирования, используйте текстовый редактор, например `nano`, с правами суперпользователя:

```bash
sudo nano /etc/ssh/sshd_config
```

## Рекомендуемая конфигурация `/etc/ssh/sshd_config`

Это эталонный файл конфигурации, который можно использовать как основу для вашего сервера.

```ini
# This is the sshd server system-wide configuration file.  See
# sshd_config(5) for more information.

# This sshd was compiled with PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin:/usr/games

# The strategy used for options in the default sshd_config shipped with
# OpenSSH is to specify options with their default value where
# possible, but leave them commented.  Uncommented options override the
# default value.

Include /etc/ssh/sshd_config.d/*.conf

# When systemd socket activation is used (the default), the socket
# configuration must be re-generated after changing Port, AddressFamily, or
# ListenAddress.
#
# For changes to take effect, run:
#
#   systemctl daemon-reload
#   systemctl restart ssh.socket
#
# ВАЖНО: В современных системах с systemd служба SSH запускается через сокет (ssh.socket).
# Если вы меняете номер порта (Port), systemd может не узнать об изменениях,
# что приведет к ошибке "A dependency job for ssh.service failed".
# 
# Чтобы изменения вступили в силу, нужно перезапустить службы в правильном порядке:
# 
#   sudo systemctl daemon-reload
#   sudo systemctl restart ssh.socket
#   sudo systemctl restart ssh.service
# 
Port 22
Port 2222
AddressFamily any
#ListenAddress 0.0.0.0
#ListenAddress ::

#HostKey /etc/ssh/ssh_host_rsa_key
#HostKey /etc/ssh/ssh_host_ecdsa_key
#HostKey /etc/ssh/ssh_host_ed25519_key

# Ciphers and keying
#RekeyLimit default none

# Logging
#SyslogFacility AUTH
#LogLevel INFO

# Authentication:

#LoginGraceTime 2m
PermitRootLogin no
#StrictModes yes
#MaxAuthTries 6
#MaxSessions 10

PubkeyAuthentication yes

# Expect .ssh/authorized_keys2 to be disregarded by default in future.
#AuthorizedKeysFile     .ssh/authorized_keys .ssh/authorized_keys2

#AuthorizedPrincipalsFile none

#AuthorizedKeysCommand none
#AuthorizedKeysCommandUser nobody

# For this to work you will also need host keys in /etc/ssh/ssh_known_hosts
#HostbasedAuthentication no
# Change to yes if you don't trust ~/.ssh/known_hosts for
# HostbasedAuthentication
#IgnoreUserKnownHosts no
# Don't read the user's ~/.rhosts and ~/.shosts files
#IgnoreRhosts yes

# To disable tunneled clear text passwords, change to no here!
PasswordAuthentication no
#PermitEmptyPasswords no

# Change to yes to enable challenge-response passwords (beware issues with
# some PAM modules and threads)
KbdInteractiveAuthentication no

# Kerberos options
#KerberosAuthentication no
#KerberosOrLocalPasswd yes
#KerberosTicketCleanup yes
#KerberosGetAFSToken no

# GSSAPI options
#GSSAPIAuthentication no
#GSSAPICleanupCredentials yes
#GSSAPIStrictAcceptorCheck yes
#GSSAPIKeyExchange no

# Set this to 'yes' to enable PAM authentication, account processing,
# and session processing. If this is enabled, PAM authentication will
# be allowed through the KbdInteractiveAuthentication and
# PasswordAuthentication.  Depending on your PAM configuration,
# PAM authentication via KbdInteractiveAuthentication may bypass
# the setting of "PermitRootLogin prohibit-password".
# If you just want the PAM account and session checks to run without
# PAM authentication, then enable this but set PasswordAuthentication
# and KbdInteractiveAuthentication to 'no'.
UsePAM yes

#AllowAgentForwarding yes
#AllowTcpForwarding yes
#GatewayPorts no
X11Forwarding yes
#X11DisplayOffset 10
#X11UseLocalhost yes
#PermitTTY yes
PrintMotd no
#PrintLastLog yes
#TCPKeepAlive yes
#PermitUserEnvironment no
#Compression delayed
#ClientAliveInterval 0
#ClientAliveCountMax 3
#UseDNS no
#PidFile /run/sshd.pid
#MaxStartups 10:30:100
#PermitTunnel no
#ChrootDirectory none
#VersionAddendum none

# no default banner path
#Banner none

# Allow client to pass locale environment variables
AcceptEnv LANG LC_*

# override default of no subsystems
Subsystem       sftp    /usr/lib/openssh/sftp-server

# Example of overriding settings on a per-user basis
#Match User anoncvs
#       X11Forwarding no
#       AllowTcpForwarding no
#       PermitTTY no
#       ForceCommand cvs server
```

## Разбор ключевых параметров

Ниже приведено детальное описание всех параметров из эталонной конфигурации, включая закомментированные.

### Секция подключения

*   `Include /etc/ssh/sshd_config.d/*.conf`
    Эта директива подключает все файлы с расширением `.conf` из указанной директории. Это позволяет разделять конфигурацию на модули, что упрощает управление.

*   `Port 22` и `Port 2222`
    Указывает, что SSH-сервер будет принимать подключения сразу на двух портах.

*   `AddressFamily any`
    Определяет, какие IP-протоколы использовать. Значение `any` означает, что сервер будет слушать подключения как по IPv4, так и по IPv6. Другие варианты: `inet` (только IPv4) и `inet6` (только IPv6).

*   `#ListenAddress 0.0.0.0` и `#ListenAddress ::`
    (Закомментировано) Позволяет указать, на каких конкретных IP-адресах сервера принимать подключения. Если закомментировано, сервер слушает на всех доступных сетевых интерфейсах.

### Секция ключей хоста

*   `#HostKey /etc/ssh/ssh_host_..._key`
    (Закомментировано) Указывает путь к приватным ключам сервера (хоста). Эти ключи используются для идентификации самого сервера клиентами. По умолчанию используются стандартные пути, поэтому эти строки обычно закомментированы.

### Секция аутентификации

*   `PermitRootLogin no`
    **(Критически важно для безопасности)**. Запрещает вход в систему под пользователем `root` напрямую через SSH.

*   `#StrictModes yes`
    (Закомментировано) Если включено (`yes`), SSH-сервер будет проверять права доступа к домашней директории пользователя и его `.ssh` файлам перед тем, как разрешить вход по ключу. Это мера безопасности, предотвращающая вход, если файлы доступны для записи другим пользователям.

*   `#MaxAuthTries 6`
    (Закомментировано) Максимальное количество попыток аутентификации для одного соединения. Помогает защититься от медленного перебора паролей.

*   `#MaxSessions 10`
    (Закомментировано) Максимальное количество одновременных сессий (открытых терминалов) для одного сетевого подключения.

*   `PubkeyAuthentication yes`
    Разрешает аутентификацию с использованием криптографических SSH-ключей. Это основной и самый безопасный способ входа.

*   `#AuthorizedKeysFile .ssh/authorized_keys .ssh/authorized_keys2`
    (Закомментировано) Указывает, в каких файлах искать разрешенные публичные ключи. Путь указан относительно домашней директории пользователя.

*   `PasswordAuthentication no`
    **(Критически важно для безопасности)**. Полностью отключает возможность входа по паролю. Это защищает ваш сервер от атак методом перебора (брутфорс).

*   `KbdInteractiveAuthentication no`
    Отключает аутентификацию "клавиатура-интерактив". Это еще один механизм, который может запрашивать пароль (часто через PAM). Его отключение дополнительно усиливает защиту от входа по паролю.

*   `#PermitEmptyPasswords no`
    (Закомментировано) Запрещает вход пользователям с пустыми паролями.

### Секция PAM

*   `UsePAM yes`
    Включает использование Pluggable Authentication Modules (PAM). PAM — это гибкая система для управления аутентификацией в Linux. Даже при отключении паролей `UsePAM` остается полезным, так как отвечает за управление учетными записями и сессиями.

### Секция перенаправления (Forwarding)

*   `X11Forwarding yes`
    Разрешает перенаправление графического интерфейса (X11) с сервера на клиентскую машину.

*   `#AllowAgentForwarding yes`
    (Закомментировано) Разрешает перенаправление SSH-агента. Это позволяет использовать ваши локальные SSH-ключи на удаленном сервере для подключения к третьему серверу (например, с сервера CI на GitLab).

*   `#AllowTcpForwarding yes`
    (Закомментировано) Разрешает перенаправление TCP-портов (создание туннелей).

### Прочие настройки

*   `PrintMotd no`
    Отключает показ файла `/etc/motd` (Message Of The Day) после входа.

*   `AcceptEnv LANG LC_*`
    Разрешает клиенту устанавливать переменные окружения, связанные с локализацией (`LANG`, `LC_ALL`, и т.д.).

*   `Subsystem sftp /usr/lib/openssh/sftp-server`
    Конфигурирует подсистему для работы протокола SFTP (Secure File Transfer Protocol).

## Применение изменений

После внесения любых изменений в файл `/etc/ssh/sshd_config` необходимо перезапустить службу SSH, чтобы они вступили в силу.

```bash
sudo systemctl restart ssh
```

Перед отключением входа по паролю (PasswordAuthentication no) убедитесь, что вы настроили и проверили вход по SSH-ключу для вашего пользователя. В противном случае вы можете потерять доступ к серверу.