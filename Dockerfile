FROM openjdk:17-jdk-slim
VOLUME /tmp
ADD target/EmployeeManagementSystemApi-0.0.1-SNAPSHOT.jar EmployeeManagementSystemApi-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/EmployeeManagementSystemApi-0.0.1-SNAPSHOT.jar"]