# Notebook
### Скриншоты

<img src="https://raw.githubusercontent.com/Orlinskas/Notebook/master/app/src/main/assets/S91107-06490196.jpg" width="160" height="300"/>  <img src="https://raw.githubusercontent.com/Orlinskas/Notebook/master/app/src/main/assets/S91107-06474483.jpg" width="160" height="300"/>  <img src="https://raw.githubusercontent.com/Orlinskas/Notebook/master/app/src/main/assets/S91107-06491096.jpg" width="160" height="300"/>

### Обзор
 #### Зависимости
 + Использована ORM ROOM 
 + Объекты сериализовались с помощью Parceler https://github.com/johncarl81/parceler
 + Использован Mockito 3.0.0 тесты на JUnit:4.12
 
 #### Уведомления создаются по схеме AlarmManager -> BroadcastReciver -> NotificationService -> создание и запуск.
 
 #### Так же реализовано сохранение состояния при смене landscape
