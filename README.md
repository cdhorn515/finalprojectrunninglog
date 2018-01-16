
# Running Routes 🏃‍♀️ :runner:

## About

As a runner, I wanted to build an app to allow myself and other runners to keep track of our running activity and share local running routes, since it's always great to find new places to explore. 

This project is deployed using AWS Elastic Beanstalk and can be viewed [here](http://runninglog-env.3npbd4agqq.us-east-2.elasticbeanstalk.com/). Use the following if you want to log in and view the admin section: 

```
username: admin 
password: Runadm!n
```

### Built with:

* Maven
* Java 8 (JDK 1.8.131)
* Spring Boot/Spring Security/Spring Mobile
* BCrypt
* HTML5
* CSS3
* Thymeleaf
* Bootstrap
* PostgresSQL


## Run this project locally

You will need to create a Postgres database 

`createdb [dbname]`

`psql [dbname]`

with the following schema:

```
CREATE TABLE map_route (
    id SERIAL INTEGER PRIMARY KEY,
    shared BOOLEAN,
    startposition VARCHAR(1000),
    endposition VARCHAR(1000),
    url VARCHAR(1000),
    route_name VARCHAR(100),
    legs VARCHAR(1000),
    user_id INTEGER REFERENCES user_data(id)
);

CREATE TABLE role (
    id SERIAL INTEGER PRIMARY KEY, 
    name VARCHAR(20)
);

CREATE TABLE run (
    id SERIAL INTEGER PRIMARY KEY, 
    date DATE,
    distance DOUBLE,
    time TIME,
    user_id INTEGER REFERENCES user_data(id),
    map_route_id INTEGER REFERENCES map_route(id)
);

CREATE TABLE user_data (
    id SERIAL INTEGER PRIMARY KEY, 
    username VARCHAR(100) UNIQUE,
    password VARCHAR(1000),
    age INTEGER,
    gender CHAR,
    city VARCHAR(100),
    state CHAR(2),
    active BOOLEAN,
    first VARCHAR(100),
    last VARCHAR(100),
    role_id INTEGER REFERENCES role(id)
);
```

* Clone this repository and in your terminal type `git clone` and paste the url that you copied. 
* CD into the **templates** directory and create an application.properties file.
 
 ```
cd /src/main/resources
touch application.properties
```
* Open this file using your favorite text editor and add the following:

```
spring.thymeleaf.cache=false

spring.datasource.url=jdbc:postgresql://localhost:5432/[your-database-name]
spring.datasource.username=[your-database-username](if applicable)
spring.datasource.password=[your-database-password](if applicable)
```
 
 * You will need to generate 3 Google Maps API keys. These are free and can be generated through the directions found at the following links: [Static Maps](https://developers.google.com/maps/documentation/static-maps/), [Directions](https://developers.google.com/maps/documentation/directions/), [Geocoding](https://developers.google.com/maps/documentation/geocoding/start).
 
 * Back in your **templates** directory create another file called apikey.properties. Open this file in your text editor and add the following, placing your Google Maps API keys where indicated:
 
 ```
STATIC_MAP_KEY=[your Google Maps API Static Maps key]
DIRECTIONS_KEY=[your Google Maps API Directions key]
GEOCODING_KEY=[your Google Maps API Geocoding key]

PARKS_URL=https://maps.googleapis.com/maps/api/staticmap?zoom=11&size=400x500&markers=size:mid%7Ccolor:red%7Clabel:1%7C34.8495083,-82.384894&markers=size:mid%7Ccolor:red%7Clabel:2%7C34.8444847,-82.40111550000002&markers=size:mid%7Ccolor:red%7Clabel:4%7C34.9311528,-82.36975679999999&markers=size:mid%7Ccolor:red%7Clabel:3%7C34.7700466,-82.3492679&key=[your Google Maps API Static Maps key]

CLEVELAND_URL=https://maps.googleapis.com/maps/api/staticmap?zoom=14&size=500x500&markers=size:mid%7Ccolor:red%7Clabel:1%7C34.8495083,-82.384894&key=[your Google Maps API Static Maps key]

FALLS_URL=https://maps.googleapis.com/maps/api/staticmap?zoom=15&size=500x500&markers=size:mid%7Ccolor:red%7Clabel:2%7C34.8444847,-82.40111550000002&key=[your Google Maps API Static Maps key]

PARIS_URL=https://maps.googleapis.com/maps/api/staticmap?zoom=12&size=500x500&markers=size:mid%7Ccolor:red%7Clabel:3%7C34.9311528,-82.36975679999999&key=[your Google Maps API Static Maps key]

CONESTEE_URL=https://maps.googleapis.com/maps/api/staticmap?zoom=12&size=500x500&markers=size:mid%7Ccolor:red%7Clabel:4%7C34.7700466,-82.3492679&key=[your Google Maps API Static Maps key]

``` 
 
* Run `mvn clean package` from your project directory (this will build the project and create a jar file in the target directory).


* To run the project type: `java -jar target/finalproject-1.1.25-RELEASE.jar`, then open a browser window and go to `localhost:8080`.

### About this application

Runners can log in to their own personal page, add data about their runs, and save their routes. Logged in runners can also delete or update information about their run (date, distance, and time), and can choose routes they have previously saved or create new routes for each run added. Routes are saved as a url link which when clicked on will display a Google map with the start and endpoint and the route outlined.

Visitors to the site are able to see routes that have been made public by the users, and can click on the map to be taken to a page with the map displayed larger and zoomed in.

An admin has the ability to see demographic information about the users who have signed up, and can view each users home page. Admins are not allowed to update or delete user run information, but can see the routes a user has created.

This app was built in **Java 8** using **Spring Boot**, with a **PostgreSQL** database managed using **JPA**. Spring security was used for authenitication/authorization of the endpoints, and **BCrypt** was used to encrypt passwords.  **Netflix's Feign** *HTTP client* was used to make requests to Google's *Geocoding* and *Directions API*, and information from the response received fromt those requests was used to create the url for Google's *Static Map API* which when passed to the HTML template, displays the map. 

**Thymeleaf** was used to display dynamic information on the page, and **Thymeleaf Security** was also used to display information depending on the type of user. **Bootstrap** and **CSS** used for styling.



 
 
 
 