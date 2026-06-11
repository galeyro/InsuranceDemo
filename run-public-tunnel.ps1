Write-Host "==================================================" -ForegroundColor Green
Write-Host "   Iniciando Infraestructura y Túnel Público      " -ForegroundColor Green
Write-Host "==================================================" -ForegroundColor Green

# 1. Verificar/Iniciar Minikube
Write-Host "Verificando estado de Minikube..." -ForegroundColor Yellow
$status = minikube status -p insurance-demo --format "{{.Host}}" 2>$null
if ($status -ne "Running") {
    Write-Host "Minikube no está corriendo. Iniciando perfil 'insurance-demo'..." -ForegroundColor Yellow
    minikube start -p insurance-demo --memory=4096 --cpus=4
} else {
    Write-Host "Minikube ya se encuentra en ejecución." -ForegroundColor Green
}

# 2. Habilitar Ingress
Write-Host "`nHabilitando Addon de Ingress..." -ForegroundColor Yellow
minikube addons enable ingress -p insurance-demo

# 3. Aplicar Manifiestos de Kubernetes
Write-Host "`nAplicando manifiestos de Kubernetes para asegurar el estado..." -ForegroundColor Yellow
kubectl apply -f k8s/

# 4. Esperar a que la base de datos, backend y frontend se inicialicen
Write-Host "`nEsperando a que los componentes de la aplicación estén listos..." -ForegroundColor Yellow
Write-Host "(Esto puede tomar un momento si se están descargando imágenes nuevas)" -ForegroundColor Gray
kubectl rollout status deployment/insurance-backend --timeout=90s -n default 2>$null
kubectl rollout status deployment/insurance-frontend --timeout=90s -n default 2>$null

# 5. Esperar a que el Ingress Controller esté listo
Write-Host "`nEsperando a que el Ingress Controller de Kubernetes responda..." -ForegroundColor Yellow
kubectl wait --namespace ingress-nginx --for=condition=ready pod --selector=app.kubernetes.io/component=controller --timeout=90s -n ingress-nginx 2>$null

# 6. Levantar túnel interno de Ingress en Minikube (Background Process)
if (Test-Path "minikube-ingress-url.txt") {
    Remove-Item "minikube-ingress-url.txt" -Force -ErrorAction SilentlyContinue
}

Write-Host "`nExponiendo Ingress Controller localmente desde Minikube..." -ForegroundColor Yellow
$serviceJob = Start-Process -FilePath "minikube" -ArgumentList "service ingress-nginx-controller -n ingress-nginx -p insurance-demo --url" -RedirectStandardOutput "minikube-ingress-url.txt" -NoNewWindow -PassThru

# Polling para capturar la URL expuesta por Minikube
$ingressUrl = $null
$timeout = 30
while ($timeout -gt 0 -and -not $ingressUrl) {
    if (Test-Path "minikube-ingress-url.txt") {
        $content = Get-Content "minikube-ingress-url.txt" -Raw
        if ($content -match "(http://127.0.0.1:[0-9]+)") {
            $ingressUrl = $Matches[1]
            break
        }
    }
    Start-Sleep -Seconds 1
    $timeout--
}

if (-not $ingressUrl) {
    Write-Host "`n[Error] No se pudo obtener la URL del túnel de Ingress de Minikube." -ForegroundColor Red
    if ($serviceJob) {
        Stop-Process -Id $serviceJob.Id -Force -ErrorAction SilentlyContinue
    }
    exit 1
}

# 7. Lanzar Cloudflare Tunnel apuntando al Ingress de Minikube
Write-Host "`nIngress Controller expuesto localmente en: $ingressUrl" -ForegroundColor Cyan
Write-Host "==================================================" -ForegroundColor Green
Write-Host "   Exponiendo clúster a internet con Cloudflare   " -ForegroundColor Green
Write-Host "==================================================" -ForegroundColor Green
Write-Host "Copia la URL '.trycloudflare.com' que aparecerá abajo para acceder a tu Demo." -ForegroundColor Green
Write-Host "Presiona [CTRL + C] para cerrar el túnel y detener el script." -ForegroundColor Yellow
Write-Host "==================================================" -ForegroundColor Green

try {
    # Ejecutar cloudflared en primer plano para mostrar los logs y la URL de trycloudflare
    cloudflared tunnel --url $ingressUrl
}
finally {
    Write-Host "`n[!] Señal de apagado detectada. Deteniendo túneles y limpiando..." -ForegroundColor Yellow
    if ($serviceJob) {
        Stop-Process -Id $serviceJob.Id -Force -ErrorAction SilentlyContinue
    }
    if (Test-Path "minikube-ingress-url.txt") {
        Remove-Item "minikube-ingress-url.txt" -Force -ErrorAction SilentlyContinue
    }
    Write-Host "¡Todo cerrado de forma segura y limpia!" -ForegroundColor Green
}
