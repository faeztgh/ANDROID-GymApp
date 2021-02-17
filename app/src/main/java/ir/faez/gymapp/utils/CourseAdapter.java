package ir.faez.gymapp.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ir.faez.gymapp.R;
import ir.faez.gymapp.activities.AllCoursesActivity;
import ir.faez.gymapp.activities.MyCourseActivity;
import ir.faez.gymapp.data.model.Course;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {

    // Instance fields
    private Context context;
    private List<Course> courses;
    private HashMap<Course, String> myCourseListAndStatus;
    private LayoutInflater layoutInflater;
    private OnCourseClickListener onCourseClickListener;
    private String status;


    // Constructor
    public CourseAdapter(Context context, List<Course> courses,
                         OnCourseClickListener onCourseClickListener) {
        this.context = context;
        this.courses = courses;
        this.onCourseClickListener = onCourseClickListener;
        this.layoutInflater = LayoutInflater.from(context);
    }


    // Constructor

    public CourseAdapter(Context context, HashMap<Course, String> myCourseListAndStatus,
                         OnCourseClickListener onCourseClickListener) {
        this.context = context;
        this.onCourseClickListener = onCourseClickListener;
        this.layoutInflater = LayoutInflater.from(context);
        this.myCourseListAndStatus = myCourseListAndStatus;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_course, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            holder.setItemData(position);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (courses != null) {
            return courses.size();
        }
        return myCourseListAndStatus != null ? myCourseListAndStatus.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ConstraintLayout itemLayout;
        public ImageView courseIconIv;
        public TextView exerciseTitle;
        public TextView exerciseType;
        public TextView exerciseTime;
        public TextView statusRibbon;
        private int position;
        private Course course;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemLayout = itemView.findViewById(R.id.item_layout_constraint);
            courseIconIv = itemView.findViewById(R.id.exercise_icon_iv);
            exerciseTitle = itemView.findViewById(R.id.exercise_title_tv);
            exerciseType = itemView.findViewById(R.id.exercise_variant_tv);
            exerciseTime = itemView.findViewById(R.id.exercise_time_tv);
            statusRibbon = itemView.findViewById(R.id.myCourse_ribbon_tv);


            invokeOnClickListeners();
        }

        private void invokeOnClickListeners() {
            itemLayout.setOnClickListener(this);
        }


        @SuppressLint({"UseCompatLoadingForColorStateLists", "SetTextI18n"})
        public void setItemData(int position) throws IOException {
            this.position = position;

            if (myCourseListAndStatus != null && myCourseListAndStatus.size() != 0) {
                course = (new ArrayList<>(myCourseListAndStatus.keySet())).get(position);
                status = (new ArrayList<>(myCourseListAndStatus.values())).get(position);
            } else {
                course = courses.get(position);
                status = " ";
            }

            try {
                if (myCourseListAndStatus != null
                        && myCourseListAndStatus.size() != 0
                        && context.getClass() == MyCourseActivity.class) {
                    if (status.equals(Status.PENDING)) {
                        itemLayout.setBackgroundTintList(context.getResources().getColorStateList(R.color.Corn));
                    }
                    if (status.equals(Status.RESERVED)) {
                        itemLayout.setBackgroundTintList(context.getResources().getColorStateList(R.color.Robins_Egg_Blue));
                        statusRibbon.setBackgroundTintList(context.getResources().getColorStateList(R.color.Chartreuse));
                        statusRibbon.setText("Reserved");
                    }
                } else if (myCourseListAndStatus != null
                        && myCourseListAndStatus.size() != 0
                        && context.getClass() == AllCoursesActivity.class) {
                    if (status.equals(Status.PENDING)) {
                        itemLayout.setBackgroundTintList(context.getResources().getColorStateList(R.color.skyBlue));
                    } else if (status.equals(Status.RESERVED)) {
                        itemLayout.setBackgroundTintList(context.getResources().getColorStateList(R.color.skyBlue));
                        statusRibbon.setBackgroundTintList(context.getResources().getColorStateList(R.color.Chartreuse));
                        statusRibbon.setText("Reserved");
                    } else {
                        itemLayout.setBackgroundTintList(context.getResources().getColorStateList(R.color.cerulean));
                        statusRibbon.setVisibility(View.GONE);
                    }
                } else {
                    itemLayout.setBackgroundTintList(context.getResources().getColorStateList(R.color.cerulean));
                    statusRibbon.setVisibility(View.GONE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            // setting item stringify data
            exerciseTitle.setText(course.getCourseTitle());
            String randomVariantNumber = Integer.toString((int) Math.round(Math.random() * (100 - 20 + 1) + 20));
            exerciseType.setText(randomVariantNumber + " Workouts");
            String randomHourNumber = Double.toString(Math.round(Math.random() * (150 - 30 + 1) + 20));
            exerciseTime.setText(randomHourNumber + " Hour");

            Glide.with(context).load(course.getPosterUrl()).into(courseIconIv);
        }


        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.item_layout_constraint) {
                onCourseClickListener.onCourseClicked(course, position, status);
            }
        }
    }
}
