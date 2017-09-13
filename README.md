
#Running Routes

As a runner, I wanted to build an app to allow myself and other runners to keep track of our running activity and share local running routes, since it's always great to find new places to explore. Runners can log in to their own personal page, add data about their runs, and save their routes. Users can choose routes they have saved or create new routes for each run added. Routes are saved as a url link which when clicked on will display a Google map with the start and endpoint and the route outlined.

Users can also delete or update information about their run (date, distance, and time). 

Visitors to the site are able to see routes that have been made public by the users, and can click on the map to be taken to a page with the map displayed larger and zoomed in.

An admin has the ability to see demographic information about the users who have signed up, and can view each users home page. Admins are not allowed to update or delete user run information, but can see the routes a user has created.

Future features to add:

*Allow user to edit route information

*Link to local track club website and provide list of upcoming running events on home page

*Add a "like" functionality to shared runs

*Allow users to share routes with specific users

*Group user runs together by route and highlight the fastest time

This app was built in Java using Spring Boot, with a Postgres SQL database managed using JPA. Spring security was used for authenitication/authorization of the endpoints, and  BCrypt was used to encrypt passwords.  Netflix's Feign HTTP client was used to make requests to Google's Geocoding and Directions API, and information from the response received fromt those requests was used to create the url for Google's Static Map Api which when passed to the HTML, displays the map. 

Thymeleaf was used to display dynamic information on the page, and Thymeleaf Security was also used to display information depending on the type of user. Bootstrap and CSS were used for styling the HTML.


 
 
 
 