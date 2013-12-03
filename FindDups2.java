import java.util.*;

public class FindDups2 {
    public static Set<Student> main(Set<Student> args) {
        Set<Student> uniques = new <Student>HashSet();
        Set<Student> dups = new <Student>HashSet();

        for (Student a : args)
            if(!uniques.add(a))
                dups.add(a);

        // Destructive set-difference
        uniques.removeAll(dups);

        System.out.println("Unique words:    " + uniques.size());
        System.out.println("Duplicate words: " + dups.size());

        return dups;
    }
}