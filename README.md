# Daycare catering bill generator (work in progress)

### Web application for generating monthly daycare catering bills, and sending them to parents via email. (Java , Maven, Spring boot, Hibernate, JWT, Junit5, frontend in Angular 8)


### How to install (Maven and node.js required):
*	Open the terminal in the main project directory and enter _mvn clean install_ command.
*	Navigate to **frontend** folder and enter _npm install_ command.

### How to run (Angular CLI required):
*	Navigate to **frontend** folder and enter _ng serve_ command.
*	Run the main method from **DaycareCateringBillGeneratorApplication** class located in **src\main\java\jedrekp\daycarecateringbillgenerator**
*	Go to http://localhost:4200/  in your browser.

### Some additional notes
In the current state, app runs on H2 in memory database and is populated with some example data on start.
You may login using one of the following credentials:

**username: jlocke | password: headmaster1** - a _headmaster_ account with an access to all of the app functionalities including:
* creating and managing: children profiles, catering options, daycare groups
* tracking and modifying children attendance
* generating/sending catering bills.

**username: mfarcik | password: supervisor1** – a _group supervisor_ account which is granted an ability to track attendance for children from daycare group assigned to this user. Additionally, this account has a read-only access to some parts of the application.

(Additional _group supervisor accounts_ can be created by _headmaster_)
