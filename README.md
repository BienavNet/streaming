API REST Backend de gestión de ventas de cuentas de streaming.
Cuenta con las siguientes funcionalidades:
- Gestión de cuentas
- Gestión de membresías
- Gestión de planes
- Gestión de órdenes
- Gestión de pagos
- Gestión de cron tasks
- Gestión de identidad y autenticación mediante JWT (sesiones stateless).

Arquitectura utilizada:
Para el desarrollo de esta API REST se utilizo un enfoque basado en arquitectura limpia (Clean Architecture), que se combinó con un enfoque de desarrollo DDD (Domain-Driven Design),
esto para aislar el dominio de la aplicación (core) de las implementaciones concretas (framework), con el único fin de brindar una alta mantenibilidad y escalabilidad del sistema 
con características futuras. Las desventajas de esta implementación, es que el sistema al ser pequeño, se genera mucho boilerplate con las transiciones entre las capas (muchos mappeos y DTOs)
lo que lo hace un poco pesada para la solución actual (también el motivo del desarrollo bajo esta arquitectura era poner en práctica como se vería un desarrollo a esta escala y con esta implementación).
También se utilizaron diferentes principios SOLID para mantener el enfoque de la arquitectura lo más ceñido posible.
Además se emplearon patrones de diseño como Factory para la creación de objetos de una sola instancia, Strategy para extender la funcionalidad de filtros, CQRS para hacer las consultas con tablas cruzadas
con el fin de evitar acoplamientos entre los repositorios.

