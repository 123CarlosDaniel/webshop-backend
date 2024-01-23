# Webshop Backend API
Api Rest para un webshop, cuenta con autenticación, protección de endpoints,
subida de archivos y almacenamiento en base de datos PostgreSQL.

## Requisitos 
- Java 17 o superior 
- PostgreSQL

## Configuracion 
### Base de Datos
La aplicación utiliza una base de datos H2 en desarrollo y PostgreSQL en producción.

### PostgreSQL - Variables de entorno

Estas variables sera usadas para definir las propiedades en el archivo
appication-prod.properties
- DB_NAME="db_name"
- DB_USERNAME="your_db_username"
- DB_PASSWORD="your_db_password"
- DB_HOST="your_db_host"
- DB_PORT=5432  
- STRIPE_API_KEY="your_stripe_key"
- JWT_SECRET_KEY=73ff0453aabc59894874205612f3d78594e383c147895e2f41a106da9183a213
- FRONTEND_URL="yoursite.com"

## Como ejecutar
De preferencia usar Intelij IDEA. Configurar el perfil de trabajo en el 
application.properties y configurar las variables de entorno si esta usando 
el perfil "prod". Ejecutar la clase principal BackendApplication.