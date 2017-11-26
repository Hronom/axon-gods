# axon-gods
Implementation of logic 
> The point being that gods exist because we believe in them

and

> Power of the God grows up with amount of believers

Contains two implemntations:
1. **axon-gods-with-event-sourcing** - raw implementation with Event sourcing
2. **axon-gods-without-event-sourcing** - implementation with generic repositories

Both implementation use MongoDB as backend storage.

More information in post [Axon Gods](https://medium.com/@Hronom/axon-gods-496525cfd2e) 

## How to run
Use `docker-compose.yml` file and execute in the project directory: 
```
docker-compose up -d
``` 

Useful URL's:
* Swagger UI [http://localhost:8080/swagger-ui.html#!/](http://localhost:8080/swagger-ui.html#!/)
* MongoDB Express: [http://localhost:8079/](http://localhost:8079/)