FROM openjdk:11
EXPOSE  9085
WORKDIR /app
ADD   ./target/*.jar /app/wallet-bootcoin-service.jar
ENTRYPOINT ["java","-jar","/app/wallet-bootcoin-service.jar"] 