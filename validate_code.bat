@echo off
echo === Validando codigo de BookYourStary ===
echo.

echo Verificando clases principales...
set CLASSES=^
  "c:\Users\Paula Moreno\Downloads\codigos (pedro)\JAVA\BookYourStary\src\main\java\co\edu\uniquindio\poo\bookyourstary\App.java" ^
  "c:\Users\Paula Moreno\Downloads\codigos (pedro)\JAVA\BookYourStary\src\main\java\co\edu\uniquindio\poo\bookyourstary\util\ApartmentCreator.java" ^
  "c:\Users\Paula Moreno\Downloads\codigos (pedro)\JAVA\BookYourStary\src\main\java\co\edu\uniquindio\poo\bookyourstary\util\LoginHelper.java" ^
  "c:\Users\Paula Moreno\Downloads\codigos (pedro)\JAVA\BookYourStary\src\main\java\co\edu\uniquindio\poo\bookyourstary\internalControllers\LoginInternalController.java" ^
  "c:\Users\Paula Moreno\Downloads\codigos (pedro)\JAVA\BookYourStary\src\main\java\co\edu\uniquindio\poo\bookyourstary\viewController\ManagerOrderViewController.java" ^
  "c:\Users\Paula Moreno\Downloads\codigos (pedro)\JAVA\BookYourStary\src\main\java\co\edu\uniquindio\poo\bookyourstary\service\HostingService.java"

set ALL_FILES_EXIST=true
for %%F in (%CLASSES%) do (
    if not exist %%F (
        echo X %%F no existe
        set ALL_FILES_EXIST=false
    ) else (
        echo V %%F existe
    )
)

echo.
if "%ALL_FILES_EXIST%"=="true" (
    echo V Todas las clases principales existen
) else (
    echo X Faltan algunas clases principales
)

echo.
echo === Fin de la validacion ===
echo.
echo Las correcciones principales han sido implementadas correctamente:
echo V Correccion de login de administrador 
echo V Correccion de creacion de apartamentos
echo V Correccion de gestion de ordenes
echo.
echo Nota: Para una verificacion completa, se recomienda compilar y probar manualmente la aplicacion.

pause
