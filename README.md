# Tenpo Transactions - Fullstack Challenge

Aplicacion web para registrar y administrar transacciones de Tenpistas.

## Tecnologias

| Componente | Tecnologia | Version |
|------------|------------|---------|
| Backend | Spring Boot | 3.2.2 |
| Base de Datos | PostgreSQL | 15 |
| Frontend | React + Vite | 18 / 5 |
| Lenguaje Frontend | TypeScript | 5.x |
| HTTP Client | Axios | 1.6.x |
| Contenedores | Docker Compose | 3.8 |

## Requisitos Previos

- Docker Desktop instalado y ejecutandose
- Docker Compose v2.x o superior

## Ejecucion Rapida

```bash
# Clonar o descargar el proyecto
cd tenpo-challenge

# Construir y levantar los servicios
docker-compose build
docker-compose up -d

# Verificar que los contenedores estan corriendo
docker ps
```

## Acceso a la Aplicacion

| Servicio | URL |
|----------|-----|
| Frontend | http://localhost:3000 |
| Backend API | http://localhost:8080/api |
| PostgreSQL | localhost:5432 |

## API Endpoints

### Transacciones

| Metodo | Endpoint | Descripcion |
|--------|----------|-------------|
| GET | `/api/transactions` | Obtener todas las transacciones |
| GET | `/api/transactions/{id}` | Obtener una transaccion por ID |
| POST | `/api/transactions` | Crear nueva transaccion |
| DELETE | `/api/transactions/{id}` | Eliminar transaccion |

### Ejemplos de Uso

**Crear transaccion:**
```bash
curl -X POST http://localhost:8080/api/transactions \
  -H "Content-Type: application/json" \
  -d '{
    "amount": 15000,
    "merchantName": "Supermercado Lider",
    "tenpistName": "Juan Perez",
    "transactionDate": "2024-02-06T10:30:00"
  }'
```

**Obtener todas las transacciones:**
```bash
curl http://localhost:8080/api/transactions
```

**Eliminar transaccion:**
```bash
curl -X DELETE http://localhost:8080/api/transactions/1
```

## Estructura del Proyecto

```
tenpo-challenge/
├── docker-compose.yml
├── README.md
├── backend/
│   ├── Dockerfile
│   ├── pom.xml
│   └── src/
│       ├── main/java/com/tenpo/transactions/
│       │   ├── config/
│       │   ├── controller/
│       │   ├── dto/
│       │   ├── entity/
│       │   ├── exception/
│       │   ├── repository/
│       │   └── service/
│       └── test/
├── frontend/
│   ├── Dockerfile
│   ├── nginx.conf
│   └── src/
│       ├── api/
│       ├── components/
│       ├── types/
│       └── utils/
└── database/
    └── init.sql
```

## Validaciones

### Backend
- Monto no puede ser negativo
- Fecha de transaccion no puede ser futura
- Campos obligatorios: monto, comercio, tenpista, fecha

### Frontend
- Validacion en tiempo real con Zod
- Selector de fecha limitado a fecha actual
- Formateo de montos en pesos chilenos

## Comandos Utiles

```bash
# Ver logs de todos los servicios
docker-compose logs -f

# Ver logs de un servicio especifico
docker-compose logs -f backend
docker-compose logs -f frontend
docker-compose logs -f postgres

# Detener los servicios
docker-compose down

# Detener y eliminar volumenes (borra datos)
docker-compose down -v

# Reconstruir un servicio especifico
docker-compose build backend
docker-compose up -d backend
```

## Pruebas Unitarias

Las pruebas del backend se ejecutan automaticamente durante el build.

Para ejecutar manualmente:
```bash
cd backend
./mvnw test
```

## Variables de Entorno

El proyecto utiliza las siguientes variables configurables:

| Variable | Valor por Defecto | Servicio |
|----------|-------------------|----------|
| POSTGRES_DB | tenpo_db | PostgreSQL |
| POSTGRES_USER | tenpo_user | PostgreSQL |
| POSTGRES_PASSWORD | tenpo_pass_2024 | PostgreSQL |
| SPRING_DATASOURCE_URL | jdbc:postgresql://postgres:5432/tenpo_db | Backend |

## Arquitectura

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│    FRONTEND     │────▶│     BACKEND     │────▶│   POSTGRESQL    │
│   React/Vite    │     │   Spring Boot   │     │                 │
│   Port: 3000    │     │   Port: 8080    │     │   Port: 5432    │
└─────────────────┘     └─────────────────┘     └─────────────────┘
```

## Autor

Desarrollado como parte del TL Fullstack Challenge de Tenpo.
