API

Для работы с приложением используется следующий API:

• URL: https://todo.doczilla.pro/api/todos

Доступные эндпоинты

1. Получение полного списка задач

   • GET /api/todos

   • Описание: Возвращает список всех задач.

2. Получение задач по диапазону дат

   • GET /api/todos/date

   • Описание: Возвращает задачи, которые входят в указанный диапазон дат.

   • Обязательные параметры:

     • from: Начальная дата (в формате YYYY-MM-DD)

     • to: Конечная дата (в формате YYYY-MM-DD)

     • Дополнительный параметр status для фильтрации по статусу (например, выполненные или невыполненные).

3. Поиск задач по названию

   • GET /api/todos/find

   • Описание: Возвращает задачи, в названии которых содержится строка q.

   • Параметр:

     • q: Строка для поиска.
   Установка и запуск

1. Настройка серверной части (при необходимости)

Если требуется обойти CORS-ограничения, создайте серверную часть на любом языке программирования (например, Node.js, Python и т.д.) для проксирования запросов к API.

2. Клиентская часть

  1. Создайте HTML-файл (index.html) с базовой структурой и подключите JavaScript-файл (script.js).

  2. Реализуйте функционал согласно вышеописанным требованиям.

Пример структуры проекта

todo-list/
│
├── index.html
├── script.js
└── styles.css