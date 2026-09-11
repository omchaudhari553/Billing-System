@echo off
setlocal
set JAVA_HOME=C:\Program Files\Java\jdk-17.0.18
set PATH=%JAVA_HOME%\bin;%PATH%
cd /d "H:\Billing System Project\Ajalkar-Billing-Website\backend"
mvnw.cmd spring-boot:run
endlocal
