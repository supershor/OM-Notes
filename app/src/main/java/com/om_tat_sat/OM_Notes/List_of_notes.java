package com.om_tat_sat.OM_Notes;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.om_tat_sat.OM_Notes.DataHolders.Notes_title_description_id_holder;
import com.om_tat_sat.OM_Notes.OnClicks.OnClick_Recycler;
import com.om_tat_sat.OM_Notes.RecyclerView_All_Notes.Notes_adapter;
import com.om_tat_sat.OM_Notes.SQLite_Helpers.Notes_description_holders_using_SQLite;

import java.util.ArrayList;

public class List_of_notes extends Fragment implements OnClick_Recycler {

    //This will be the global variables
    RecyclerView recyclerView_notes;
    SharedPreferences email_address;
    FloatingActionButton add_new;
    Notes_description_holders_using_SQLite notes_description_holders_using_SQLite;
    ArrayList<Notes_title_description_id_holder>arraylist_notes_description_id_holder;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Initializing all the views when my view is created so that we don't get null exception when invoking any type of function of that view
        add_new=view.findViewById(R.id.add_new_note);
        add_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //This will be used to add new notes
                //We will be changing fragment to add new notes and will not be passing any data
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_main,new Add_edit_notes()).addToBackStack("change").commit();
            }
        });

        //Initializing recycler view
        recyclerView_notes=view.findViewById(R.id.recycler_view_notes);

        //Initializing SharedPreferences
        email_address=getActivity().getSharedPreferences("Emails_and_Password",0);

        //Initializing SQLite
        notes_description_holders_using_SQLite=new Notes_description_holders_using_SQLite(getContext());

        //Setting up list
        setlist();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Inflating list of notes fragment aka view
        return inflater.inflate(R.layout.fragment_list_of_notes, container, false);
    }

    @Override
    public void onClick(int position, int clicked_on) {

        //This will be used to delete and edit notes
        //2 is for delete and 1 is for edit
        if (clicked_on==2){

            //This will be used to delete notes
            //Alert dialog pop up to confirm the user wants to delete the note
            //will be setting title and message for alert dialog
            //Title as per notes title
            AlertDialog.Builder alert=new AlertDialog.Builder(getContext());
            alert.setTitle("Delete "+arraylist_notes_description_id_holder.get(position).title+" ?")
                    .setMessage("Are you sure yo want to delete this note ✍️(◔◡◔)\nYou wont be able to see it again ~_~")
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            //This will be used to delete notes from SQLite using id
                            notes_description_holders_using_SQLite.delete_note(arraylist_notes_description_id_holder.get(position).id);
                            //fetching list again
                            setlist();
                        }
                    })
                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            //This will be used to close alert dialog
                            dialog.dismiss();
                        }
                    });
            alert.show();
        }else {

            //This will be used to edit notes
            //We will be changing fragment to add new notes and will be passing data to it
            //We will be passing id, title and description of notes
            //We will be using SharedPreferences to store data
            //Ill also use email SharedPreferences from main activity in order to edit any notes or to add new one
            SharedPreferences.Editor editor=getActivity().getSharedPreferences("Add_edit_notes",0).edit();
            editor.putInt("id",arraylist_notes_description_id_holder.get(position).id);
            editor.putString("title",arraylist_notes_description_id_holder.get(position).title);
            editor.putString("description",arraylist_notes_description_id_holder.get(position).description);
            editor.apply();
            getActivity().getSupportFragmentManager().beginTransaction().add(R.id.frame_layout_main,new Add_edit_notes()).addToBackStack("change").commit();

        }
    }
    public void setlist(){

        //Setting up list
        //using Notes_description_holders_using_SQLite being initialized to fetch all the notes of current user using email address
        //setting up recycler view
        arraylist_notes_description_id_holder=notes_description_holders_using_SQLite.fetch_note(email_address.getString("email","-1"));
        Notes_adapter notes_adapter=new Notes_adapter(arraylist_notes_description_id_holder,getContext(),this::onClick);
        recyclerView_notes.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView_notes.setAdapter(notes_adapter);
    }
}