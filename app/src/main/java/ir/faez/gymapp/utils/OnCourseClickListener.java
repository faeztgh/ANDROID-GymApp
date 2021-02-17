package ir.faez.gymapp.utils;

import ir.faez.gymapp.data.model.Course;

public interface OnCourseClickListener {

    void onCourseClicked(Course course, int position,String status);
}
