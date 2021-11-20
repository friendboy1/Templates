# Description
MVP. Model-View-Presenter

## Navigation
+ [up](https://github.com/friendboy1/Templates/tree/architecture)
+ [root](https://github.com/friendboy1/Templates/tree/master)

## Details
Основная идея в том, что Presenter управляет синхронизацией между View и Model, хранит интерфейс View
и говорит, что нужно выполнить. Методы attach/detach для избегания утечек памяти и отписывания от асинхронных действий.</br>
View отвечает только за отображение и ввод данных, хранит ссылку на Presenter. Model для всего остального

### Сложности
+ Многословность описываемых действий
+ Нужно следить за утечками памяти
+ Синхронизация между ожидаемым и актуальным состоянием (решается через Moxy, которая добавляет слой состояния)

### Links
+ [Курс от SkillBranch Middle Android Developer](https://skill-branch.ru/middle-android-developer)
+ [Пример чистой реализации с даггером и чистой архитектурой](https://github.com/Gaket/Earthquakes)
+ [О различиях MV* паттернов](https://habr.com/ru/company/mobileup/blog/313538/)
+ [Статья про Moxy](https://habr.com/ru/post/276189/)
