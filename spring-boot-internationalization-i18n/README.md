# Spring Boot with i18n Internationalization
In the age of globalization, software applications are no longer limited by geographical boundaries.<br/>
As businesses expand their horizons, it becomes imperative for software developers to ensure their applications are accessible and user-friendly for audiences around the world.<br/>
This is where Internationalization (i18n) plays a pivotal role.

<b>Author:</b> <a href="https://github.com/springterest" target="_blank">springterest</a><br>
<b>Created:</b> 2024-09-27<br>
<b>Last updated:</b> 2024-09-27

[![](https://img.shields.io/badge/Spring%20Boot-8A2BE2)]() [![](https://img.shields.io/badge/release-May%2022,%202025-blue)]() [![](https://img.shields.io/badge/version-3.5.0-blue)]()

## 1. Why Internationalization Matters?

Internationalization is the process of designing and developing software applications that can be adapted to various languages, regions, and cultures without engineering changes.<br/>
While it may seem like an added complexity, the benefits are substantial:

1. **Expanded Market Reach:** By localizing your application, you open doors to new markets and audiences, thereby increasing your user base and potential revenue streams.

2. **Enhanced User Experience:** Users feel more comfortable and engaged when they interact with applications in their native language. It fosters trust and loyalty, leading to higher user retention rates.

3. **Compliance and Accessibility:** In many regions, providing applications in local languages is a legal requirement. By adhering to internationalization standards, you ensure compliance and accessibility for all users.

## 2. How an i18n Internationalization Implementation Works in Spring Boot?

Spring Boot simplifies the process of internationalization through its built-in support for message bundles and locale resolution. Here's how it works:

### 2.1 Create a Spring Boot Application

For this basic example we will start with a simple REST endpoint which we can call to see the i18n implementation at work.<br>
Lets create an application using the dependencies as previewed:

![01-start-spring-io](https://github.com/springterest/spring/spring-boot-internationalization-i18n/blob/main/images/01-start-spring-io.jpg)

[![](https://img.shields.io/badge/Lombok-8A2BE2)]()
Because it is just that easy to use.
Want to know more about <b>Project Lombok</b>? [Click this link](https://projectlombok.org/features/)

[![](https://img.shields.io/badge/Spring%20Web-8A2BE2)]()
This Spring Framework dependency will provide us with all the necessary functionality to create and manage our REST endpoints.

### 2.2 Configure i18n Internationalization

Now that the Spring Boot application is created, it is time to configure i18n Internationalization.<br/>
Within your `src/main/resources/application.yml` file, setup the following configuration:

```yml
spring:
  messages:
    basename: i18n/messages
    encoding: UTF-8
    cache-duration: 60
    fallback-to-system-locale: false
    logging-language: en
```

*If you are using a application.properties file, the configuration is as follows:*

```properties
spring.messages.basename=i18n/messages
spring.messages.encoding=UTF-8
spring.messages.cache-duration=60
spring.messages.fallback-to-system-locale=false
spring.messages.logging-language=en
```

The `basename` points to the folder where the **messages bundle** files are located.<br/>
The folder must always be located within folder: `src/main/resources` <br/>
`i18n` points to the folder, `messages` points to the name with which the bundle file names start.

Messages bundle files store the code and the value for our messages.

### 2.3 Create i18n Internationalization Messages Bundle Files

In the `src/main/resources` folder, create a folder named `i18n`

Here we will create the messages bundle files for our three languages: English, German, and Dutch.<br/>
English must be our standard language if no language tag is provided in the REST calls.

`messages.properties` = our standard language file, in this example containing the English messages.<br/>
`messages_de.properties` = containing the German messages.<br/>
`messages_nl.properties` = containing the Dutch messages.

![02-i18n-folder](https://github.com/springterest/spring/spring-boot-internationalization-i18n/blob/main/images/02-i18n-folder.jpg)

In each of the messages bundle files, we will configure six codes:
- `hello.world.log`
- `hello.world`
- `hello.name.log`
- `hello.name`
- `hello.multi.log`
- `hello.multi`

Example `messages.properties` file:
```properties
hello.world                 = Hello World! This is the English message.
hello.world.log             = DemoController hello endpoint logging message.

hello.name                  = Hello {0}
hello.name.log              = DemoController hello name endpoint logging message with name {}.

hello.multi                 = Hello {0}, you are {1} years old and you live in {2}
hello.multi.log             = DemoController hello multi endpoint logging message with name {}, age {} and city {}.
```

### 2.4 Create REST Endpoints

Now that <strong>i18n Internationalization</strong> is configured in our <strong>Spring Boot</strong> application, it is time to use the newly implemented funcationality.

For this, we will create some REST endpoints using the standard Spring Web implementation.

`DemoController.java`
```java
@RestController
@RequestMapping(value = "/api/v1")
@Slf4j
public record DemoController(I18nService i18nService) {

    @GetMapping("/hello")
    public String hello() {
        log.info(i18nService.getLogMessage("hello.world.log"));
        return i18nService.getMessage("hello.world");
    }

    @GetMapping("/hello/{name}")
    public String helloSingleArg(@PathVariable String name) {
        log.info(i18nService.getLogMessage("hello.name.log"), name);
        return i18nService.getMessage("hello.name", name);
    }

    @GetMapping("/hello/multi")
    public String helloMultiArg() {
        log.info(i18nService.getLogMessage("hello.multi.log"), "John", 30, "Oakland");
        return i18nService.getMessage("hello.multi", "John", String.valueOf(30), "Oakland");
    }
}
```

`@RestController` is a Spring Boot annotation, stating that this class handles our requests.<br/>
`@RequestMapping` is used to map requests to controller methods. In this case our endpoint starts with `/api/v1`<br/>
`@Slf4j` is the most commonly used logging annotation for Spring Boot applications.

### 2.5 The I18nService

Now the fun begins.<br/>
The `I18nService` is specifically designed to receive both `message codes` with and without arguments.<br/>
Apart from that, it automatically retrieves the `HttpServletRequest`, enabling the access to the user's request and retrieving their locale (local language).<br/>
It is a powerful and essential tool within our <strong>i18n Internationalization</strong> implementation.

`I18nService.java`
```java
@Service
@RequiredArgsConstructor
public class I18nService {

  private final MessageSource messageSource;

  private final HttpServletRequest request;

  // The logging language is set within the application.yml file
  @Value("${spring.messages.logging-language}")
  private String loggingLanguage;

  public String getLogMessage(String code) {
    return messageSource.getMessage(code, null, Locale.of(loggingLanguage));
  }

  public String getMessage(String code, @Nullable String... args) {
    return messageSource.getMessage(code, args, request.getLocale());
  }
}
```

`@Service` is a Spring Boot annotation, stating that this class handles our business logic.<br/>

### 2.6 The Methods Explained

#### 2.6.1 `getLogMessage(String code)`

- This method retrieves a log message using the provided <b>message code</b>.
- It utilizes the <b>MessageSource</b> bean to get the message based on the preferred <b>loggingLanguage</b> configured in the `application.yml` file.
- The <b>loggingLanguage</b> is retrieved from the `application.yml` file using the `@Value` annotation.
- It returns the localized log message corresponding to the given <b>message code</b>.
<br/><br/>

#### 2.6.2 `getMessage(String code, String... args)`

- This method retrieves a <b>message</b> using the provided <b>message code and arguments</b>.
- It also utilizes the <b>MessageSource</b> bean to get the message based on the <b>locale obtained from the HttpServletRequest object</b>.
- The method allows passing <b>additional arguments</b> to be formatted into the message if placeholders are defined in the <b>message code</b>.
- It returns the <b>localized message with arguments</b> replaced, corresponding to the given <b>message code</b>.

## 3. Spring Boot i18n Internationalization In Action 

- To test the <b>REST endpoints</b>, a tool like <b>Postman</b> can be used to send <b>HTTP GET requests</b>.
- A Postman collection is added within the repository `src/main/resources/postman/collection-to-import.json`
- When making requests, the `Accept-Language` header should be set to specify the desired language.
- You can use <b>language codes</b> like `en` for English, `de` for German, or `nl` for Dutch.
- Setting the `Accept-Language` header informs the server about the <b>preferred language for the response</b>, allowing the application to return <b>localized messages</b> accordingly.

[![](https://img.shields.io/badge/GET-green)]()<br/>
<small>Endpoint:</small> `http://localhost:8081/api/v1/hello`<br/>
<small>Header:</small> <b>Key</b> `Accept-Language` - <b>Value</b> `en`, `de` or `nl`

![03-postman-get-hello](https://github.com/springterest/spring/spring-boot-internationalization-i18n/blob/main/images/03-postman-get-hello.jpg)
<br><br>

[![](https://img.shields.io/badge/GET-green)]()<br/>
<small>Endpoint:</small> `http://localhost:8081/api/v1/hello/{name}`<br/>
<small>Header:</small> <b>Key</b> `Accept-Language` - <b>Value</b> `en`, `de` or `nl`

![04-postman-get-hello-name](https://github.com/springterest/spring/spring-boot-internationalization-i18n/blob/main/images/04-postman-get-hello-name.jpg)
<br><br>

[![](https://img.shields.io/badge/GET-green)]()<br/>
<small>Endpoint:</small> `http://localhost:8081/api/v1/hello/multi`<br/>
<small>Header:</small> <b>Key</b> `Accept-Language` - <b>Value</b> `en`, `de` or `nl`

![05-postman-get-hello-multi](https://github.com/springterest/spring/spring-boot-internationalization-i18n/blob/main/images/05-postman-get-hello-multi.jpg)
<br><br>

The logging messages are shown within the <strong>Spring Boot</strong> <b>application terminal</b>.

![06-i18n-internationalization-logging](https://github.com/springterest/spring/spring-boot-internationalization-i18n/blob/main/images/06-i18n-internationalization-logging.jpg)


## Let's Stay Connected

If you have any questions in regard to this repository and/or documentation, please do reach out.

Don't forget to:
- <b>Star</b> the [repository](https://github.com/springterest/spring/spring-boot-internationalization-i18n)
- [Follow me](https://github.com/springterest) for more interesting repositories!