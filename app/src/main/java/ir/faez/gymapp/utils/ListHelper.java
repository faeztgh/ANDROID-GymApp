package ir.faez.gymapp.utils;

import java.util.ArrayList;
import java.util.List;

import ir.faez.gymapp.data.model.Course;

public class ListHelper {
    private static ListHelper instance;
    private List<Course> allCoursesList;

    private ListHelper(){
        allCoursesList=new ArrayList<>();
    }

    public synchronized static ListHelper getInstance(){
        if (instance == null){
            instance = new ListHelper();
        }
        return instance;
    }

    public boolean insetToAllCourses(Course course){
        return allCoursesList.add(course);
    }


}
