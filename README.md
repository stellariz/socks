# SOCKS Прокси Сервер

Java реализация SOCKS4, SOCKS4a и SOCKS5 прокси-серверов с поддержкой различных методов аутентификации

## Возможности

### SOCKS4 & SOCKS4a
- ✅ Поддержка стандартного протокола SOCKS4
- ✅ Расширение SOCKS4a для DNS запросов
- ✅ Проксирование TCP соединений

### SOCKS5
- ✅ Режим без аутентификации
- ✅ Аутентификация по логину/паролю (RFC 1929)
- ✅ Поддержка IPv4, IPv6 и доменных имен

## Документация протоколов

Реализация основана на официальных спецификациях:
- [SOCKS4 Protocol](https://www.openssh.org/txt/socks4.protocol)
- [SOCKS4a Protocol](https://www.openssh.org/txt/socks4a.protocol)
- [RFC 1928 - SOCKS Protocol Version 5](https://datatracker.ietf.org/doc/html/rfc1928)
- [RFC 1929 - Username/Password Authentication for SOCKS V5](https://datatracker.ietf.org/doc/html/rfc1929)