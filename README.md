edit file src/resources/application.yml base on your configuration
you must only some values below
url: jdbc:mysql://localhost:3306/data_collector
username: root
password: password
server:
    port: 8080


edit file test/resources/application.yml base on your configuration
you must only some values below
url: jdbc:mysql://localhost:3306/data_collector
username: root
password: password

test app = mvn clean; mvn package;
run app  = mvn clean; mvn spring-boot:run -Drun.jvmArguments="-Dspring.profiles.active=prod";
