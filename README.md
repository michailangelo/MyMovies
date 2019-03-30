# MyMovies
Java Swing App. 
The app gets all its data from themoviedb.org via a REST API provided by the movie's platform.
The results from the REST API queries are in JSON format. The app builds a GUI with a following options:
1) Saving data to a local db-managed by JAVADB(derby).
2) Creating custom favorite movie lists.
3) Searching for movies using specific criteria.
4) Statistics output like the best rated movies of the db.
5) Exiting and closing the app.
The db schema was done in SQL commands and Java Persistence API was used to communicate with the Java code.
For JSON data process, it uses the javax.json package.

