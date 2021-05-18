FROM java:8
Volume /tmp
ADD utopiauser-0.0.1-SNAPSHOT.jar app.jar
CMD ["/usr/bin/java" , "-jar", "app.jar"]
EXPOSE 8081
