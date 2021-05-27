## Задача:

Нужно разработать кроссплатформенное приложение (windows, linux) для подготовки к сдаче экзамена по ПДД, по категориям ABM и CD (выбор категорий отдельно), в котором будут следующие возможности:

1) Режим "Тренировка" (с отображением правильного ответа и комментария к вопросу, в случае если ответ не правильный). При ответе на вопрос можно воспользоваться подсказкой. Тренировку можно проводить по номерам билетов, либо по темам;
2) Режим "Экзамен" (результаты показываются только в конце). Вопросы в режиме "Экзамен" формируется по случайным 20 вопросам из билетов по категории (с учетом реквизита «number»). Время на экзамен ограничено 20 минутами. Даётся возможность допустить ошибку в двух вопросах. В случае, если пользователь ошибается – к вопросам добавляются еще 5 вопросов и 5 минут времени.  После прохождения экзамена результаты пользователя должны сохраняться;
3) У администратора программы должна быть возможность собирать статистику по пользователям. В разрезе параметров: пользователь, дата прохождения экзамена, количество правильных ответов, количество неправильных. 
4) Желательно сделать еще режим «Сложные вопросы», который будет формироваться из вопросов, в которых пользователь допустил ошибку. Если это сложно – то не надо;
5) В программе должна быть система лицензирования;
6) Должна быть база знаний. То есть, сами правила, информация о размере штрафов и информация по знакам. В каком виде сделать базу знаний я пока не решил, поэтому это в последнюю очередь.

*Приложенные файлы*:

topics.json – Список тем билетов
- id – идентификатор темы,
- name – Название темы

questions.json – Список вопросов по категориям
- blockNumber – номер блока вопросов от 1 до 4. Каждый блок вопросов по 5шт;
- Number – порядковый номер вопроса в блоке
- ticketNumber – Номер билета
- cat – категория прав. 0 – ABM, 1 – CD;
- Text – Текст вопроса;
- Id –идентификатор вопроса
- Image – имя файла с картинкой к вопросу
- Comment – Подсказка к вопросу
- Options[] – Массив возможных вариантов ответа
- rightOption – Идентификатор правильного ответа
- topics[] – Массив тем, к которым относится вопрос.

основная часть:

- 36 часов

правки:

- 6 часов