FROM openjdk:11
RUN mkdir -p /tmp/apps
RUN mkdir -p /tmp/keystores
COPY target/mancala-api-0.0.1.jar /tmp/apps/mancala/
COPY target/*.p12 /tmp/keystores/
CMD java -Dlogging.path=/logs/ -Xmx512m -Xms256m \
                                        -jar /tmp/apps/mancala/mancala-api-0.0.1.jar \
                                        --server.ssl.key-store=file:/tmp/keystores/mancala-api-web-keystore.p12 \
                                        --server.ssl.trust-store=file:/tmp/keystores/mancala-api-truststore.p12
EXPOSE 8080
