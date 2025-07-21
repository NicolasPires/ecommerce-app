FROM azul/zulu-openjdk:21
VOLUME /tmp
COPY target/ecommerce.jar ecommerce.jar
ENTRYPOINT ["java", "-jar", "/ecommerce.jar"]