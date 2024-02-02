@echo off
@REM set PROFILE=cloud
set PROFILE=local
set MONGO_ATLAS_URI=mongodb+srv://dbUser:!x4bwGf.5AYpxqK@cloudnativecluster.a3nahss.mongodb.net/?retryWrites=true&w=majority
set DATABASE_NAME=mydatabase
mvn spring-boot:run