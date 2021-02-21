package ir.faez.gymapp.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.List;

import ir.faez.gymapp.R;
import ir.faez.gymapp.data.model.Review;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    // Instance fields
    private Context context;
    private List<Review> reviewList;
    private LayoutInflater layoutInflater;

    // Constructor
    public ReviewAdapter(Context context, List<Review> reviews) {
        this.context = context;
        this.reviewList = reviews;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_review, parent, false);
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
        return reviewList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ConstraintLayout itemLayout;
        public TextView reviewOwner;
        public TextView reviewDateTime;
        public TextView reviewText;
        private int position;
        private Review review;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemLayout = itemView.findViewById(R.id.review_layout_constraint);
            reviewOwner = itemView.findViewById(R.id.review_owner_name_tv);
            reviewDateTime = itemView.findViewById(R.id.review_dateTime_tv);
            reviewText = itemView.findViewById(R.id.review_text_tv);


        }

        public void setItemData(int position) throws IOException {
            this.position = position;
            this.review = reviewList.get(position);
            String ownerName = review.getOwnerFullName().length() > 40 ? review.getOwnerFullName().substring(0, 40) : review.getOwnerFullName();

            reviewOwner.setText(ownerName);
            reviewDateTime.setText(review.getDateTime());
            reviewText.setText(review.getText());

        }
    }


}
