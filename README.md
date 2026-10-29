# ACASA Space Academy: Sistema de Inscripciones con Patrones de Diseño

Actividad 10 – Patrones de Diseño (Creacional, Estructural y de Comportamiento).

## Descripción del caso de uso

Sistema de inscripciones de **ACASA Space Academy** (Observatorio Astronómico San Agustín, Cochabamba – Bolivia). Los estudiantes se inscriben en módulos de astronomía de nivel **Básico**, **Intermedio** o **Avanzado** según su grado. Si el grado no corresponde, la inscripción se rechaza. Cada módulo puede incluir opcionalmente un certificado, y al inscribir se avisa a Coordinación y a Tesorería.

Todo funciona por terminal, en un solo archivo: `AcasaPatrones.java`.

## Patrones de diseño aplicados

| Tipo | Patrón | Clases | Para qué se usa |
|---|---|---|---|
| Creacional | **Factory Method** | `ModuloFactory` | Crea el módulo según el nivel (`BASICO`, `INTERMEDIO`, `AVANZADO`). Quien lo necesita no usa `new` ni conoce los datos de cada módulo. |
| Estructural | **Decorator** | `Modulo`, `ModuloBase`, `ModuloConCertificado` | Agrega "Certificado" (título y +Bs 50) a cualquier módulo sin crear una clase por cada combinación ni modificar la original. |
| Comportamiento | **Observer** | `Inscripciones`, `Observador`, `Coordinacion`, `Tesoreria` | Al inscribir, `Inscripciones` avisa a todos sus observadores y cada uno reacciona a su manera, sin que `Inscripciones` los conozca. |

### 1. Factory Method (Creacional)
- **Problema:** crear módulos con `new` en varios lugares repite datos (grados, precios) y complica los cambios.
- **Solución:** `ModuloFactory.crear(nivel)` centraliza la creación. Si cambia un precio o aparece un nivel nuevo, se modifica solo la fábrica.

### 2. Decorator (Estructural)
- **Problema:** ofrecer el certificado en cada nivel obligaría a crear clases como `ModuloBasicoConCertificado`, `ModuloAvanzadoConCertificado`, etc.
- **Solución:** `ModuloConCertificado` envuelve cualquier `Modulo`, delega en él y le suma el título y el costo extra.

### 3. Observer (Comportamiento)
- **Problema:** al inscribir, varias áreas deben enterarse y actuar distinto.
- **Solución:** `Inscripciones` mantiene una lista de `Observador` y los notifica. `Coordinacion` registra o rechaza y `Tesoreria` cobra solo si la inscripción fue aceptada. Se pueden agregar áreas nuevas sin tocar `Inscripciones`.

## Estructura del proyecto

```
.
├── AcasaPatrones.java   # Código completo (interfaces, patrones y main)
└── README.md
```

## Requisitos

- Java JDK 11 o superior (`javac -version` y `java -version` deben mostrar una versión).

## Cómo compilar y ejecutar

```bash
javac AcasaPatrones.java
java AcasaPatrones
```

Si las tildes se ven como "?", ejecutar con:

```bash
java -Dfile.encoding=UTF-8 AcasaPatrones
```

## Salida esperada

```
Inscribiendo a Ana (2do primaria)...
  [Coordinacion] Registrado: Ana (2do primaria) en 'Exploradores del Espacio'
  [Tesoreria] Cobrar Bs 100.0 a Ana (2do primaria)
Inscribiendo a Luis (5to secundaria)...
  [Coordinacion] Registrado: Luis (5to secundaria) en 'Ingenieria de Sistemas Aeroespaciales + Certificado'
  [Tesoreria] Cobrar Bs 250.0 a Luis (5to secundaria)
Inscribiendo a Ana (2do primaria)...
  [Coordinacion] Rechazado: Ana (2do primaria) no cumple el grado de 'Ingenieria de Sistemas Aeroespaciales'
```

Luis paga Bs 250: Bs 200 del módulo Avanzado más Bs 50 del certificado. Ana es rechazada en el módulo Avanzado porque cursa 2do de primaria, así que Tesorería no cobra.

## Niveles y grados

Los grados se manejan del 1 al 12 (1 a 6 = primaria, 7 a 12 = secundaria).

| Nivel | Grados | Precio (Bs) |
|---|---|---|
| Básico | 1ro a 6to de primaria | 100 |
| Intermedio | 1ro a 4to de secundaria | 150 |
| Avanzado | 5to y 6to de secundaria | 200 |

## Autor

- Nombre: _(tu nombre completo)_
- Materia: _(nombre de la materia)_
- Docente: _(nombre del docente)_
- Universidad: Universidad Mayor de San Simón (UMSS)# acasa-patrones-diseno
