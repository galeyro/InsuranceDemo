Write-Host "==================================================" -ForegroundColor Green
Write-Host "   Iniciando Entorno de Desarrollo (Port-Forward)   " -ForegroundColor Green
Write-Host "==================================================" -ForegroundColor Green

# 1. Detener jobs anteriores si existen para evitar conflictos
$oldJobs = Get-Job -Name "postgres-pf", "frontend-pf", "backend-pf", "kafka-pf" -ErrorAction SilentlyContinue
if ($oldJobs) {
    $oldJobs | Stop-Job | Remove-Job
}

# 2. Iniciar port-forwards en segundo plano (como Jobs de PowerShell)
Start-Job -Name "postgres-pf" -ScriptBlock { kubectl port-forward svc/my-postgres-postgresql 5433:5432 } | Out-Null
Start-Job -Name "frontend-pf" -ScriptBlock { kubectl port-forward svc/insurance-frontend-svc 8081:80 } | Out-Null
Start-Job -Name "backend-pf"  -ScriptBlock { kubectl port-forward svc/insurance-backend-svc 8082:8080 } | Out-Null
Start-Job -Name "kafka-pf"    -ScriptBlock { kubectl port-forward svc/my-kafka 9092:9092 } | Out-Null

# 3. Esperar un segundo y verificar estado
Start-Sleep -Seconds 2
$jobs = Get-Job -Name "postgres-pf", "frontend-pf", "backend-pf", "kafka-pf"

Write-Host "`nEstado de los túneles:" -ForegroundColor Cyan
foreach ($job in $jobs) {
    $statusColor = if ($job.State -eq "Running") { "Green" } else { "Red" }
    Write-Host " * $($job.Name): " -NoNewline
    Write-Host "$($job.State)" -ForegroundColor $statusColor
}

Write-Host "`nURLs de acceso:" -ForegroundColor Green
Write-Host " * PostgreSQL:        localhost:5433" -ForegroundColor Gray
Write-Host " * Apache Kafka:      localhost:9092" -ForegroundColor Gray
Write-Host " * Angular Frontend:  http://localhost:8081" -ForegroundColor Gray
Write-Host " * Spring Boot API:   http://localhost:8082" -ForegroundColor Gray

Write-Host "`n>>> PRESIONA [CTRL + C] PARA APAGAR TODOS LOS TÚNELES <<<" -ForegroundColor Yellow
Write-Host "==================================================" -ForegroundColor Green

# 4. Mantener la terminal abierta y capturar la interrupción (CTRL+C) para limpiar
try {
    while ($true) {
        Start-Sleep -Seconds 1
    }
}
finally {
    Write-Host "`n[!] Detectada señal de apagado. Deteniendo túneles..." -ForegroundColor Yellow
    $jobs | Stop-Job | Remove-Job
    Write-Host "¡Todos los túneles se han cerrado con éxito! Saliendo..." -ForegroundColor Green
}
