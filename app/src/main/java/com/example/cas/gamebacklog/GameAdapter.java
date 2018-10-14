package com.example.cas.gamebacklog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.ViewHolder> {

    private List<Game> mGames;
    final private GameClickListener mGameClickListener;

    public interface GameClickListener{
        void gameOnClick (int i);
    }


    public GameAdapter(List<Game> mGames, GameClickListener mGameClickListener) {
        this.mGames = mGames;
        this.mGameClickListener = mGameClickListener;
    }

    @NonNull
    @Override
    public GameAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater= LayoutInflater.from(context);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_card, parent, false);
// Return a new holder instance
        GameAdapter.ViewHolder viewHolder = new GameAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GameAdapter.ViewHolder holder, int position) {
        Game game =  mGames.get(position);
        holder.textViewName.setText(game.getName());
        holder.textViewConsole.setText(game.getConsole());
        holder.textViewStatus.setText(game.getStatus());
    }

    @Override
    public int getItemCount() {
        return mGames.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView textViewName;
        public TextView textViewConsole;
        public TextView textViewStatus;

        public View view;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.game_name);
            textViewConsole = itemView.findViewById(R.id.game_console);
            textViewStatus = itemView.findViewById(R.id.game_status);
            view = itemView;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mGameClickListener.gameOnClick(clickedPosition);
        }

    }

    public void swapList (List<Game> newList) {


        mGames = newList;

        if (newList != null) {

            // Force the RecyclerView to refresh

            this.notifyDataSetChanged();

        }

    }


}
