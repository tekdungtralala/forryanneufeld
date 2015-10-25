#### create two databases

one db for real application, the other one for unit test

the database name is data_collector, data_collector_test

#### edit app config file 

src/resources/application.yml base on your configuration

you must only changes some values below, please dont change any other place

    url: jdbc:mysql://localhost:3306/data_collector
    username: root
    password: password
    server:
      port: 8080


#### edit test config file 

test/resources/application.yml base on your configuration

you must only changes some values below, please dont change any other place

    url: jdbc:mysql://localhost:3306/data_collector_test
    username: root
    password: password

test app

    mvn clean; mvn package;
run app

    mvn clean; mvn spring-boot:run -Drun.jvmArguments="-Dspring.profiles.active=prod";
