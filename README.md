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

Intermediate feedback questions and possible solutions: 
-------
Next I have broken down the points you mentioned and tried to answer them. Please free to ask / address questions or let me know if you want me to do something else
on top (e.g. code-wise or just architecture wise)! Thanks :) 

1) _Currently your ratingsService starts streaming to file on every call Movies endpoint. Basically you are opening a new stream for each request. 
The movie ratings will continuously be coming from that streaming service firehose and you will  be writing a txt file from different requests depending on which 
machine the request ends up.
This is clearly not workable solution. What is the correct way to handle this? (What if you used something like Redis)_

Answer: 
Yes, this is just a naive solution, also without any caching implemented and not cluster ready. 
We could use Redis as a key-value store. We do have the ratings as a key-value data structure: id":337167,"rating":0.39309174.  
For communicating with Redis from the Scala code I could use this library: https://github.com/debasishg/scala-redis. 
Redis quering being a key-value store is limited but for our use case it would suffice. We should query REDIS when a request comes in for ids and then calculate 
in memory the average and "isBellowMedianRating" value. 

Code-wise: 

We would have a REDIS instance / cluster and in the Scala code open a Redis client. Inside the ratingsService AkkaStream when we get new ratings in the source we 
would persist these to Redis. Currently I am saving to a file. The output stream should be Redis now. There are different Redis Akka-Stream drivers that could be used
or something like in this post: https://stackoverflow.com/questions/49028905/whats-the-simplest-way-to-use-sse-with-redis-pub-sub-and-akka-streams
Though not sure we could handle it this way due to the fact that we need to apply some custom logic to implement the median as I have described bellow. 
We should use Redis store as a LIST.

Fetching / querying from Redis should be done in a separate class called: RedisClient. This based on the fact if using a driver or direct AkkaStream sourcing to Redis should
contain: 
```scala> import com.redis._
import com.redis._

scala> val r = new RedisClient("localhost", 6379)
r: com.redis.RedisClient = localhost:6379

scala> r.set("key", "some value")
```
All communication with Redis should be inside a RedisClientImpl client class implementing something like this: 
trait IRedisService { 
    def persistRatings(rating): Unit // not needed if we persist directly inside the AkkaStream as output stream
    def ratings(ids: Int): Seq[Rating]
    def median(): Float
}
For this example I looked at this Scala Redis library: https://github.com/debasishg/scala-redis
We could use this also in order to make the system production ready, having an async Redis cluster.
For testing this class we could use: https://github.com/zxl0714/redis-mock to mock Redis in the unit tests.

2) _You seem to prefer using classes and then dependency inject them into controllers etc. These classes do not have any mutable state, and Scala is a functional language. 
Can you think of a better way? What would be a more idiomatic Scala way of doing these?_

Answer:
I am not sure I got here the question right, but I guess you are referring to creating Modules which are standalone: https://www.playframework.com/documentation/2.6.x/ScalaDependencyInjection
This allows you to make modules and services independent, not coupled to each other. Being a FP language Scala we should follow similar to the video you sent me from 
ScalaDays (Effective Scala) something like an onion pattern where we have side-effects isolated at the edge in a thin layer and otherwise remain in a side-effect pure
area. In FP if we have pure functions the result is always the same so we could call them in any order, on any server and maintain the same output. Having separate Modules
makes this easier to implement. 

3) _Your batching logic for artist info will generate (n+1) queries for each movie. 
Currently the artistinfo service is artificially configured to allow only 5 ids, so that is the best you can do anyways. 
But what if it is not configured that way and it allowed 100 ids. Can you think about a more generic way of handling batching? 
Which may also be towards implementing paging solution._   

Answer: 
In this case there should be a BatchingHandler class that could generically handle in isolation batching. This should be hooked up with an independent service class, e.g.
ArtistInfo and should be able to:
- find out based on ArtistInfo ids needed how many calls we need to do (divide number of ids we need to fetch to the max batch size and the rest either make a batch
request with the rest or if 1 single item remaining do a single call) 
- this will produce something like: Seq[Future[CallResponse]]
- reduce to a Future[Seq[CallResponse]] e.g. like I do using Future.sequence 
- handle errors

From the outside (aggregator or controller) we should not be conserned with these details. We should just have a for comprehension for example that aggregates 
the different services that are composed. 
Using CATS and refactoring the current implementation could be a good idea: https://typelevel.org/cats/datatypes/eithert.html.
In this way the nice thing is we could handle errors & future in one go, but would need a lot of refactoring based on the current Vanilla Scala approach. 

4) _Average and median are not the same things. Your belowMedian uses an average instead of median. And yes it makes this significantly harder ;)_

Answer:
Yes, ok. I totally misunderstood it here and yes it is much simpler if it is just an average.
I have looked it up on wiki: https://en.wikipedia.org/wiki/Median. I understand what it means. 
The approach to tackle this one using Redis would be:
- one item comes from the firehose we look it up in the Redis store
- if we find it we do the average rating between this value and the new one
- store it to Redis as a list
- implement a new method for `median` in Redis which will find the median value and compare this value with the rating of the currently queried movie 
thus calculating the "isBellowMedianRating" property
I have inspired for my solution from this article: https://luca3m.me/2014/05/27/median-filter-redis.html. Of course there are differences as I described before, plus
we would look at the value (rating), not key (movieId) to calculate the median.

5) _You should check out the O complexity of some of the maps, inner maps etc. you use. For example those in the RatingService and when it is called from the controller._ 

Answer: 
I guess in the latest version of the code the situation has improved since first evaluated. 

Useful link: 
--------------
1. Play framework: https://www.playframework.com/documentation/2.6.x/Home
2. Circe: https://circe.github.io/circe/ 
3. Akka-Streams from Play: https://www.playframework.com/documentation/2.6.x/ScalaWS#directly-creating-wsclient 
4. Exponential back-off strategy with Scala Futures: https://hackernoon.com/exponential-back-off-with-scala-futures-7426340d0069