*Para los endpoints puedes usar la ruta de swagger en /swagger-ui/index.html*
Controllers:
- Cuentas /api/account:
    POST /: Almacenar una nueva cuenta dentro del sistema
      Parámetros: {email: string, password: string, platform: string, payment: string}
      Descripción: platform es un id de una plataforma ya registrada, payment es un id de un medio de pago ya registrado
      Retorna: {id: id de la respuesta, status: estado http de la respuesta, message: mensaje o descripción de la acción realizada, data: objeto de la nueva cuenta registrada en el sistema}
  
    DELETE /{id}: Eliminar una cuenta existente dentro del sistema
      Parámetros: {id: string (en la URL / path)}
      Descripción: Elimina la cuenta asociada al identificador único proporcionado utilizando el caso de uso correspondiente.
      Retorna: {status: 204 No Content} (sin cuerpo de respuesta)

    GET /details/{page}/{offset}: Obtener los detalles de las cuentas de forma paginada
      Parámetros: {page: int (en la URL / path), offset: int (en la URL / path)}
      Descripción: Retorna una lista paginada con los detalles de las cuentas registradas en el sistema.
      Retorna: {id: id de la respuesta, status: 200 OK, message: mensaje o descripción de la acción realizada, data: objeto paginado con una lista de elementos donde cada uno contiene {id: string, email: string, platformName: string}}

    GET /{id}: Obtener una cuenta específica por su identificador único
      Parámetros: {id: string (en la URL / path)}
      Descripción: Busca y retorna la información detallada de una cuenta registrada utilizando su ID.
      Retorna: {id: id de la respuesta, status: 200 OK, message: mensaje o descripción de la acción realizada, data: objeto de la cuenta encontrada {AccountResponse}}

    PUT /{id}: Actualizar una cuenta existente en el sistema
      Parámetros:
      - id: string (en la URL / path)
      - request: {email: string, password: string, platform: string, payment: string} (en el cuerpo / body)
      Descripción: Actualiza la información de una cuenta específica utilizando su ID y los nuevos datos proporcionados (donde platform y payment son IDs de registros existentes).
      Retorna: {id: string (ID de la cuenta), status: 200 OK, message: "Entity updated successfully", data: objeto de la cuenta actualizada {id: string, email: string, password: string, registeredAt: LocalDate, platform: PlatformResponse, payment: PaymentResponse}}

    GET /filter/{field}/{criteria}: Filtrar cuentas según un campo y un criterio específico
      Parámetros: {field: string (en la URL / path), criteria: string (en la URL / path)}
      Descripción: Permite filtrar las cuentas registradas en el sistema indicando el campo por el cual se desea buscar (field) y el valor o criterio de búsqueda (criteria).
      Retorna: {id: id de la respuesta, status: 200 OK, message: mensaje o descripción de la acción realizada, data: lista de objetos {id: string, email: string, platformName: string}}

  - Crontasks /api/crontasks
    PUT /status/{id}/{status}: Actualizar el estado de una tarea o cuenta en el sistema
      Parámetros: {id: string (en la URL / path), status: boolean (en la URL / path)}
      Descripción: Modifica el estado actual (activo/inactivo o habilitado/deshabilitado representado por un booleano) del registro asociado al ID proporcionado.
      Retorna: {id: id de la respuesta, status: 200 OK, message: "Status Updated Successfully.", data: null (sin datos en el cuerpo)}

    GET /: Obtener todas las tareas cron registradas en el sistema
      Parámetros: Ninguno
      Descripción: Consulta y retorna una lista con todas las tareas cron (cron tasks) disponibles en el sistema.
      Retorna: {id: id de la respuesta, status: 200 OK, message: mensaje o descripción de la acción realizada, data: lista de objetos {id: string, name: string, description: string, active: boolean}}

  - Dashboard /api/dashboard
    GET /: Obtener la información general para el panel principal (dashboard)
      Parámetros: Ninguno
      Descripción: Consulta y retorna un resumen consolidado con los datos clave y métricas necesarias para mostrar en el dashboard del sistema.
      Retorna: {id: id de la respuesta, status: 200 OK, message: mensaje o descripción de la acción realizada, data: objeto de tipo DashboardInfoQuery con la siguiente estructura: {
      totalAccounts: Long,
      totalOrders: Long,
      totalPlatforms: Long,
      totalOrderMoney: Double,
      accountsByPlatform: lista de objetos {AccountsByPlatformQuery},
      membershipNearToExpire: lista de objetos {MembershipNearToExpireQuery},
      ordersNearToExpireQuery: lista de objetos {OrderDetailsSnapshotQuery}
      }}

  - Membership Config (Plan) /api/membership/config
    POST /: Almacenar una nueva configuración de membresía dentro del sistema
      Parámetros: {request: {platform: string, plan: string, profiles: int} (en el cuerpo / body)}
      Descripción: platform es el ID de una plataforma registrada, plan es el identificador o nombre del plan, y profiles indica la cantidad de perfiles asociados.
      Retorna: {id: string (ID de la nueva configuración creada), status: 201 Created, message: "Membership config created successfully.", data: string (ID de la configuración creada)}

    DELETE /{id}: Eliminar una configuración de membresía existente
      Parámetros: {id: string (en la URL / path)}
      Descripción: Elimina la configuración de membresía asociada al identificador único proporcionado utilizando el caso de uso correspondiente.
      Retorna: {status: 204 No Content} (sin cuerpo de respuesta)

    GET /{id}: Obtener los detalles de una configuración de membresía específica
      Parámetros: {id: string (en la URL / path)}
      Descripción: Busca y retorna la información detallada de una configuración de membresía registrada utilizando su ID.
      Retorna: {id: id de la respuesta, status: 200 OK, message: mensaje o descripción de la acción realizada, data: objeto de tipo MembershipConfigResponse {id: string, platform: string, plan: string, profiles: Integer}}

    GET /platform/{id}: Obtener las configuraciones de membresía por plataforma
      Parámetros: {id: string (en la URL / path)}
      Descripción: platform es el ID de una plataforma registrada. Consulta y retorna una lista con todas las configuraciones de membresía asociadas a dicha plataforma.
      Retorna: {id: id de la respuesta, status: 200 OK, message: mensaje o descripción de la acción realizada, data: lista de objetos {id: string, platform: string, plan: string, profiles: Integer}}

  - Membership /api/membership
    POST /: Almacenar una nueva membresía dentro del sistema
      Parámetros: {request: {account: string, membershipConfig: string, isFreeTrial: boolean, membershipBuyDate: LocalDate, membershipExpireDate: LocalDate} (en el cuerpo / body)}
      Descripción: account es el ID de una cuenta registrada, membershipConfig es el ID de una configuración de membresía registrada, isFreeTrial indica si es una prueba gratuita, y se especifican las fechas de compra y expiración de la membresía.
      Retorna: {id: string (ID de la nueva membresía creada), status: 201 Created, message: "Membership created succesfully.", data: string (ID de la membresía creada)}

    DELETE /account/{id}: Eliminar una membresía por el ID de cuenta
      Parámetros: {id: string (en la URL / path)}
      Descripción: accountId es el ID de una cuenta. Elimina la membresía o membresías asociadas a dicha cuenta utilizando el caso de uso correspondiente.
      Retorna: {status: 204 No Content} (sin cuerpo de respuesta)

    GET /account/{id}: Obtener los detalles de una membresía mediante el ID de la cuenta
      Parámetros: {id: string (en la URL / path)}
      Descripción: accountId es el ID de una cuenta. Consulta y retorna la información detallada de la membresía asociada a dicha cuenta.
      Retorna: {id: string (ID de la cuenta), status: 200 OK, message: mensaje o descripción de la acción realizada, data: objeto de tipo MembershipDetailsResponse {id: string, details: MembershipConfigResponse, isFreeTrial: boolean, membershipBuyDate: LocalDate, membershipExpireDate: LocalDate}}

    PUT /{id}: Actualizar una membresía existente en el sistema
      Parámetros:
      - id: string (en la URL / path)
      - request: {isFreeTrial: boolean, membershipBuyDate: LocalDate, membershipExpireDate: LocalDate, membershipConfigId: string} (en el cuerpo / body)
      Descripción: Actualiza la información de una membresía específica utilizando su ID y los nuevos datos proporcionados.
      Retorna: {id: string (ID de la membresía), status: 200 OK, message: "Membership updated successfully.", data: null (sin datos específicos en el cuerpo)}

  - Order /api/order
    POST /: Almacenar una nueva orden dentro del sistema
        Parámetros: {request: {account: string, profileAmount: int, buyDate: LocalDate, finishDate: LocalDate, paymentAmount: double, orderType: string} (en el cuerpo / body)}
        Descripción: account es el ID de la cuenta asociada, profileAmount indica la cantidad de perfiles, buyDate y finishDate son las fechas de inicio y finalización, paymentAmount representa el monto del pago, y orderType especifica el tipo de orden.
        Retorna: {id: string (ID de la orden creada), status: 201 Created, message: "Order created successfully.", data: string (ID de la orden creada)}

     DELETE /{id}: Eliminar una orden existente del sistema
        Parámetros: {id: string (en la URL / path)}
        Descripción: orderId es el ID de la orden. Elimina la orden asociada a dicho identificador utilizando el caso de uso correspondiente.
        Retorna: {status: 204 No Content} (sin cuerpo de respuesta)

    GET /{id}: Obtener los detalles de una orden específica
      Parámetros: {id: string (en la URL / path)}
      Descripción: orderId es el ID de la orden. Consulta y retorna la información detallada de una orden registrada en el sistema.
      Retorna: {id: string (ID de la orden), status: 200 OK, message: mensaje o descripción de la acción realizada, data: objeto de tipo OrderDetailsResponse {id: string, accountEmail: string, platformName: string, platformIcon: string, plan: string, profileAmount: Integer, buyDate: LocalDate, finishDate: LocalDate, paymentAmount: Double, orderType: string}}

    GET /orders/{page}/{offset}: Obtener las órdenes de forma paginada
      Parámetros:
      - page: int (en la URL / path)
      - offset: int (en la URL / path)
      Descripción: page indica el número de página y offset especifica el tamaño o desplazamiento. Consulta y retorna un objeto paginado que contiene una lista resumida con las órdenes registradas en el sistema.
      Retorna: {id: id de la respuesta, status: 200 OK, message: mensaje o descripción de la acción realizada, data: objeto de tipo Page con elementos estructurados como {id: string, accountEmail: string, platformName: string, platformIcon: string, buyDate: LocalDate, orderType: string}}

    GET /records/{page}/{offset}: Obtener los registros de órdenes en formato de instantánea (snapshot) de forma paginada
      Parámetros:
      - page: int (en la URL / path)
      - offset: int (en la URL / path)
      Descripción: page indica el número de página y offset especifica el tamaño o desplazamiento. Consulta y retorna un objeto paginado con las instantáneas detalladas de los registros de órdenes.
      Retorna: {id: id de la respuesta, status: 200 OK, message: mensaje o descripción de la acción realizada, data: objeto de tipo Page con elementos estructurados como {id: string, accountEmail: string, platformName: string, planName: string, profileAmount: Integer, buyDate: LocalDate, finishDate: LocalDate, paymentAmount: Double, orderType: string}}

  - Payments /api/payment
      POST /: Almacenar un nuevo pago dentro del sistema
        Parámetros: {request: {number: string, month: Integer, year: Integer, cvv: string, bankName: string} (en el cuerpo / body)}
        Descripción: Registra un nuevo método de pago (tarjeta) con su número, mes y año de expiración, CVV y nombre del banco. Retorna la información detallada del pago creado.
        Retorna: {id: string (ID del pago creado), status: 201 Created, message: "Payment created successfully.", data: objeto de tipo PaymentResponse {id: string, card: string, bankName: string}}
      
      DELETE /{id}: Eliminar un pago existente del sistema
        Parámetros: {id: string (en la URL / path)}
        Descripción: Elimina el pago asociado al identificador único proporcionado utilizando el caso de uso correspondiente.
        Retorna: {status: 204 No Content} (sin cuerpo de respuesta)
      
      GET /{id}: Obtener los detalles de un pago específico
        Parámetros: {id: string (en la URL / path)}
        Descripción: Consulta y retorna la información detallada de un pago registrado utilizando su ID.
        Retorna: {id: id de la respuesta, status: 200 OK, message: mensaje o descripción de la acción realizada, data: objeto de tipo PaymentResponse {id: string, card: string, bankName: string}}
        
      GET /{page}/{offset}: Obtener los pagos de forma paginada
        Parámetros:
        - page: int (en la URL / path)
        - offset: int (en la URL / path)
        Descripción: page indica el número de página y offset especifica el tamaño o desplazamiento. Consulta y retorna un objeto paginado con la lista de pagos registrados.
        Retorna: {id: id de la respuesta, status: 200 OK, message: mensaje o descripción de la acción realizada, data: objeto de tipo Page con elementos estructurados como {id: string, card: string, bankName: string}}
        
      GET /filter/{field}/{criteria}: Filtrar pagos por campo y criterio
        Parámetros:
        - field: string (en la URL / path)
        - criteria: string (en la URL / path)
        Descripción: field representa el atributo por el cual filtrar y criteria es el valor de búsqueda. Consulta y retorna una lista con los pagos que coinciden con los criterios de filtrado.
        Retorna: {id: id de la respuesta, status: 200 OK, message: mensaje o descripción de la acción realizada, data: lista de objetos PaymentResponse {id: string, card: string, bankName: string}}

