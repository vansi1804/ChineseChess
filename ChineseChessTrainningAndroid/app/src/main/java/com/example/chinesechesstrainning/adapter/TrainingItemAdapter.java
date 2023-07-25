package com.example.chinesechesstrainning.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chinesechesstrainning.R;
import com.example.chinesechesstrainning.model.training.TrainingDTO;

import java.util.ArrayList;

public class TrainingItemAdapter extends RecyclerView.Adapter<TrainingItemAdapter.ViewHolder> {

    public interface IMatchItemOnClick {
        void setClick(TrainingDTO trainingDTOClicked);
    }

    private final Context context;
    private final ArrayList<TrainingDTO> trainingDTOS;
    private final IMatchItemOnClick matchItemOnClick;

    public TrainingItemAdapter(ArrayList<TrainingDTO> trainingDTOS, Context context, IMatchItemOnClick matchItemOnClick) {
        this.trainingDTOS = trainingDTOS;
        this.context = context;
        this.matchItemOnClick = matchItemOnClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View matchItemView = inflater.inflate(R.layout.activity_training_item,parent,false);
        return new ViewHolder(matchItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(trainingDTOS.get(position), matchItemOnClick);
    }

    @Override
    public int getItemCount() {
        return trainingDTOS.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView title;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_training_title);
        }
        public void bind(final TrainingDTO trainingDTOItem, final IMatchItemOnClick matchItemOnClick) {
            title.setText(trainingDTOItem.getTitle().replaceAll("Tuyển tập","Tuyển tập\n"));
            itemView.setOnClickListener(v -> matchItemOnClick.setClick(trainingDTOItem));
        }
    }
}
