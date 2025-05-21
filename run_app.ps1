# Ruta del proyecto
$PROJECT_DIR = "c:\Users\Paula Moreno\Downloads\codigos (pedro)\JAVA\BookYourStary"

Write-Host "==========================================="
Write-Host "Compilando y ejecutando BookYourStary"
Write-Host "==========================================="

# Cambiar al directorio del proyecto
Set-Location -Path $PROJECT_DIR

# Limpiar proyecto
Write-Host ""
Write-Host "Limpiando proyecto..." -ForegroundColor Cyan
mvn clean

# Compilar proyecto
Write-Host ""
Write-Host "Compilando proyecto..." -ForegroundColor Cyan
mvn compile

# Comprobar si la compilación fue exitosa
if ($LASTEXITCODE -ne 0) {
    Write-Host ""
    Write-Host "[ERROR] La compilación falló. Corrige los errores y vuelve a intentarlo." -ForegroundColor Red
    Read-Host "Presiona Enter para salir"
    exit 1
}

# Ejecutar proyecto
Write-Host ""
Write-Host "Ejecutando aplicación..." -ForegroundColor Green
mvn exec:java -Dexec.mainClass="co.edu.uniquindio.poo.bookyourstary.App" -Dexec.classpathScope=runtime

Write-Host ""
Write-Host "Aplicación terminada." -ForegroundColor Cyan
Read-Host "Presiona Enter para salir"
