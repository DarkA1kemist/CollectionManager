import com.digdes.school.JavaSchoolStarter;

import java.util.*;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) throws Exception {

    JavaSchoolStarter starter = new JavaSchoolStarter();

        starter.execute("insert values 'id'=3, 'lastname'='Pivo', 'age'=null");

        //Debug invoke
        System.out.println(starter.execute("select wHEre 'Age'=null"));





    }
}