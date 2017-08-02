# play-example

An example of [Play Framework 2.6.x](https://www.playframework.com/) framework usage for building a REST service with Java 8.
The service itself represents simple API for books catalog.

# What's implemented
- routing
- asynchronous controllers
- marshaling/unmarshalling using Jackson including Views and Serializers
- validation using JSR 303
- persistence layer using in-memory H2 and Ebean ORM including optimistic locking and soft-delete
- database evolution scripts
- testing with JUnit

 ### Usage by getting the code from github
Running from project directory (if sbt already installed):
 ```sbtshell
 sbt
 > run
  ```
 or (Java 8 JDK must be installed)
 ```sbtshell
 $ ./sbt
 > run
  ```
 or (for Win)
 ```
 sbt
 > run
  ```
  
Then you'll see something like 
```sbtshell

--- (Running the application, auto-reloading is enabled) ---

[info] p.c.s.AkkaHttpServer - Listening for HTTP on /0:0:0:0:0:0:0:0:9000

(Server started, use Enter to stop and go back to the console...)

```

Now you can open browser at [localhost:9000](http://localhost:9000/) and apply evolution script for database.

Then you can request books by genre id:

```sbtshell
curl http://localhost:9000/v1/genres/f022820c-cb84-4981-9184-46b6f9a17de8/books?offset=0
```
=> 
```json
[
    {
        "id": "0ae2a56c-5dd4-47ed-b230-690c11a786be",
        "name": "Alice's Adventures in Wonderland",
        "year": 1865,
        "edition": 1,
        "author": {
            "id": "bdd6741d-71b9-493c-9c31-38af19b9e27c",
            "lastName": "Dodgson",
            "firstName": "Charles"
        }
    }
]
```
Or create new book:
```sbtshell
curl \
 -H "Content-Type: application/json" \
 -X POST \
 -d '{"name":"The Hunting of the Snark","author":{"id":"bdd6741d-71b9-493c-9c31-38af19b9e27c"},"year":1876,"edition":1,"genreCollection":[{"id":"f022820c-cb84-4981-9184-46b6f9a17de8"}]}' \
 http://localhost:9000/v1/books
```
=> 
```json
{
    "id": "f5ce84de-6e18-4c88-b160-be749b10ba5d",
    "whenCreated": "2017-08-03T01:04:44.254+03:00"
}
```
or update author:
```sbtshell
curl \
 -H "Content-Type: application/json" \
 -X PUT \
 -d '{"lastName":"Carroll","firstName":"Lewis","middleName":"Lutwidge"}' \
 http://localhost:9000/v1/authors/bdd6741d-71b9-493c-9c31-38af19b9e27c
```
=>
```json
{
    "id": "bdd6741d-71b9-493c-9c31-38af19b9e27c",
    "whenCreated": "2017-08-03T00:59:16.413+03:00",
    "whenUpdated": "2017-08-03T01:14:13.127+03:00"
}

```

### Testing
```sbtshell
sbt
> test
```
=>
```sbtshell
[info] Test run started
[info] Test RepositoryTest.saveBook started
[info] application - Creating Pool for datasource 'default'
...
```

### Have fun!