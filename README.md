# route-planner
Application to calculate a bus itinerary in a city, using an external API providing the bus schedules.

There are two parts in the application:
- client: a Java application where the user inputs the current and final destinations of his trip
- server: a PHP application which, using the user's input, calls an external API that returns the bus lines and schedules matching the user's journey.
The server sens the data back to the client who then displays the itinerary, using an open-source map.
