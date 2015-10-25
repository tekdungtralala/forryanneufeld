#### create two databases

one db for real application, the other one for unit test

the database name is data_collector, data_collector_test

test app

    mvn test -DDB_URL=jdbc:mysql://localhost:3306/data_collector_test -DDB_USERNAME=root -DDB_PASSWORD=password -DPORT=9009
run app

    mvn clean spring-boot:run -Drun.jvmArguments="-DDB_URL=jdbc:mysql://localhost:3306/data_collector -DDB_USERNAME=root -DDB_PASSWORD=password -DPORT=8080 -Dspring.profiles.active=prod"
