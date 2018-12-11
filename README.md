Microservice composition service
=====
This is a microservice implementation that composes the following service: 
* Movie-Search Service
    * movies 
    * genres
* Movie-Info Service
* Artist-Info Service
* Ratings-Stream Service

The first 3 services are REST get endpoint that search for movies, retrieve detail movie info, artist info and a lookup of genres
Ratings service is used as a streaming service for movie ratings. Ratings received are averaged and we calculate for each movie if
it's rating is bellow median average. The composed movie data is a REST get endpoint /movies in JSON format. 

Example GET call:
```Uri
http://localhost:9000/movies?genre=Science%20Fiction&revenue=500000000&offset=0&limit=5
```
Example JSON response: 

```JSON
{  
   "data":{  
      "movieDetail":[  
         {  
            "id":1893,
            "title":"Star Wars: Episode I - The Phantom Menace",
            "averageRating":4.185192,
            "ratedBelowMedian":true,
            "runtime":136,
            "releaseDate":"1999-05-19",
            "revenue":924317558,
            "posterPath":"https://image.tmdb.org/t/p/w342/n8V09dDc02KsSN6Q4hC2BX6hN8X.jpg",
            "originalLanguage":"en",
            "genres":[  
               "Adventure",
               "Action",
               "Science Fiction"
            ],
            "cast":[  
               {  
                  "id":3896,
                  "gender":2,
                  "name":"Liam Neeson",
                  "profilePath":"https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185/9mdAohLsDu36WaXV2N3SQ388bvz.jpg"
               },
               {  
                  "id":3061,
                  "gender":2,
                  "name":"Ewan McGregor",
                  "profilePath":"https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185/aEmyadfRXTmmR7UW7OXsm5a6smS.jpg"
               },
               {  
                  "id":524,
                  "gender":1,
                  "name":"Natalie Portman",
                  "profilePath":"https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185/rtLTG4yrEcROXhTBGXMrbyiUEC5.jpg"
               },
               {  
                  "id":33196,
                  "gender":2,
                  "name":"Jake Lloyd",
                  "profilePath":"https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185/1MndIkdjjDypRDi3PpMzy3j0Lof.jpg"
               },
               {  
                  "id":27762,
                  "gender":2,
                  "name":"Ian McDiarmid",
                  "profilePath":"https://image.tmdb.org/t/p/w185/sa6FTcK7xCHCFFR10jyOOOffd7f.jpg"
               }
            ]
         }
      ]
   }
}
```  


Description and architecture used
-------
Used language, frameworks, libraries: 
* Scala: Play framework, SBT, ScalaTest, Akka Streams (built-in Play), Circe

Architecture points: 

Each endpoint / service has it's own service class that wraps the code and functionality: MovieSearchService, MovieInfoService, ArtistInfoService and RatingService.
Also it's own models (case classes) and specific exceptions. All calls for the downstream REST services are done using Scala Futures in an asynchronous manner.

As the REST services might fail, with different probabilities when doing calls to them there is a retry mechanism with progressive back off strategy implemented. 
This is configured roughly for each service based on it's failure rate. There is no library used here, just standard Scala code for it to prove a fact.  
Retry mechanism is implemented generically in the class: `RetryHandler.scala`. The retry mechanism can be tweaked based on the use using these parameters: 
`(noTries: Int, factor: Float = 1.5f, init: Int = 1, cur: Int = 0)`

Data received from the rating streaming service is sourced into an Akka-Streams stream. I am not using any other library for Akka-Streams, just using the in-build Play
version for simplicity. The output from the stream I persist in a file (`ratings.txt`) to make things simpler, avoiding any other framework, database, service 
(e.g. Kafka). The service will read data from the file (a snapshop of the current streams received) and calculate for distinct movie ids the average rating and then if
rating average is bellow the median average.  
* averageRating
* isBellowAverageRating
For now the streaming service relies in the same repository, microservice but could be considered as an independent one. It also depends whether there are other
services / teams that might subscribe to it.

All the data is collected from the downstream services using error handling and retry mechanism tailored for its needs. After we have the data we call `Composer.scala`
to compose the response from each data source into a `MovieDetail` model. The prodused model is then returned in the GET call `/movies` in JSON format. 

All JSON serialization and deserialization is done via Circe using automatic encoding / decoding. For this reason we need to maintain the exact field names in the 
JSON and in the models.

Architecture diagram:


Future improvements suggestions
-------
In order to maintain a more simple solution I have made some compromises in order to save time. For a more mature, production ready system we definitely would
need to do these improvements:
* cache results from Lookup service endpoint `genres`
* add monitoring of application
* add CI (e.g. GO.CD or Jenkins) for build, test, deploy
* fine tune retry params for each service based on actual data from the monitoring systems
* move uri's, file names, timeout settings and other keys to config file
* fine tune timeout configuration based on data for each service
* resilience use specialized libraries and solutions e.g. Netflix Hystrix (https://github.com/Netflix/hystrix/wiki)
* use a more mature streaming solution instead of just streaming to a file as output
* add logging (e.g. logback)
* paging is currently not implemented and I am just cutting the results in order to simplify the solution. A fully implemented paging solution is a problem
in itself and I tried to focus more on the important parts of the design. If needed I can give an example of what and how should be implemented.

The problem for the pagination is that currently downstream services e.g. movie info, artist-info have a limitation to 5 results so we would need
to make multiple calls to get all the results (e.g. search can produce 100 results) and we should be using the batched / single request calls based on 
the use-case. 

Streaming solution improvements: 
Could use for example Kafka using Akka-Streams to handle back-pressure issues that might occur. Use log compaction from Kafka in order to reduce load. 
Persist data from stream to a database, key-value store instead of a file.  
Treat failures for the streaming endpoint or inside the Akka-Stream stream for ratings.

Hystrix suggestions:
* use ChaosMonkey if service is run on a cluster
* integrate circuit breakers

Another alternvative could be to use http://reactivex.io/ observables. 

How to run it
-------

Prerequisites - Start dependencies (downstream services) first:
```bash
docker-compose up
```

Using SBT terminal using:
 
```sbtshell
sbt clean 
sbt compile
sbt test
sbt run
```

You can also run and debug using IntelliJ if you want.

Scalafmt:
--------
In order to have well formatted, consistent, easy to maintain code approved by Scala community standards I use Scalafmt.
It is configurable to work within IntelliJ or other IDEs, integrated with your favourite shortcuts and also at build time
when a file is saved code will be reformatted accordingly.
Installation documentation:https://github.com/lucidsoftware/neo-sbt-scalafmt
Useful `sbt` commands to run Scalafmt tasks:
```sbtshell
> scalafmt       # format compile sources
> test:scalafmt  # format test sources
> sbt:scalafmt   # format .sbt source
``` 

Useful link: 
--------------
1. Play framework: https://www.playframework.com/documentation/2.6.x/Home
2. Circe: https://circe.github.io/circe/ 
3. Akka-Streams from Play: https://www.playframework.com/documentation/2.6.x/ScalaWS#directly-creating-wsclient 
4. Exponential back-off strategy with Scala Futures: https://hackernoon.com/exponential-back-off-with-scala-futures-7426340d0069
5. Scalafmt: https://scalameta.org/scalafmt/