- Platform /api/platform
    POST /: Almacenar una nueva plataforma dentro del sistema
      Parámetros: {request: {name: string, link: string, icon: string} (en el cuerpo / body)}
      Descripción: Registra una nueva plataforma con su nombre, enlace y un icono representativo. Retorna la información detallada de la plataforma creada.
      Retorna: {id: string (ID de la plataforma creada), status: 201 Created, message: "Platform created succesfully.", data: objeto de tipo PlatformResponse {id: string, name: string, link: string, icon: string}}
      
    DELETE /{id}: Eliminar una plataforma existente del sistema
      Parámetros: {id: string (en la URL / path)}
      Descripción: Elimina la plataforma asociada al identificador único proporcionado utilizando el caso de uso correspondiente.
      Retorna: {status: 204 No Content} (sin cuerpo de respuesta)
      
    GET /: Obtener todas las plataformas registradas
      Parámetros: Ninguno
      Descripción: Consulta y retorna una lista con todas las plataformas disponibles en el sistema.
      Retorna: {id: id de la respuesta, status: 200 OK, message: mensaje o descripción de la acción realizada, data: lista de objetos PlatformResponse {id: string, name: string, link: string, icon: string}}
      
    GET /{id}: Obtener los detalles de una plataforma específica
      Parámetros: {id: string (en la URL / path)}
      Descripción: Consulta y retorna la información detallada de una plataforma utilizando su identificador único.
      Retorna: {id: id de la respuesta, status: 200 OK, message: mensaje o descripción de la acción realizada, data: objeto de tipo PlatformResponse {id: string, name: string, link: string, icon: string}}
      
    PUT /{id}: Actualizar una plataforma existente
      Parámetros:
      - id: string (en la URL / path)
      - platformRequest: {name: string, link: string, icon: string} (en el cuerpo / body)
      Descripción: Actualiza la información de una plataforma utilizando su ID y los nuevos datos proporcionados (nombre, enlace e icono). Retorna la plataforma actualizada.
      Retorna: {id: id de la respuesta, status: 200 OK, message: mensaje o descripción de la acción realizada, data: objeto de tipo PlatformResponse {id: string, name: string, link: string, icon: string}}
      
    GET /filter/{field}/{criteria}: Filtrar plataformas por campo y criterio
      Parámetros:
      - field: string (en la URL / path)
      - criteria: string (en la URL / path)
      Descripción: field representa el atributo por el cual filtrar y criteria es el valor de búsqueda. Consulta y retorna una lista con las plataformas que coinciden con los criterios de filtrado.
      Retorna: {id: id de la respuesta, status: 200 OK, message: mensaje o descripción de la acción realizada, data: lista de objetos PlatformResponse {id: string, name: string, link: string, icon: string}}

Despliegue:
