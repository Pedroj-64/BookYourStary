@echo off
echo ===========================================
echo Compilando BookYourStary
echo ===========================================

REM Ubicación del proyecto
set PROJECT_DIR=c:\Users\Paula Moreno\Downloads\codigos (pedro)\JAVA\BookYourStary

cd "%PROJECT_DIR%"

REM Limpiar proyecto
echo.
echo Limpiando proyecto...
call mvn clean

REM Compilar proyecto
echo.
echo Compilando proyecto...
call mvn compile

REM Comprobar si la compilación fue exitosa
if %ERRORLEVEL% NEQ 0 (
  echo.
  echo [ERROR] La compilación falló. Corrige los errores y vuelve a intentarlo.
  pause
  exit /b 1
) else (
  echo.
  echo [ÉXITO] La compilación fue exitosa.
  pause
)
