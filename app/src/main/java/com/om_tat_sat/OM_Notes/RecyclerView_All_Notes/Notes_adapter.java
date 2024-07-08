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

    //Here I will be using recycler adapter so that I can directly use it in fragments when needed
    //I will also be using interface OnClick_Recycler to handle clicks
    //Ill be storing data in arraylist_notes_description_id_holder
    Context context;
    ArrayList<Notes_title_description_id_holder> arraylist_notes_description_id_holder;
    OnClick_Recycler onClickRecycler;

    //Initializing recycler adapter
    public Notes_adapter(ArrayList<Notes_title_description_id_holder> arraylist_notes_description_id_holder, Context context, OnClick_Recycler onClickRecycler) {
        this.arraylist_notes_description_id_holder = arraylist_notes_description_id_holder;
        this.context = context;
        this.onClickRecycler = onClickRecycler;
    }


    //Inflating layout for recycler view
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.notes_xml_layout,parent,false);
        return new ViewHolder(view,onClickRecycler);
    }


    //Binding data to recycler view using view holder
    @Override
    public void onBindViewHolder(@NonNull Notes_adapter.ViewHolder holder, int position) {

        //Setting data to views
        holder.notes_title.setText(arraylist_notes_description_id_holder.get(position).title);
        holder.notes_description.setText(arraylist_notes_description_id_holder.get(position).description);

        //holder.notes_last_edited.setText(arraylist_notes_description_id_holder.get(position).last_edited);
    }

    //Returning size of arraylist_notes_description_id_holder
    @Override
    public int getItemCount() {
        return arraylist_notes_description_id_holder.size();
    }


    //View holder class for recycler view
    public class ViewHolder extends RecyclerView.ViewHolder {

        //Initializing views
        TextView notes_title,notes_description;
        TextView notes_last_edited;
        FloatingActionButton edit,delete;
        public ViewHolder(@NonNull View itemView, OnClick_Recycler onClickRecycler) {
            super(itemView);

            //Initializing views using find view by id
            notes_title=itemView.findViewById(R.id.title_notes);
            notes_description=itemView.findViewById(R.id.notes_description);

            //notes_last_edited=itemView.findViewById(R.id.notes_last_edited);
            edit=itemView.findViewById(R.id.edit);
            delete=itemView.findViewById(R.id.delete);

            //Setting clicks on views
            //edit button is assigned to 1 for clicked on position
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickRecycler.onClick(getAdapterPosition(),1);
                }
            });

            //delete button is assigned to 1 for clicked on position
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickRecycler.onClick(getAdapterPosition(),2);
                }
            });
        }
    }
}
