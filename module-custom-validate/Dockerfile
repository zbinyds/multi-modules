FROM openjdk:11

WORKDIR /app

COPY ./target/*.jar /app/app.jar

EXPOSE 7500

ENTRYPOINT ["java","-Dfile.encoding=UTF-8","-Duser.timezone=Asia/Shanghai","-jar","/app/app.jar"]

