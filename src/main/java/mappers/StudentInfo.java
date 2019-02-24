package mappers;

/**
 *
 * @author Mark
 */
public class StudentInfo {

    public StudentInfo(String firstname,String lastname, long studentId, String classNameThisSemester, String classDescription) {
        this.fullName = firstname + " " + lastname;
        this.studentId = studentId;
        this.classNameThisSemester = classNameThisSemester;
        this.classDescription = classDescription;
    }

    @Override
    public String toString() {
        return "StudentInfo{" + "fullName=" + fullName + ", studentId=" + studentId + ", classNameThisSemester=" + classNameThisSemester + ", classDescription=" + classDescription + '}';
    }

    
    
public String fullName;
public long studentId;
public String classNameThisSemester;
public String classDescription;

    
}
