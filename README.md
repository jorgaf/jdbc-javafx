# Barbería FX

Es una aplicación con fines educativos que busca simular el proceso de creación de citas en una barbería.

La aplicación maneja:

- Clientes
- Barberos
- Servicios
- Citas

Usa JavaFX para la construcción de un cliente básico y una base de datos en MySQL. La implementación utiliza JDBC para
interactuar con la base de datos.

El frontal (JavaFX) es básico y permite visualizar y crear citas. En esta primera implementación, para la creación de
las
citas se usa únicamente cuadros de texto para el ingreso de los identificadores del barbero, el cliente y el servicio.
De igual forma en la visualización de citas, únicamente se muestran los identificadores.

¿Qué cambios se debería hacer para presentar los nombres y no los identificadores?

Respuesta:

- Se realizaron cambios en [Cita.java](src/main/java/ec/edu/utpl/carreras/computacion/model/Cita.java) agregando los
  atributos para el manejo de los nombres, un nuevo constructor y métodos *get* para esos nuevos atributos.
- También se realizó un cambio en la consulta de la base de datos: modificar la consulta para obtener las citas, se usó
  *JOIN* para obtener los nombres del barbero, cliente y servicio (ver línea 61
  clase [AppointmentController.java](src/main/java/ec/edu/utpl/carreras/computacion/controller/AppointmentController.java#L136)).
-
En [AppointmentController.java](src/main/java/ec/edu/utpl/carreras/computacion/controller/AppointmentController.java#L135)
se modificó el método *loadAppointmentsFromDB* para que trabaje con los nuevos datos (nombres)

Si bien estos cambios ayudan en la presentación, aún se usan cajas de texto para la creación de la cita, además, el
ingreso de la fecha y hora de la reserva es complicado.

¿Qué cambios se debería realizar para crear una cita usando combobox (cajas de selección) que muestren los nombres del
barbero, cliente y servicio? y ¿Cómo mejorar el ingreso de la fecha y hora de la reserva?

Respuesta:
Para realizar la implementación es necesario modificar varios archivos, pero a nivel de base de datos fue necesario:

- Agregar consultas para traer los datos de todos los barberos, clientes y servicios. Revisar la
  clase [AppointmentController](src/main/java/ec/edu/utpl/carreras/computacion/controller/AppointmentController.java) y
  los
  métodos [loadBarberos](src/main/java/ec/edu/utpl/carreras/computacion/controller/AppointmentController.java#L63), [loadClientes](src/main/java/ec/edu/utpl/carreras/computacion/controller/AppointmentController.java#L87), [loadServicios](src/main/java/ec/edu/utpl/carreras/computacion/controller/AppointmentController.java#L111).
- Para los cambios de la GUI se realizaron varios cambios en archivos
  como [MainView.fxml](src/main/resources/ec/edu/utpl/carreras/computacion/MainView.fxml)
  y [AppointmentController](src/main/java/ec/edu/utpl/carreras/computacion/controller/AppointmentController.java). Con
  el fin de reemplazar las cajas de texto por combos y agregar un control denominado DatePicker para seleccionar la
  fecha.
- Además se agregó se sobreescribió el método toString de las
  clases: [Barbero](src/main/java/ec/edu/utpl/carreras/computacion/model/Barbero.java), [Cliente](src/main/java/ec/edu/utpl/carreras/computacion/model/Cliente.java)
  y [Servicio](src/main/java/ec/edu/utpl/carreras/computacion/model/Servicio.java).

Si bien se ha conseguido mejorar el ingreso, aún queda pendiente la selección de la hora.

La aplicación funciona y hace lo que tiene que hacer, pero, se puede mejorar. Para mejorar la aplicación se debe usar
patrones de diseño. ¿Qué patrones se podría aplicar? (Recomendación: busque información sobre los patrones: Singleton y
Repository).

Respuesta:

Se usaron los patrones anteriormente mencionados, esto ha permitido que la
clase [AppointmentController](src/main/java/ec/edu/utpl/carreras/computacion/controller/AppointmentController.java)
quede libre del trabajo con la base de datos y se especialice en su rol dentro del MVC (controlador).

Los cambios que se hicieron incluye:

- Agregar las
  clases [BarberoRepository](src/main/java/ec/edu/utpl/carreras/computacion/repository/BarberoRepository.java), [CitaRepository](src/main/java/ec/edu/utpl/carreras/computacion/repository/CitaRepository.java), [ClienteRepository](src/main/java/ec/edu/utpl/carreras/computacion/repository/ClienteRepository.java)
  y [ServicioRepository](src/main/java/ec/edu/utpl/carreras/computacion/repository/ServicioRepository.java)
- En la clase AppointmentController:
    - Se han modificado los
      métodos: [loadBarberos](src/main/java/ec/edu/utpl/carreras/computacion/controller/AppointmentController.java#L69), [loadClientes](src/main/java/ec/edu/utpl/carreras/computacion/controller/AppointmentController.java#L82), [loadServicios](src/main/java/ec/edu/utpl/carreras/computacion/controller/AppointmentController.java#95), [loadAppointmentsFromDB](src/main/java/ec/edu/utpl/carreras/computacion/controller/AppointmentController.java#108)
      y [handleAddAppointment](src/main/java/ec/edu/utpl/carreras/computacion/controller/AppointmentController.java#121)
    - Se agregaron los
      atributos: [CitaRepository](src/main/java/ec/edu/utpl/carreras/computacion/controller/AppointmentController.java#L47), [BarberoRepository](src/main/java/ec/edu/utpl/carreras/computacion/controller/AppointmentController.java#L48), [ClienteRepository](src/main/java/ec/edu/utpl/carreras/computacion/controller/AppointmentController.java#L49)
      y [ServicioRepository](src/main/java/ec/edu/utpl/carreras/computacion/controller/AppointmentController.java#L50)
- En la clase [Cita](src/main/java/ec/edu/utpl/carreras/computacion/model/Cita.java) se agregó el
  método [setId](src/main/java/ec/edu/utpl/carreras/computacion/model/Cita.java#L53)
