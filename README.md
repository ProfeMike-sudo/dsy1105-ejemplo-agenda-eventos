# DSY1105 · Ejemplo: Agenda de Eventos (navegación + formularios validados + MVVM)

Ejemplo didáctico para **DSY1105 — Desarrollo de Aplicaciones Móviles** (Duoc UC, 2026-2).

> **Este proyecto es la continuación del que armaron en la Guía 08**, no uno nuevo. Sobre el mismo paquete `com.agenda.app` y la misma estructura (`model/`, `ui.components/`, `ui.screen/`, `ui.theme/`), hoy se agregan dos piezas: la **navegación** (`navigation/`) y el **ViewModel** (`viewmodel/`), más la pantalla de formulario. La `data class Evento` que ya definieron en la Guía 08 (`titulo`, `fecha`) se extiende con `lugar` y `cupos`, que el formulario de hoy necesita.

Demuestra, en un solo proyecto pequeño, los tres temas que se trabajan juntos en la Experiencia de Aprendizaje 2:

1. **Navegación** entre pantallas (`NavHost`, rutas con `sealed class`, menú inferior `NavigationBar`).
2. **Formularios validados** (Material 3: `OutlinedTextField`, `isError`, `supportingText`).
3. **Paso de información** entre pantallas con un **ViewModel compartido** + `StateFlow` (patrón **MVVM**).

La app es una **agenda de eventos**: en una pantalla completas un formulario validado y, al guardar, el evento aparece en la otra pantalla sin pasar argumentos por la navegación. Ese es el corazón de la guía.

---

## Requisitos

- Android Studio (Ladybug o superior).
- JDK 17+.
- SDK de Android `compileSdk 36` (el proyecto baja lo necesario con Gradle).
- Emulador o dispositivo con API 24+.

## Cómo ejecutar

```bash
# compilar y generar el APK de debug
./gradlew assembleDebug

# correr los tests unitarios
./gradlew testDebugUnitTest
```

O abre la carpeta en Android Studio y presiona **Run ▶**.

---

## Estructura del proyecto (y por qué)

```
app/src/main/java/com/agenda/app/
├── MainActivity.kt              → punto de entrada: solo llama a AppNavHost()
├── navigation/                  → (nuevo hoy) rutas + NavHost + menú
│   ├── Rutas.kt                 → rutas como sealed class (evita "strings sueltos")
│   └── AppNavHost.kt            → NavHost + NavigationBar + ViewModel compartido
├── model/
│   ├── Evento.kt                → la "forma" de un evento (data class)
│   └── FormularioEventoEstado.kt → estado del formulario + errores
├── viewmodel/                   → (nuevo hoy) la fuente de verdad
│   └── AgendaViewModel.kt       → estado + validación
└── ui/
    ├── components/              → piezas reutilizables (ya existía)
    │   └── TarjetaEvento.kt     → tarjeta que dibuja UN evento
    └── screen/                  → las pantallas (ya existía)
        ├── PantallaListaEventos.kt → pantalla 1: lista de eventos
        └── PantallaAgregarEvento.kt → pantalla 2: formulario validado (nueva hoy)
```

### Las tres capas de MVVM

| Capa | Carpeta | Qué hace | Qué NO hace |
|---|---|---|---|
| **Model** | `model/` | Describe la forma del dato (`Evento`, estado del formulario) | No valida ni guarda |
| **View** | `ui/screen/` + `ui/components/` + `navigation/` | Dibuja y responde a toques (`Composable`) | No tiene reglas de negocio |
| **ViewModel** | `viewmodel/` | Guarda el estado, valida, decide qué pasa con los datos | No dibuja ni conoce la UI |

**Regla de oro:** la pantalla (View) *observa* el estado y *avisa* acciones (`onValueChange`, `onClick`); el ViewModel *decide*. La validación vive en el ViewModel, nunca en el componente visual.

---

## Conceptos clave, en una frase

- **`StateFlow`**: un "valor que se puede observar". Cuando cambia, quien lo observa se entera solo.
- **`collectAsState()`**: puente entre `StateFlow` y Compose: convierte el flujo en estado de la UI y la redibuja automáticamente.
- **`_x` vs `x`** (ej. `_eventos` / `eventos`): `_x` es privado y mutable (lo cambia el ViewModel); `x` es público y de solo lectura (lo ve la pantalla).
- **`ViewModel` compartido**: se crea **una vez** en `AppNavHost` y se pasa por parámetro a ambas pantallas. Así comparten la misma lista. Si cada pantalla llamara a `viewModel()` por su cuenta, cada una tendría una copia distinta y no se verían los datos.
- **`sealed class` para rutas**: navegar con `Rutas.Lista.ruta` en vez de `"lista"`. El compilador valida las rutas y evita errores de tipeo.
- **Validación por campo**: `isError = true` + `supportingText` es lo que hace que Material 3 pinte el campo en rojo con el mensaje debajo.

---

## La validación (reglas de negocio)

En `AgendaViewModel.validarFormulario()`:

| Campo | Regla |
|---|---|
| Título | obligatorio (no vacío) |
| Fecha | obligatorio (no vacío) |
| Cupos | opcional; si se escribe, numérico y **≥ 0** |

El campo numérico (`cupos`) se guarda como texto mientras se escribe (para no interrumpir al usuario con "12." a medio escribir) y se convierte recién al validar.

---

## Tests

`app/src/test/java/.../viewmodel/AgendaViewModelTest.kt` prueba **solo la lógica** (sin emulador), porque el ViewModel es Kotlin puro:

- título vacío → error
- fecha vacía → error
- cupos negativo / no numérico → error
- formulario válido → se guarda y aparece en la lista
- formulario inválido → **no** se guarda
- cupos en blanco → se guarda como 0

```
./gradlew testDebugUnitTest   # 7 tests
```

---

## Cómo extenderlo (tarea de investigación)

Este ejemplo es la base para tu Parcial 2. Ideas para investigar y aplicar por tu cuenta (cada una con su búsqueda en la documentación oficial):

1. **`rememberSaveable` vs `remember`**: ¿qué le pasa al formulario si giras el teléfono? ¿Qué cambia con `rememberSaveable`?
2. **Rutas tipadas** (navegación con argumentos): pasar un `id` de evento entre pantallas usando rutas con parámetros.
3. **`Snackbar` / `AlertDialog`**: mostrar "Evento guardado" después de guardar.
4. **`DropdownMenu`** para la categoría o el lugar, en vez de un campo de texto libre.
5. **Persistencia con Room / DataStore**: que la agenda sobreviva al cerrar la app.
6. **Animaciones**: transición al navegar (`AnimatedContent`) o al agregar un evento.

> Regla de trabajo: investiga, aplica en tu código y deja **una línea comentada** explicando qué hace y de dónde salió.

---

## Stack técnico

- Kotlin + Jetpack Compose (Material 3)
- Navigation Compose
- Arquitectura MVVM (`ViewModel` + `StateFlow`)
- Gradle con version catalog (`gradle/libs.versions.toml`)

Mínimo y sin frameworks extra (sin Hilt, sin Room) a propósito: el foco es que se vea **cada pieza** de la arquitectura, no que una librería haga magia.
