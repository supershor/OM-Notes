package com.om_tat_sat.OM_Notes.RecyclerView_All_Notes;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.om_tat_sat.OM_Notes.DataHolders.Notes_title_description_id_holder;
import com.om_tat_sat.OM_Notes.OnClicks.OnClick_Recycler;
import com.om_tat_sat.OM_Notes.R;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

public class Notes_adapter extends RecyclerView.Adapter<Notes_adapter.ViewHolder> {
    Context context;
    ArrayList<Notes_title_description_id_holder> arraylist_notes_description_id_holder;
    OnClick_Recycler onClickRecycler;

    public Notes_adapter(ArrayList<Notes_title_description_id_holder> arraylist_notes_description_id_holder, Context context, OnClick_Recycler onClickRecycler) {
        this.arraylist_notes_description_id_holder = arraylist_notes_description_id_holder;
        this.context = context;
        this.onClickRecycler = onClickRecycler;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.notes_xml_layout,parent,false);
        return new ViewHolder(view,onClickRecycler);
    }

    @Override
    public void onBindViewHolder(@NonNull Notes_adapter.ViewHolder holder, int position) {
        holder.notes_title.setText(arraylist_notes_description_id_holder.get(position).title);
        holder.notes_description.setText(arraylist_notes_description_id_holder.get(position).description);
        //holder.notes_last_edited.setText(arraylist_notes_description_id_holder.get(position).last_edited);
        Log.e( "onBindViewHolder: ",arraylist_notes_description_id_holder.get(position).id.toString()+"-->>"+arraylist_notes_description_id_holder.get(position).title);
    }

    @Override
    public int getItemCount() {
        return arraylist_notes_description_id_holder.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView notes_title,notes_description;
        TextView notes_last_edited;
        FloatingActionButton edit,delete;
        public ViewHolder(@NonNull View itemView, OnClick_Recycler onClickRecycler) {
            super(itemView);
            notes_title=itemView.findViewById(R.id.title_notes);
            notes_description=itemView.findViewById(R.id.notes_description);
            //notes_last_edited=itemView.findViewById(R.id.notes_last_edited);
            edit=itemView.findViewById(R.id.edit);
            delete=itemView.findViewById(R.id.delete);

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, getAdapterPosition()+"--"+"edit", Toast.LENGTH_SHORT).show();
                    onClickRecycler.onClick(getAdapterPosition(),1);
                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, getAdapterPosition()+"--"+"delete", Toast.LENGTH_SHORT).show();
                    onClickRecycler.onClick(getAdapterPosition(),2);
                }
            });
        }
    }
}
