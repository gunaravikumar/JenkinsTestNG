title TriggerGridNode
TIMEOUT /T 5
java -Dwebdriver.chrome.driver="%THIS_FOLDER_PATH%/Browser drivers/Chrome/%CHROME_DRIVER_FILE_NAME%" -Dwebdriver.gecko.driver="%THIS_FOLDER_PATH%/Browser drivers/Firefox/%FIREFOX_DRIVER_FILE_NAME%" -Dwebdriver.ie.driver="%THIS_FOLDER_PATH%/Browser drivers/InternetExplorer/%IE_DRIVER_FILE_NAME%" -jar %SSS% -role webdriver -hub %GRIDHUBIP%:%WD_PORT%/grid/register -port %GRIDPORT%
