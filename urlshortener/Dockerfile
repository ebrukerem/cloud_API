FROM openjdk:8
EXPOSE 8080
ADD out/artifacts/urlshortener_jar /target
ADD out/artifacts/urlshortener_jar/urlshortener.jar target/urlshortener.jar
ENTRYPOINT ["java","-jar","/target/urlshortener.jar"]