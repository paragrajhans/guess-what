package com.neu.edu.numad17s_finalproject_ab_vs.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.neu.edu.numad17s_finalproject_ab_vs.R;
import com.neu.edu.numad17s_finalproject_ab_vs.model.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Created by ashishbulchandani on 13/04/17.
 */

public class LivePlayerAdapter extends RecyclerView.Adapter<LivePlayerAdapter.ViewHolder> {


    ArrayList<Player> playerList;
    Map<Integer,String> selectedPlayers = new HashMap<>();

    public LivePlayerAdapter(ArrayList<Player> playerList){

        this.playerList = playerList;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.live_player_list_rowitem, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final Player player = playerList.get(position);
        holder.name.setText(player.getName());
        holder.selected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    selectedPlayers.put(position,player.getRegId());
                else
                    selectedPlayers.remove(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public Map<Integer,String> getSelectedPlayerMap() {
        return selectedPlayers;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        public TextView name;
        public CheckBox selected;

        public ViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            selected = (CheckBox) view.findViewById(R.id.selected);
        }

    }
}
