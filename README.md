# Описание

Unbounded Sound - сервис для обмена и прослушивании музыки.

## Api

### Пользователи

запрос/ответ в форме application/json 

users

POST /users

RequestBody:
```
{
    "email": "string",
    "password": "string"
    "nickname": "string",
}
```
1. date - в iso формате
2. email - почта; уникальный идентификатор
3. password - пароль не мении 8 символов

ResponseStatus:

HttpStatus: 200

ResponseBody:
```
{
    "id": "int4"
}
```

1. id - номер под которым сохранился пользователь

HttpStatus: 400

GET /users/profile - Получение информации о пользователе

RequestBody: 
```
```

Headers: 
```
Required: BearerToken
```

HttpStatus: 200

ResponseBody
```
{
    "id": "long",
    "nickname": "string",
    "email": "string",
}
```

HttpStatus: 403

PATCH /users/profile - изменения данных профиля пользователя

RequestBody:
```
{
    "email": "string",
    "nickname": "string",
}
```
Можно заполнять не все поля, отправлять все

Headers:
```
Required: BearerToken
```

HttpStatus: 200

ResponseBody:
```
    "id": "int4"
```

HttpStatus: 400

HttpStatus: 403

DELETE /users - удаление пользователя, без удаление треков, загруженных им (они становятся достоянием общественности)

RequestBody:
```
```

Headers:
```
Required: BearerToken
```

HttpStatus: 200

ResponseBody:
```
```

### Сессии

sessions

запрос/ответ в форме application/json 

GET /sessions - получение данных о текущей сессии по id

RequestBody
```
{
    "id": "uuid"
}
```

Headers
```
Required: Bearer token
```

HttpStatus: 200:
```
{
    "expiredAt": "datetime"
    "enable": "boolean"
}
```

POST /sessions/login - создание новой сессии (aka логин в систему)

ResponseRequest
```
{
    "email": "string",
    "password": "string"
}
```

HttpStatus: 200

ResponseBody
```
{
    "id": "uuid",
    "accessToken": "string",
    "issuedAt": "datetime"
    "expiredAt": "datetime"
}
```

1. id - uuid идентификатор сессии в системе
2. accessToken - bearer токен, с которым пользователь обращается к системе с запросами
3. issuedAt - время создание сессии
4. expiredAt - время окончания сессии

HttpStatus: 403

POST /sessions/logout - выйти из сессии

RequestBody
```
```

Headers
```
Required: Bearer token
```

HttpStatus: 200

ResponseBody
```
```

HttpStatus 403

## Формат сообщений о ошибках:

1. HttpStatus: 500 - ошибка на стороне сервера
2. HttpStatus: 400 - ошибка запроса: запрос не соответствует протоколу dto, или содержит невалидные данные, отображено в сообщении ошибки  
3. HttpStatus: 403 - ошибка авторизации: устаревший токен, неправильный пароль, отображено в сообщении

ResponseBody
```
{
    "code": "int4",
    "message": "string"
}
```
code - уникальный код ошибки, соответствующей ошибке
message - сообщение ошибки

Соответствие ошибок:
```
1 - ошибка1
2 - ошибка2
3 - ошибка3
```

## Форматы

1. Iso формат времени - [Iso format documentation](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/format/annotation/DateTimeFormat.ISO.html)
