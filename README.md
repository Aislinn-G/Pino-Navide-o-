
 🎄 Pino Navideño 3D – Proyecto de Graficación con JOGL

Este repositorio contiene un proyecto desarrollado en **Java** utilizando la librería JOGL para la materia de Graficación.  
El objetivo principal es construir y renderizar un pino navideño 3D mediante OpenGL, aplicando conceptos de modelado, materiales, iluminación y transformaciones 3D.

---

Descripción General

El proyecto dibuja un árbol navideño en 3D formado por varios conos y una esfera superior que simula la estrella.  
Incluye configuración completa de:

- 🌲 Modelado por niveles (conos apilados).  
- 💡 Iluminación ambiente, difusa y especular.  
- 🟤 Materiales con brillo y reflexión.  
- 🧭 Ajustes de cámara.  
- 🌌 Fondo de escena simulando noche.

Este repositorio contiene todas las carpetas necesarias, incluyendo:
- Código fuente dentro de `/origen/pinonavideño/`
- Archivos de NetBeans
- Configuración del proyecto

---

 Tecnologías

- Java (JDK 8 o superior)
- JOGL (JOGAMP – OpenGL 2.0)
- IDE recomendado: NetBeans

---

Cómo Ejecutar el Proyecto

1. Clona el repositorio:

git clone https://github.com/Aislinn-G/Pino-Navide-o-

2. Abre el proyecto en NetBeans.

3. Configura las librerías JOGL:
   - `gluegen-rt.jar`
   - `jogl-all.jar`
   - Más las librerías nativas de tu sistema operativo.

4. Asegúrate de que la clase principal esté configurada como: PinoNavideño/src/Pino3DSimple.java

5. Ejecuta desde NetBeans.

---

 Controles 

Dependiendo de la clase base utilizada, la escena soporta:

- Rotación del árbol
- Zoom de cámara
- Movimiento de luz

---

Estructura del Repositorio

PinoNavideño/
│
├── origen/
│ └── pinonavideño/
│ └── Pino3DSimple.java
│
├── proyecto nb/
├── construir/ clases/
├── compilación.xml
├── manifiesto.mf
└── README.md


---

Autor

Proyecto realizado por **Génesis Aislinn González Martínez**  
Código base y guía académica: **Genaro Méndez López**
