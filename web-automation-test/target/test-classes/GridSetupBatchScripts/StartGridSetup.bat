SET TempFile=%CD%\GridSetupDetails.txt
for /f "tokens=1* delims==" %%G in ('type "%TempFile%"') do set %%G=%%H
SET GRIDHUBIP=%GridHub%& SET GRIDPORT=%GridNodePort%
SET WD_PORT=4444
SET SSS=selenium-server-standalone-3.141.59.jar
SET CHROME_DRIVER_FILE_NAME=chromedriver.exe
SET FIREFOX_DRIVER_FILE_NAME=geckodriver.exe
SET IE_DRIVER_FILE_NAME=IEDriverServer.exe
SET THIS_FOLDER_PATH=%CD%

cd %THIS_FOLDER_PATH%
start cmd /k "TriggerGridHub.bat"
TIMEOUT /T 5
call "TriggerGridNode.bat"
pause