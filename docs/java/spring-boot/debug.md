
### Spring Boot requests: to “re-run your application with 'debug' enabled” - how do I?

add properties to application.properties(or yml)

```
debug=true
```

and you can change log level

```
logging.level.<package_name>=<LOGGING_LEVEL>

# e.g.
logging.level.org.springframework.context=DEBUG
```

### reference

https://stackoverflow.com/questions/51225325/spring-boot-requests-to-re-run-your-application-with-debug-enabled-how-do/51225501

