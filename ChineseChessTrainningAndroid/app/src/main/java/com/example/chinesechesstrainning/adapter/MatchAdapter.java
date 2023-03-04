package com.example.chinesechesstrainning.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chinesechesstrainning.R;
import com.example.chinesechesstrainning.model.Match;

import java.util.ArrayList;

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.ViewHolder> {

    public interface IMatchItemOnClick {
        void setClick(Match matchClicked);
    }

    private final Context context;
    private final ArrayList<Match> matches;
    private final IMatchItemOnClick matchItemOnClick;
    public MatchAdapter(ArrayList<Match> matches, Context context, IMatchItemOnClick matchItemOnClick) {
        this.matches = matches;
        this.context = context;
        this.matchItemOnClick = matchItemOnClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View matchItemView = inflater.inflate(R.layout.activity_match_item,parent,false);
        return new ViewHolder(matchItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(matches.get(position), matchItemOnClick);
    }

    @Override
    public int getItemCount() {
        return matches.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView title;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.match_title);
        }
        public void bind(final Match matchItem, final IMatchItemOnClick matchItemOnClick) {
            title.setText(matchItem.getName().replaceAll("Tuyển tập","Tuyển tập\n"));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    matchItemOnClick.setClick(matchItem);
                }
            });
        }
    }
}
