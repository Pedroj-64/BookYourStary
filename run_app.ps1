Write-Host "==========================================="
Write-Host "Iniciando BookYourStary..."
Write-Host "==========================================="

# Verificar si Maven está instalado
$mvnCmd = if (Get-Command mvn -ErrorAction SilentlyContinue) {
    "mvn"
} else {
    Write-Host "Maven no está instalado. Utilizando mvnw..." -ForegroundColor Yellow
    ".\mvnw"
}

# Limpiar y compilar el proyecto
Write-Host ""
Write-Host "Limpiando y compilando..." -ForegroundColor Cyan
& $mvnCmd clean compile

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
