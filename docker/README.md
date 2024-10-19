# NRS Docker

## Предварительные шаги

1. На Windows и MacOS можно установить [Docker Desktop](https://www.docker.com/products/docker-desktop/). На Linux тоже, но лучше просто установить пакет `docker-ce`, перед этим добавив соответствующий репозиторий. Инструкции для основных дистрибутивов можно найти [здесь](https://docs.docker.com/engine/install/). 
2. Запустить демон Docker'а (при работе с Docker Desktop достаточно просто открыть само приложение).


## Команды
Все команды нужно выполнять из директории `docker/`:
```shell
cd docker/
```

**Запустить** все сервисы (на данный момент существует только один сервис - БД PostgreSQL):
```shell
docker compose up -d
```

**Остановить**:
```shell
docker compose down
```

**Очистить БД** (и остановить сервисы, если запущены) (по умолчанию информация в БД сохраняется между запусками):
```shell
docker compose down -v
```


## Примечания

### [`lazydocker`](https://github.com/jesseduffield/lazydocker)
Это удобная утилита для управления контейнерами, volume'ами, сетями docker интерактивно прямо из терминала

#### Установка
Linux и MacOS (Homebrew):
```shell
brew install lazydocker
```

Windows (Chocolatey):
```shell
choco install lazydocker
```

Windows (Scoop):
```shell
scoop install lazydocker
```
