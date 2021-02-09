package ir.faez.gymapp.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ir.faez.gymapp.R;
import ir.faez.gymapp.data.model.Course;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {

    // Instance fields
    private Context context;
    private List<Course> courses;
    private LayoutInflater layoutInflater;
    private OnCourseClickListener onCourseClickListener;

    // Constructor
    public CourseAdapter(Context context, List<Course> courses,
                         OnCourseClickListener onCourseClickListener) {
        this.context = context;
        this.courses = courses;
        this.onCourseClickListener = onCourseClickListener;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_course, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setItemData(position);
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ConstraintLayout itemLayout;
        public ImageView courseIconIv;
        public TextView exerciseTitle;
        public TextView exerciseType;
        public TextView exerciseTime;
        private int position;
        private Course course;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemLayout = itemView.findViewById(R.id.item_layout_constraint);
            courseIconIv = itemView.findViewById(R.id.exercise_icon_iv);
            exerciseTitle = itemView.findViewById(R.id.exercise_title_tv);
            exerciseType = itemView.findViewById(R.id.exercise_variant_tv);
            exerciseTime = itemView.findViewById(R.id.exercise_time_tv);

            invokeOnClickListeners();
        }

        private void invokeOnClickListeners() {
            itemLayout.setOnClickListener(this);
        }


        public void setItemData(int position) {
            this.position = position;
            Course course = courses.get(position);

            switch (courses.get(position).getStatus()) {
                case "RESERVED":
                    itemLayout.setBackgroundTintList(context.getResources().getColorStateList(R.color.Robins_Egg_Blue));
                    break;
                case "NOT_RESERVED":
                    itemLayout.setBackgroundTintList(context.getResources().getColorStateList(R.color.Hollywood_Cerise));
                    break;
            }

            // setting item stringify data
            exerciseTitle.setText(courses.get(position).getCourseTitle());

            String randomVariantNumber = Double.toString(Math.round(Math.random() * (100 - 20 + 1) + 20));
            exerciseType.setText(randomVariantNumber + " Workouts");

            String randomHourNumber = Double.toString(Math.round(Math.random() * (150 - 30 + 1) + 20));
            exerciseTime.setText(randomHourNumber + " Hour");
        }


        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.item_layout_constraint:
                    onCourseClickListener.onCourseClicked(course, position);
                    break;
            }
        }
    }
}
