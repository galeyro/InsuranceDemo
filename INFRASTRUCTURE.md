# Arquitectura e Infraestructura Local (InsuranceDemo)

Este documento detalla la configuración de la infraestructura local basada en Kubernetes (Minikube), la automatización de CI/CD, la integración de GitOps (FluxCD), y la exposición temporal a internet (Cloudflare Tunnel) empleada en este proyecto.

---

## 1. Componentes del Clúster (Minikube)

La infraestructura local se ejecuta sobre un clúster dedicado en Minikube utilizando el controlador/driver de Docker Desktop.

* **Perfil del Clúster:** `insurance-demo`
* **Especificaciones:** 4 vCPUs, 4GB RAM (mínimo recomendado para desarrollo local).

### Componentes Internos
Todos los servicios se encuentran instalados en el namespace `default`:

* **PostgreSQL:** Desplegado mediante Helm (Chart de Bitnami).
  * **Nombre del servicio:** `my-postgres-postgresql`
  * **Base de datos inicial:** `insurance_db` (creada automáticamente).
* **Apache Kafka (KRaft):** Desplegado mediante Helm en modo KRaft (sin ZooKeeper) para reducir el consumo de recursos.
  * **Nombre del servicio:** `my-kafka`
  * **Réplicas:** 1 nodo controlador/broker (escala reducida para desarrollo local).
  * **Seguridad:** Protocolo `PLAINTEXT` (autenticación deshabilitada para simplificar las pruebas locales).
* **Ingress Controller (Nginx):** Addon interno de Minikube activado para enrutamiento.

---

## 2. Puertos de Desarrollo y Acceso Local

Para evitar conflictos con otros servicios locales que se ejecutan en tu PC (como bases de datos u otros servidores en WSL), se configuró un reenvío de puertos dedicado:

| Servicio | Puerto en Contenedor | Puerto Local (Host) | URL / Acceso |
| :--- | :--- | :--- | :--- |
| **PostgreSQL** | `5432` | `5433` | `localhost:5433` |
| **Angular Frontend** | `80` | `8081` | `http://localhost:8081` |
| **Spring Boot API** | `8080` | `8082` | `http://localhost:8082` |

---

## 3. Script de Automatización de Desarrollo (Uso Local)

Para no abrir múltiples consolas, se creó un script unificado en la raíz del proyecto para abrir todos los túneles a la vez:

### Iniciar el Entorno Local
Ejecuta en PowerShell:
```powershell
.\run-dev.ps1
```
Este script limpia conexiones anteriores, levanta los reenvíos de puertos en segundo plano y se queda esperando.

### Detener el Entorno Local
* Presiona `Ctrl + C` en la terminal que ejecuta el script, o cierra la ventana de la terminal.
* *El script interceptará el cierre y destruirá los túneles automáticamente.*

---

## 4. Ciclo GitOps (FluxCD) y CI/CD

El proyecto implementa prácticas modernas de GitOps mediante **FluxCD v2**.

```mermaid
graph TD
    Developer[Desarrollador] -->|git push| GitHub[GitHub Repo]
    GitHub -->|GitHub Actions| GHCR[GitHub Container Registry]
    FluxCD[FluxCD en Minikube] -->|Vigila /k8s| GitHub
    FluxCD -->|Aplica cambios| Minikube[Pods del Clúster]
```

1. **Compilación (CI):** Un push a la rama `main` en carpetas específicas (`Insurance-Backend/**` o `insurance-frontend/**`) activa el workflow en GitHub Actions que construye las imágenes Docker y las publica en **GitHub Container Registry (GHCR)** con visibilidad pública.
2. **Sincronización (CD):** **FluxCD** vigila la carpeta `/k8s` del repositorio remoto y aplica automáticamente cualquier cambio de configuración en el clúster de Minikube sin intervención manual.

---

## 5. Exposición a Internet para Demos (Cloudflare Tunnel)

Para mostrar una demo en vivo de la aplicación (frontend + backend) desde tu PC local a cualquier dispositivo con internet (como un móvil u otro cliente), utilizamos los túneles rápidos de Cloudflare.

### Paso 1: Iniciar el puente de red de Minikube para Ingress
Debido a que en Windows la IP de Minikube no es accesible directamente por Cloudflare, exponemos el controlador de Ingress en un puerto local de tu PC:
```powershell
minikube service ingress-nginx-controller -n ingress-nginx -p insurance-demo --url
```
*Este comando se quedará corriendo en segundo plano y te dará una salida como:*
`http://127.0.0.1:51802` (el puerto final puede variar en cada inicio).

### Paso 2: Lanzar el túnel de Cloudflare
Abre otra terminal y ejecuta el túnel apuntando al puerto que te dio el comando anterior:
```powershell
# Reemplaza 51802 por el puerto real que te dio el paso 1
cloudflared tunnel --url http://127.0.0.1:51802
```
*Copia la URL `https://xxxx.trycloudflare.com` que saldrá en consola y compártela.*

### Paso 3: Apagar la Demo
* Presiona `Ctrl + C` en ambas consolas (la de `minikube service` y la de `cloudflared`). La URL pública dejará de funcionar inmediatamente.

---

## 6. Guía Rápida: Cómo apagar y encender TODO desde cero

### Cómo Apagar Todo (Fin de jornada)
1. Si tienes el script `run-dev.ps1` o la demo de Cloudflare abiertos, ciérralos con `Ctrl + C` o cierra las terminales.
2. Apaga el clúster de Minikube:
   ```powershell
   minikube stop -p insurance-demo
   ```
3. Cierra Docker Desktop.

### Cómo Encender Todo de nuevo
1. Abre **Docker Desktop**.
2. Enciende el clúster de Kubernetes:
   ```powershell
   minikube start -p insurance-demo
   ```
3. Verifica que todo esté sano (`Running`):
   ```powershell
   kubectl get pods
   ```
4. **Si vas a programar en local:** Ejecuta `.\run-dev.ps1`.
5. **Si vas a hacer una demo a internet:** Sigue los pasos de la **Sección 5** (Cloudflare Tunnel).
