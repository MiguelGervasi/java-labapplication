import java.util.*;

public class Student{

  int id;
  String firstname,lastname;
  //private int id;

  public Student(int i, String f, String l)
  {
	  this.id = i;
	  this.firstname = f;
	  this.lastname = l;
  }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
		    return true;
		}
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Student other = (Student) obj;
        if (id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        //int hash = Integer.parseInt(id);
        //return hash;
        return this.id;

    }

    public String toString()
    {
		return String.valueOf(id);
	}

}