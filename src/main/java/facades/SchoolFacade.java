package facades;

import entity.Semester;
import entity.Student;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import jdk.nashorn.internal.ir.ForNode;
import mappers.StudentInfo;

/**
 *
 * @author Mark
 */
public class SchoolFacade {

    EntityManagerFactory emf;

    public SchoolFacade(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public static void main(String[] args) {
        SchoolFacade facade = new SchoolFacade(Persistence.createEntityManagerFactory("pu"));

        for (StudentInfo studentInfo : facade.getStudentInfo()) {
            System.out.println(studentInfo.toString());
        }

        /*
        // 1+2
        for (Student student : facade.getAllStudentsWithSpecificFirstName("Anders")) {
            System.out.println(student.getFirstname() + " " + student.getLastname());
        }
        
        // 3
        System.out.println("Id: "+facade.insertStudent("Mark", "Denner"));
        
        
        // 4 
        facade.assignStudentASemester((long)7, (long)2);
        
        // 5
        for (Student student : facade.findStudentWithLastname("And")) {
            System.out.println(student.getFirstname() + " " + student.getLastname());
            
        }

        // 6 
        System.out.println("amount of students" + facade.findStudentsForSemester("").size());
        
        // 7
        System.out.println(facade.findAmountOfStudentsForSemesters());
         */
    }

    public List<Student> getAllStudents() {
        EntityManager em = emf.createEntityManager();
        try {
            Query query = em.createNamedQuery("Student.findAll", Student.class);
            return (List<Student>) query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Student> getAllStudentsWithSpecificFirstName(String firstname) {
        EntityManager em = emf.createEntityManager();
        try {
            Query query = em.createNamedQuery("Student.findByFirstname", Student.class).setParameter("firstname", firstname);
            return (List<Student>) query.getResultList();
        } finally {
            em.close();
        }
    }

    public Long insertStudent(String firstname, String lastname) {
        EntityManager em = emf.createEntityManager();
        Student student = new Student();
        student.setFirstname(firstname);
        student.setLastname(lastname);
        try {
            em.getTransaction().begin();
            em.persist(student);
            //em.getTransaction().commit();
            em.flush();
            return student.getId();
        } finally {
            em.close();
        }
    }

    public Long assignStudentASemester(Long studentId, Long semesterId) {
        EntityManager em = emf.createEntityManager();
        try {
            Query studentQuery = em.createNamedQuery("Student.findById").setParameter("id", studentId);
            Query semesterQuery = em.createNamedQuery("Semester.findById").setParameter("id", semesterId);
            Student student = (Student) studentQuery.getSingleResult();
            Semester semester = (Semester) semesterQuery.getSingleResult();
            em.getTransaction().begin();
            student.setCurrentsemesterId(semester);
            semester.getStudentCollection().add(student);
            em.getTransaction().commit();
            return student.getId();
        } finally {
            em.close();
        }
    }

    public List<Student> findStudentWithLastname(String lastname) {
        EntityManager em = emf.createEntityManager();
        try {
            Query query = em.createNamedQuery("Student.findByLastname").setParameter("lastname", lastname);
            return (List<Student>) query.getResultList();
        } finally {
            em.close();
        }

    }

    public List<Student> findStudentByLastname(String lastname) {
        EntityManager em = emf.createEntityManager();
        try {
            Query query = em.createNamedQuery("Student.findByLastname").setParameter("lastname", lastname);
            return (List<Student>) query.getResultList();
        } finally {
            em.close();
        }

    }

    public Long findAmountOfStudentsForSemester(String semesterName) {
        EntityManager em = emf.createEntityManager();
        try {
            Query query = em.createNamedQuery("Semester.findByName").setParameter("name", semesterName);
            Semester sem = (Semester) query.getSingleResult();
            return (long) sem.getStudentCollection().size();
        } finally {
            em.close();
        }

    }

    public Long findAmountOfStudentsForSemesters() {
        EntityManager em = emf.createEntityManager();
        try {
            Query query = em.createNamedQuery("Semester.findAll");
            List<Semester> semesters = (List<Semester>) query.getResultList();
            int iterator = 0;

            for (Semester semester : semesters) {
                iterator += semester.getStudentCollection().size();
            }

            return (long) iterator;
        } finally {
            em.close();
        }

    }

    public List<StudentInfo> getStudentInfo() {
        EntityManager em = emf.createEntityManager();
        try {
            Query query = em.createQuery("SELECT NEW mappers.StudentInfo(c.firstname, c.lastname, c.id, c.currentsemesterId.name, c.currentsemesterId.description) FROM Student AS c", StudentInfo.class);
            return (List<StudentInfo>) query.getResultList();

        } finally {
            em.close();
        }

    }

}
