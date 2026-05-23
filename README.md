# Homework 3

## Опис

Програма обчислює кількість кроків у послідовності Коллатца для натуральних чисел від 1 до 10 000 000.

Обчислення виконуються паралельно з використанням фіксованого пулу потоків.

Результат містить:
- кількість оброблених значень
- кількість потоків
- загальну кількість кроків
- середню кількість кроків
- час виконання

Результати зберігаються у файл:

results/collatz_parallel_results.csv


## Структура проєкту

Dockerfile  
README.md  
src/  
 Main.java  
results/  
 collatz_parallel_results.csv  


## Запуск за допомогою Docker

Зібрати Docker-образ:

docker build -t collatz-app .

Запустити Docker-контейнер:

docker run --rm -v "%cd%/results:/app/results" collatz-app

---
