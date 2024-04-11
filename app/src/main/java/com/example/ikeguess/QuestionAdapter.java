package com.example.ikeguess;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ikeguess.Activity.SimpleQuizzActivity;
import com.example.ikeguess.customClass.FlashCard;
import com.example.ikeguess.customClass.QuizzCreator;
import com.squareup.picasso.Picasso;

import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> implements View.OnClickListener {

    public QuestionAdapter(List<FlashCard> flashCards) {
        this.flashCards = flashCards;
    }

    private List<FlashCard> flashCards;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_flashcard, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FlashCard flashCard = flashCards.get(position);
        if (flashCard.mediaType.equals("image")) {
            holder.root.setMaxHeight(750);
            holder.root.setMinimumHeight(750);
            holder.title.setText(flashCard.goodAnswer);
            holder.answers.setVisibility(View.INVISIBLE);
            holder.difficulty.setVisibility(View.INVISIBLE);
            //holder.difficulty.setImageResource(android.R.drawable.ic_media_play);
            holder.media.setVisibility(View.VISIBLE);
            Picasso.get().load((String) flashCard.mediaUrl).into(holder.media);
        }
        else {
            holder.root.setMaxHeight(300);
            holder.root.setMinimumHeight(300);
            holder.title.setText(flashCard.mediaUrl.toUpperCase());
            holder.answers.setVisibility(View.VISIBLE);
            holder.difficulty.setVisibility(View.VISIBLE);
            holder.media.setVisibility(View.INVISIBLE);

        }
        holder.answers.setText(flashCard.goodAnswer + "");

        holder.itemView.setTag(flashCard);
        holder.itemView.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return flashCards.size();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rootItem:
                Context context = v.getContext();
                FlashCard f = (FlashCard) v.getTag();
                Intent currentInt = new Intent(context, SimpleQuizzActivity.class);
                QuizzCreator quizzCreator =  new QuizzCreator();
                quizzCreator.listQuizz.add(f);
                currentInt.putExtra("listQuizz", quizzCreator.listQuizz);
                context.startActivity(currentInt);
                break;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        final ImageView difficulty;
        final ImageView media;
        final TextView title;
        final TextView answers;
        final ConstraintLayout root;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.rootItem);
            difficulty = itemView.findViewById(R.id.difficultyImageView);
            title = itemView.findViewById(R.id.titleTextView);
            answers = itemView.findViewById(R.id.answersTextView);
            media = itemView.findViewById(R.id.mediaImageView);
        }
    }
}
