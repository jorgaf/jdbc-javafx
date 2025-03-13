# Barbería FX

Es una aplicación con fines educativos que busca simular el proceso de creación de citas en una barbería.

La aplicación maneja:

- Clientes
- Barberos
- Servicios
- Citas

Usa JavaFX para la construcción de un cliente básico y una base de datos en MySQL. La implementación utiliza JDBC para interactuar con la base de datos.

El frontal (JavaFX) es básico y permite visualizar y crear citas. En esta primera implementación, para la creación de las
citas se usa únicamente cuadros de texto para el ingreso de los identificadores del barbero, el cliente y el servicio. De igual forma en la visualización de citas, únicamente se muestran los identificadores.

¿Qué cambios se debería hacer para presentar los nombres y no los identificadores?

Respuesta:
- Se realizaron cambios en [Cita.java](src/main/java/ec/edu/utpl/carreras/computacion/model/Cita.java) agregando los atributos para el manejo de los nombres, un nuevo constructor y métodos *get* para esos nuevos atributos.
- También se realizó un cambio en la consulta de la base de datos: modificar la consulta para obtener las citas, se usó *JOIN* para obtener los nombres del barbero, cliente y servicio (ver línea 61 clase [AppointmentController.java](src/main/java/ec/edu/utpl/carreras/computacion/controller/AppointmentController.java#L61)).
- En [AppointmentController.java](src/main/java/ec/edu/utpl/carreras/computacion/controller/AppointmentController.java#L76) se modificó el método *loadAppointmentsFromDB* para que trabaje con los nuevos datos (nombres)

Si bien estos cambios ayudan en la presentación, aún se usan cajas de texto para la creación de la cita, además, el ingreso de la fecha y hora de la reserva es complicado.

¿Qué cambios se debería realizar para crear una cita usando combobox (cajas de selección) que muestren los nombres del barbero, cliente y servicio? y ¿Cómo mejorar el ingreso de la fecha y hora de la reserva?


