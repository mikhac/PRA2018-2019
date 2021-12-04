package hibernate;


import hibernate.model.Address;
import hibernate.model.Employee;
import hibernate.queries.Queries;

import javax.persistence.*;
import java.util.List;
import java.util.Random;


class Manager {

    public static void main(String[] args) {
        System.out.println("Start");
        EntityManager entityManager;
        EntityManagerFactory entityManagerFactory = null;

        try {
            // FACTORY NAME HAS TO MATCH THE ONE FROM PERSISTED.XML !!!
            entityManagerFactory = Persistence.createEntityManagerFactory("hibernate-dynamic");

            entityManager = entityManagerFactory.createEntityManager();

            entityManager.getTransaction().begin();

            Address empAddress = new Address();
            empAddress.setCity("Guadalajara");
            empAddress.setStreet("Dyniowa");
            empAddress.setNr("8");
            empAddress.setHousenr("20");
            empAddress.setPostcode("419");
            entityManager.persist(empAddress);

            Employee emp = new Employee();
            emp.setFirstName("Jan");
            emp.setLastName("Polak" + new Random().nextInt());
            emp.setSalary(100);
            emp.setPesel(new Random().nextInt());
            emp.setAddress(empAddress);
            entityManager.persist(emp);

            // zad 10
            for (int i = 1; i < 100; i++) {
                entityManager.persist(Employee.copyEmployee(emp));
            }

            Employee employee = entityManager.find(Employee.class, emp.getId());
            if (employee == null)
                System.out.println(emp.getId() + " not found!");
            else
                System.out.println("Found " + employee);

            assert employee != null;
            System.out.println("Employee " + employee.getId() + " " + employee.getFirstName() + employee.getLastName());
            changeFirstGuyToNowak(entityManager);

            entityManager.getTransaction().commit();
            System.out.println("Done");

            // zad6 - test poza transakcją
//            entityManager.remove(emp);
//            entityManager.persist(emp);

            // zad 8
//            emp.getAddress().setStreet(null);

            entityManager.close();
        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed. " + ex);
        } finally {
            assert entityManagerFactory != null;
            entityManagerFactory.close();
        }
    }

    static void changeFirstGuyToNowak(EntityManager entityManager) {
        List<Employee> employees = new Queries(entityManager).getEmployeeByName("Polak");
        employees.get(0).setLastName("NowakPRE" + new Random().nextInt());
    }
}
