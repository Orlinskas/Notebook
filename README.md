# Notebook
### Скриншоты

<img src="https://raw.githubusercontent.com/Orlinskas/Notebook/master/app/src/main/assets/S91107-06490196.jpg" width="160" height="300"/>  <img src="https://raw.githubusercontent.com/Orlinskas/Notebook/master/app/src/main/assets/S91107-06474483.jpg" width="160" height="300"/>  <img src="https://raw.githubusercontent.com/Orlinskas/Notebook/master/app/src/main/assets/S91107-06491096.jpg" width="160" height="300"/>

### Обзор
 #### Зависимости
 + Использована ORM ROOM 
 + Объекты сериализовались с помощью Parceler https://github.com/johncarl81/parceler
 + Использован Mockito 3.0.0 тесты на JUnit:4.12
 + Для правильной работы Background Service на Андроид Oreo and higher использована библиотека
   https://github.com/Euzee/serviceManager
 
 #### Уведомления создаются по схеме AlarmManager -> BroadcastReciver -> ServiceManager -> создание и запуск.
      (уведомления приходят только с запущенным приложением ).
 #### Дальнейшая синхронизация с сервером реализована с помощью soft delete (двух полей "isSynchronized" и "deleted_at" в Entity).
 #### Так же реализовано сохранение состояния при смене landscape
