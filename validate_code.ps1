# Script de validación para BookYourStary
Write-Host "=== Validando código de BookYourStary ===" -ForegroundColor Cyan

# Verificar que existan las clases principales
$classes = @(
    "c:\Users\Paula Moreno\Downloads\codigos (pedro)\JAVA\BookYourStary\src\main\java\co\edu\uniquindio\poo\bookyourstary\App.java",
    "c:\Users\Paula Moreno\Downloads\codigos (pedro)\JAVA\BookYourStary\src\main\java\co\edu\uniquindio\poo\bookyourstary\util\ApartmentCreator.java",
    "c:\Users\Paula Moreno\Downloads\codigos (pedro)\JAVA\BookYourStary\src\main\java\co\edu\uniquindio\poo\bookyourstary\util\LoginHelper.java",
    "c:\Users\Paula Moreno\Downloads\codigos (pedro)\JAVA\BookYourStary\src\main\java\co\edu\uniquindio\poo\bookyourstary\internalControllers\LoginInternalController.java",
    "c:\Users\Paula Moreno\Downloads\codigos (pedro)\JAVA\BookYourStary\src\main\java\co\edu\uniquindio\poo\bookyourstary\viewController\ManagerOrderViewController.java",
    "c:\Users\Paula Moreno\Downloads\codigos (pedro)\JAVA\BookYourStary\src\main\java\co\edu\uniquindio\poo\bookyourstary\service\HostingService.java"
)

$allFilesExist = $true
foreach ($file in $classes)
    if (Test-Path $file) {
        Write-Host "✓ $file existe" -ForegroundColor Green
    } else {
        Write-Host "✗ $file no existe" -ForegroundColor Red
        $allFilesExist = $false
    }
}

if ($allFilesExist) {
    Write-Host "`n✓ Todas las clases principales existen" -ForegroundColor Green
} else {
    Write-Host "`n✗ Faltan algunas clases principales" -ForegroundColor Red
}

# Verificar que tengamos las correcciones principales implementadas
Write-Host "`n=== Validando correcciones ===" -ForegroundColor Cyan

$correctionsFound = 0
$totalCorrections = 3

# Verificar corrección de login
$loginFile = Get-Content "c:\Users\Paula Moreno\Downloads\codigos (pedro)\JAVA\BookYourStary\src\main\java\co\edu\uniquindio\poo\bookyourstary\internalControllers\LoginInternalController.java" -ErrorAction SilentlyContinue
if ($loginFile -match "pepito@gmail.com" -and $loginFile -match "adminpass") {
    Write-Host "✓ Corrección de login de administrador implementada" -ForegroundColor Green
    $correctionsFound++
} else {
    Write-Host "✗ Corrección de login de administrador NO implementada" -ForegroundColor Red
}

# Verificar corrección de apartamentos
$apartmentFile = Get-Content "c:\Users\Paula Moreno\Downloads\codigos (pedro)\JAVA\BookYourStary\src\main\java\co\edu\uniquindio\poo\bookyourstary\util\ApartmentCreator.java" -ErrorAction SilentlyContinue
if ($apartmentFile -match "createApartments" -and $apartmentFile -match "MainController.getInstance\(\).getHostingService\(\).getApartamentRepository\(\).save") {
    Write-Host "✓ Corrección de creación de apartamentos implementada" -ForegroundColor Green
    $correctionsFound++
} else {
    Write-Host "✗ Corrección de creación de apartamentos NO implementada" -ForegroundColor Red
}

# Verificar corrección de ManageOrder
$manageOrderFile = Get-Content "c:\Users\Paula Moreno\Downloads\codigos (pedro)\JAVA\BookYourStary\src\main\java\co\edu\uniquindio\poo\bookyourstary\viewController\ManagerOrderViewController.java" -ErrorAction SilentlyContinue
if ($manageOrderFile -match "txt_numHuespedes.textProperty\(\).addListener" -and $manageOrderFile -match "Date_Fin.setValue\(LocalDate.now\(\).plusDays\(1\)\)") {
    Write-Host "✓ Corrección de gestión de órdenes implementada" -ForegroundColor Green
    $correctionsFound++
} else {
    Write-Host "✗ Corrección de gestión de órdenes NO implementada" -ForegroundColor Red
}

# Resumen
Write-Host "`n=== Resumen ===" -ForegroundColor Cyan
Write-Host "$correctionsFound de $totalCorrections correcciones implementadas" -ForegroundColor $(if ($correctionsFound -eq $totalCorrections) { "Green" } else { "Yellow" })

if ($correctionsFound -eq $totalCorrections) {
    Write-Host "`n✓ El código parece estar correctamente corregido" -ForegroundColor Green
} else {
    Write-Host "`n⚠ Hay correcciones que podrían no estar implementadas correctamente" -ForegroundColor Yellow
}
