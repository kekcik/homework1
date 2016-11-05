# Калькулятор удобный v 0.1

* Поддерживает порядок операций, т.е. на запрос 2 + 2 * 2 будет выведено 6, а не 8, как при наивной реализации. P.S.: этот пункт сильно усложнил логику калькуятор из-за чего кода оказалось много.
* Поддерживает унарные операции для числа, которое мы вводим на экран.
* Адаптировано для горизонтальной ориентации (немного изменено расположение кнопок).
* Работает при корректном пользовательском запросе.
* Разделяет команды clear (C) и all clear (AC)
* Допускает операции с точкой

Created by Ivan Trofimov richard1997dog@gmail.com

# Калькулятор

Предлагается написать простейшее приложение «Калькулятор».

Требования:
Идентификаторы кнопок должны иметь идентификаторы с именами d0, d1, …, d9. Идентификатор кнопок операций: div, mul, sub, add, кнопки «равно» - eqv, кнопки «очистить» - clear, текстового поля с результатом вычисления - result.

Критерии оценки (максимальные баллы):
- 2 балла - за корректную вёрстку
- 2 балла - за корректное отображение вводимых данных (обработка нажатий)
- 2 балла - корректные вычисления
- 2 балла - поддержка ориентаций (разная вёрстка для разных ориентаций, сохранение состояния при перевороте экрана)
- 2 балла - преподавателю нравится стиль кода
