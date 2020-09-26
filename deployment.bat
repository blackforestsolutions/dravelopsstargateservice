@ECHO ON
echo %1
SET arg1=%1
SET PSScript=%~p1deployment.ps1

Powershell -ExecutionPolicy Bypass -Command "& '%PSScript%' '%arg1%'"
EXIT /B