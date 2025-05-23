@echo off
echo Iniciando BookYourStary...

REM Asegúrate de que el comando se ejecute desde el directorio del proyecto
cd /d "%~dp0"

REM Verificar si Maven está instalado
where mvn >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo Maven no está instalado. Utilizando mvnw...
    set MVN_CMD=.\mvnw
) else (
    set MVN_CMD=mvn
)

REM Limpiar y compilar el proyecto
echo Limpiando y compilando...
%MVN_CMD% clean compile
if %ERRORLEVEL% NEQ 0 (
    echo Error durante la compilación. Por favor revise los mensajes de error anteriores.
    pause
    exit /b 1
)

echo Compilación exitosa. Ejecutando la aplicación...
%MVN_CMD% exec:java -Dexec.mainClass="co.edu.uniquindio.poo.bookyourstary.App"

echo.
echo Aplicación terminada.
pause
