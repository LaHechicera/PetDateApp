# 🧾 Proyecto: PetDateApp

Este repositorio contiene el desarrollo técnico del sistema de la aplicación móvil PetDate App, como parte de las evaluaciones del 2do semetre del 2025 de la asignatura **Desarrollo de Aplicaciones Móviles**.

**Para descargar APK seguir siguiente ruta en repositorio**

- **PetDateApp\app\release\app-release.apk.**
## 📦 Descripción General del Proyecto

PetDate App es una aplicación móvil desarrollada para Android, cuyo propósito es facilitar la administración, seguimiento y registro de mascotas por parte de los usuarios. 

La plataforma integra una arquitectura cliente–servidor, donde la aplicación móvil actúa como frontend y se conecta a un microservicio independiente encargado de gestionar la información de mascotas, alojado en la nube y respaldado por una base de datos NoSQL.


## 🧩 Arquitectura de Microservicio

> 📝 La estructura del sistema está conformado por un microservicio 'mascota-ms', el que fue desplegado en Render y esta enlazado con una base de datos no relacional en MongoDB Atlas.

La arquitectura del servicio de 'mascota-ms' y su arquitectura:
```
├── mascota-ms/📦
│       └── 📂controller
│           └── PetController.java
│       └── 📂model
│           └── Pet.java
│       └── 📂repository
│           └── PetRepository.java
│       └── 📂service
│           └── PedidoService.java
└── 
```
### Microservicios Desarrollados

- `mascota-ms`: > 📝 Agrega, modifica y elimina mascotas.

## 🛠️ Arquitectura general

El proyecto utiliza una arquitectura modular y desacoplada, dividida en:

✔ **Aplicación Móvil (Android – Kotlin)**

- Kotlin
- Android Jetpack Compose
- MVVM (Model–View–ViewModel)
- Room / DataStore para persistencia local
- Retrofit para consumo del microservicio
- Navigation Compose
- Theming dinámico (modo oscuro/claro)

✔ **Microservicio de Mascotas (Spring Boot – Java)**

- Spring Boot
- MongoDB Atlas
- Repositorios JPA para Mongo
- Controladores REST
- Swagger / OpenAPI
- Dockerfile – Multistage Maven
- Despliegue en Render

✔ **Framework:**

- Androidstudio

## 🗄️ Configuración de Bases de Datos

La Base de Datos se realizo con MongoDB Atlas, el cual nos permitio crear un cluster en la nube de manera gratuita que va guardando en tiempo real los datos subidos al microservicio. La configuracion de las conexiones se realizo mediante el archivo `application.properties` de la siguiente manera:
```
spring.application.name=mascota-ms

# Puerto local
server.port=8080

# URI de MongoDB Atlas
spring.mongodb.uri=mongodb+srv://DB_MascotasMs:----@mascotams.nlsdzph.mongodb.net/?appName=MascotaMs

# Nombre de la base de datos
spring.mongodb.database=MascotaMs
```

## Definicion de Tablas y campos
Nuestro microservicio cuenta con una tabla en la base de datos donde se almacenan los datos requeridos.

- **Microservicio mascota-ms:** este contiene la tabla mascota, en la cual se deben rellenar los datos de 'ownerId' (se crea en base al correo de inicio de sesion para enlazar a la mascota a ese correo en especifico a traves de la base de datos local), 'name', 'imageURL' (este guarda el link de la imagen que seleccionamos desde el dispositivo para luego ir a buscarla, no se almacena en la BD), 'animalType', 'weight', 'allergies', 'food' y 'bio'.

## 📮 Endpoints y Pruebas

Endpoints Microservicio mascota-ms
- GET: https://mascotams-backend.onrender.com/pets
- GET all pets: https://mascotams-backend.onrender.com/pets/owner
- GET all pets from owner: https://mascotams-backend.onrender.com/pets/owner/{ownerId}
- GET pet from id: https://mascotams-backend.onrender.com/pets/{petId}
- POST: https://mascotams-backend.onrender.com/pets 
- DELETE: https://mascotams-backend.onrender.com/pets/delete/{petId}

**Para hacer un POST**
```
{
"ownerId": "campo@gmail.com",
"name": "Kida",
"imageUrl": "Aun no se sube imagen",
"animalType": "Gato",
"weight": 4,
"allergies": "Acaros de otros gatos",
"food": "Taste of the wild",
"bio": "Le gusta la jalea y el yogurt"
}
```

## 🧑‍💻 Integrantes del Equipo

| **Nombre**                  | **Rol en el proyecto** |
|-------------------------|-------------------|
| Camila González | Frontend y Backend |
| Andy Villarroel | Frontend          |


## 📂 Estructura del Repositorio

El repositorio cuenta con un unico proyecto, con sus correspondientes carpeta y README.

A continuacion se observa como se ve el repositorio.
```
📦 PetDateAPP
├── 📂 .idea
├── 📂 app (aqui se encuentra el proyecto)
├── 📂 gradle
├── .gitignore
├── build.gradle.kts
├── gradle.properties
├── gradlew
├── gradlew.bat
├── settings.gradle.kts
└── README.md
```

## 👥 Colaboración en GitHub

Primero que todo se realizo la creación del repositorio en GitHub y se crearon las ramas `main` que contribuyo principalmete a los Pull Request, las ramas `andy` y `cami` las que trabajaron tanto en el frontend, backend y base de datos.

Como equipo consideramos que la mejor manera de trabajar colaborativamente fue la comunicacion, los que nos ayudo a coordinar commits frecuentemente cada vez que se realizaba avance.

Se realizaron `push` constantes por parte de los colaboradores, para mantener lo más actualizado el repositorio y la rama `master`.
