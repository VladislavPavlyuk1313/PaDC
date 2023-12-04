# Лабораторні роботи з курсу Паралельного програмування.
<h6>Виконав студент групи ІОз-11 Павлюк В.В.</h6>
<br/>
Варіант 6: 

MА = max(B + C) \* MD \* MT + MZ \* MB;  
E=В \* МD + C \* MT \* a. 

<br/>
Цей репозиторій включає в себе:
<ul>
  <li>
    Моделі:
    <ul>
      <li>
        Matrix.java - модель, описує матриці;
      </li>
      <li>
        Vector.java - модель, описує вектори;
      </li>
    </ul>
  </li>
  <li>
    Сервіси:
    <ul>
      <li>
        Adder.java - сервіс, метод .add(), якого реалізує додавання дійсних чисел за алгоритмом Кахана;
      </li>
      <li>
        AppConfig.java - сервіс, який здійснює достів до файлу конфігурації;
      </li>
      <li>
        DataService.java - сервіс, який здійснює запис згенерованих даних в файли, їх читання, а також запис результатів;
      </li>
    </ul>
  </li>
  
  <li>
    GenerateData.java - програма, яка генерує необхідні для виконання лабораторних робіт дані і записує їх до файлів.
  </li>
  
  <li>
    Lab0.java - програма, яка обчислює задані вирази без застосування засобів паралельного програмування.
  </li>
  
  <li>
    Lab1.java...Lab6.java - програми, які обчислюють задані вирази застосувуючи відповідні засоби паралельного програмування.
  </li>
  
</ul>

<h5>Висновки: </h5> в ході виконання даних лабораторних робіт було вивчено та використано на практиці основні способи створення паралельних програм мовою Java. Результати виконання лабораторних робіт показують, що використання засобів паралельного програмування суттєво зменшують час виконання об'ємних обчислень.