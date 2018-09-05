REM java -jar selenium-server-standalone-3.14.0.jar -role hub
setlocal
cd /d %~dp0

java -jar jar/selenium-server-standalone-3.14.0.jar -role hub -hubConfig json/hubconfig.json

endlocal