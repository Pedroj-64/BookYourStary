@echo off
echo Ejecutando BookYourStary (modo simple)...

REM Asegúrate de que el comando se ejecute desde el directorio del proyecto
cd /d "%~dp0"

REM Ejecutar la aplicación directamente mediante maven
call mvn exec:java -Dexec.mainClass="co.edu.uniquindio.poo.bookyourstary.App"

echo.
echo Aplicación terminada.
pause
