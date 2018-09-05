REM -Dwebdriver.chrome.driver="chromedriver.exe" -Dwebdriver.ie.driver="IEDriverServer.exe" -Dwebdriver.gecko.driver="geckodriver.exe"

setlocal
cd /d %~dp0

java -Dwebdriver.chrome.driver="driver//chromedriver-v2.41-win32//chromedriver.exe"^
     -Dwebdriver.ie.driver="driver//IEDriverServer_x64_3.14.0//IEDriverServer.exe"^
     -Dwebdriver.gecko.driver="driver//geckodriver-v0.21.0-win64//geckodriver.exe"^
     -jar jar/selenium-server-standalone-3.14.0.jar -role node -nodeConfig json/nodeconfig.json

endlocal