
package org.example;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("example-unit");

        EntityManager em = emf.createEntityManager();
        System.out.println("funcionando  Alberto");
        // Crear objetos de prueba
        Hotel hotel1 = new Hotel();
        hotel1.setNombre("Hotel A");
        hotel1.setDireccion("Dirección 1");
        hotel1.setCiudad("Ciudad A");
        hotel1.setPais("País A");
        hotel1.setEstrellas(4);

        Habitacion habitacion1 = new Habitacion();
        habitacion1.setNumero("101");
        habitacion1.setTipo("Doble");
        habitacion1.setPrecio(100.0);
        hotel1.getHabitaciones().add(habitacion1);

        Habitacion habitacion2 = new Habitacion();
        habitacion2.setNumero("102");
        habitacion2.setTipo("Individual");
        habitacion2.setPrecio(80.0);
        hotel1.getHabitaciones().add(habitacion2);

        Cliente cliente1 = new Cliente();
        cliente1.setNombre("Cliente 1");
        cliente1.setApellido("Apellido 1");
        cliente1.setEmail("cliente1@example.com");
        cliente1.setTelefono("123456789");

        Reserva reserva1 = new Reserva();
        reserva1.setFechaEntrada(LocalDate.of(2022, 1, 1));
        reserva1.setFechaSalida(LocalDate.of(2022, 1, 5));
        reserva1.setCliente(cliente1);
        reserva1.setHabitacion(habitacion1);

        // Persistir objetos en la base de datos
        em.getTransaction().begin();
        em.persist(hotel1);
        em.persist(cliente1);
        em.persist(reserva1);
        em.getTransaction().commit();



        // Verificar la consulta
        String ciudad = "Ciudad A";
        List<Hotel> hoteles = em.createQuery(
                        "SELECT h FROM Hotel h WHERE h.ciudad = :ciudad", Hotel.class)
                .setParameter("ciudad", ciudad)
                .getResultList();

        System.out.println("Hoteles en la ciudad " + ciudad + ":");
        for (Hotel hotel : hoteles) {
            System.out.println(hotel.getNombre());
        }

        // Verificar las consultas
        LocalDate fechaEntrada = LocalDate.of(2022, 1, 1);
        LocalDate fechaSalida = LocalDate.of(2022, 1, 5);
        Hotel hotel = hotel1; // Usar el hotel creado anteriormente

        // Consulta 1: Obtener el número total de reservas en un rango de fechas
        Long totalReservas = em.createQuery(
                        "SELECT COUNT(r) FROM Reserva r WHERE r.fechaEntrada >= :fechaEntrada AND r.fechaSalida <= :fechaSalida", Long.class)
                .setParameter("fechaEntrada", fechaEntrada)
                .setParameter("fechaSalida", fechaSalida)
                .getSingleResult();
        System.out.println("Total de reservas en el rango de fechas: " + totalReservas);

        // Consulta 2: Obtener el tipo de habitación más reservado en un rango de fechas
        String tipoHabitacion = em.createQuery(
                        "SELECT r.habitacion.tipo FROM Reserva r WHERE r.fechaEntrada >= :fechaEntrada AND r.fechaSalida <= :fechaSalida GROUP BY r.habitacion.tipo ORDER BY COUNT(r) DESC", String.class)
                .setParameter("fechaEntrada", fechaEntrada)
                .setParameter("fechaSalida", fechaSalida)
                .setMaxResults(1)
                .getSingleResult();
        System.out.println("Tipo de habitación más reservado en el rango de fechas: " + tipoHabitacion);

        // Consulta 3: Obtener el cliente con más reservas
        Cliente cliente = em.createQuery(
                        "SELECT r.cliente FROM Reserva r GROUP BY r.cliente ORDER BY COUNT(r) DESC", Cliente.class)
                .setMaxResults(1)
                .getSingleResult();
        System.out.println("Cliente con más reservas: " + cliente.getNombre() + " " + cliente.getApellido());


        // Cerrar el EntityManager y el EntityManagerFactory
        em.close();
        emf.close();
    }
}

/*

Manejo del Ciclo de Estados en JPA
El ciclo de estados en JPA (Java Persistence API) define los diferentes estados que puede tener una entidad en relación con el contexto de persistencia (EntityManager). Comprender y manejar correctamente estos estados es crucial para trabajar eficazmente con JPA. Los estados del ciclo de vida de una entidad en JPA son:

New (Nuevo):

Una entidad está en estado "New" cuando ha sido creada pero aún no ha sido persistida en la base de datos.
Managed (Gestionado):

Una entidad está en estado "Managed" cuando está asociada con un contexto de persistencia (EntityManager) y cualquier cambio en la entidad se reflejará automáticamente en la base de datos.
Detached (Desconectado):

Una entidad está en estado "Detached" cuando ya no está asociada con un contexto de persistencia. Los cambios en la entidad no se reflejarán automáticamente en la base de datos.
Removed (Eliminado):

Una entidad está en estado "Removed" cuando ha sido marcada para su eliminación en la base de datos.
*/


