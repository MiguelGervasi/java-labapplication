import java.util.*;

public class Logs{

  int id;
  String datestamp,signin,signout;
  //private int id;

  public Logs(int i, String ds, String si, String so)
  {
	  this.id = i;
	  this.datestamp = ds;
	  this.signin = si;
      this.signout = so;
  }

//make a constructor including date so we can order by signin and signout

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
        Logs other = (Logs) obj;
        if (id != other.id || !datestamp.equals(other.datestamp) || !signin.equals(other.signin) || !signout.equals(other.signout)) {
            return false;
		}
		return true;

    }

    @Override
    public int hashCode() {
        return this.id;
    }


    public String toString()
    {
		return String.valueOf(id);
	}

}