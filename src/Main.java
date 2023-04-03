import com.digdes.school.JavaSchoolStarter;

import java.util.*;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) throws Exception {

    JavaSchoolStarter starter = new JavaSchoolStarter();
        starter.execute("insert values 'id'=1, 'lastname'='Oleg', 'age'=0");
        starter.execute("insert values 'id'=2, 'lastname'='ba', 'age'=-5");
        starter.execute("insert values 'id'=3, 'lastname'='Pivo', 'age'=null");
        starter.execute("insert values 'id'=4, 'lastname'='Molecula', 'age'=15");
        starter.execute("insert values 'id'=5, 'lastname'=null");
        //Debug invoke
        System.out.println(starter.execute("update values 'lastname'='unknown' where 'lastname'=null"));





    }
}