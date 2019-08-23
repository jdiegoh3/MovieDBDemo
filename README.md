# Aplicación visualización de Películas

**Documentation view**: **COMMAND + MAY + -** or go to code>folding>collapse all

### Patron de Arquitectonico: MVVM



### Idiomas soportados: Inglés (default) y español



## Capas de la aplicación.

- Capa de vistas/presentación: Todas las Views y los ViewModels ( Se encuentran dentro de UI )
- Capa de Red: Paquete **services**.
- Business Logic: Repository/ViewModels.
- Capa Persistencia: CacheUtils se encarga de manejar el cache de las peticiones Http y almacenarlos. (La carpeta **models** está preparada para el manejo de persistencia por medio de base de datos local.)
- Capa de Datos: Paquete **models**



## Responsabilidades de cada clase creada.

- Movie: Es el modelo de la película. Almacena y accede a los datos de una pelicula.
- MoviesAPI: Interfaz de retrofit. Consumir las Rest APIs asociadas a Movie.
- MoviesAPIClient: Es el cliente de la interfaz **MoviesAPI**, facilita el consumo, manejo y emisión de los datos hacia el Repository.
- MovieRepository: Mediador entre el Modelo y el consumo de las Rest APIs. ( Se podría decir que es el mediador entre los ViewModels - Model/Rest API)
- Todo el paquete de serializers: Cada clase es un serializador, preparados para convertir (parsear) un JSON en una Clase de Java (usan la libreria GSON).
- Todo el paquete ui>adapters: Son los adaptadores, viewholders que permiten adaptar las estructuras de datos  (como ej: List, etc.) a los elementos del UI como RecyclersViews.
- BaseActivity: Actividad Base de la cual extienden las demás. Es muy importante para implementar funcionalidades a todas las Activities, inyeccion de dependencias etc.
- MovieListActivity: La parte visual (UI) del modelo Movie y sus categorias, usa un RecyclerView para mostrar Categorías, Peliculas y algunos elementos de UX como progressBar etc.
- MoviListViewModel: Es el que se encarta de conectar la UI con el Model. Posee funcionalidades que interacturan con el modelo mediande el Repository.



## Preguntas

En qué consiste el principio de responsabilidad única? Cuál es su propósito?

- Consiste en la separación de responsabilidades de cada módulo o clase y en la medida de lo posible se encarguen de realizar una funcionalidad en especifico. Cada elemento, servicio etc. debe estar alineado con la responsabilidad que fue creado. Entre sus propositos me atrevería a decir que se enfocan en mantener una alta cohesión (  funcionalidades unidas entre si por diferentes clases)  pero manteniendo un bajo acoplamiento esto nos trae beneficios como alta modularidad, facil testeo, mayor posibilidad de reutilización de código, legibilidad del código entre otras.

Qué características tiene, según su opinión, un "buen" código o código limpio?

- Patron arquitectonico bien definido.
- Patrones de diseño qué modularicen funcionalidades y hagan el código más legible.
- Evitar redundancia, repetición de código.
- Bien documentado.
- Fácilmente entendible y extendible por otro desarrollador.
- En lo posible, con pruebas unitarias implementadas.



## Posibles mejoras

- Implementación de inyección de dependencias **DI** (Usando Dagger2 o Toothpick)
- Darle un mejor manejo al RecyclerView respecto a los elementos de UX.
- Manejo de cache por medio de persistencia en local storage (Usando Realm u otra librería).





Juan Diego Hoyos M.