package com.om_tat_sat.OM_Notes;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.om_tat_sat.OM_Notes.SQLite_Helpers.Notes_description_holders_using_SQLite;

public class Add_edit_notes extends Fragment {

    //This will be the global variables
    EditText title,description;
    TextView length;
    FloatingActionButton save;
    FloatingActionButton back;
    SharedPreferences notes_id_holder;
    SharedPreferences email_address;
    String title_str;
    String id_str;
    String description_str;

    @Override
    public void onDestroy() {

        //Trying to go back to all list fragment when user taps back or tries destroying the add new or edit fragments
        //Ill use Main Activity fragment manager and will try replacing current fragment with all list of notes
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_main,new List_of_notes()).commit();
        super.onDestroy();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Inflating edit notes fragment aka view
        return inflater.inflate(R.layout.fragment_add_edit_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Initializing all the views when my view is created so that we don't get null exception when invoking any type of function of that view
        title=view.findViewById(R.id.title_notes);
        description=view.findViewById(R.id.notes_description);
        length=view.findViewById(R.id.description_characters);
        save=view.findViewById(R.id.save);
        back=view.findViewById(R.id.back);

        //We will be accessing SharedPreferences to get notes if we trying to edit any notes
        //Ill also use email SharedPreferences from main activity in order to edit  any notes or to add new one
        notes_id_holder=getActivity().getSharedPreferences("Add_edit_notes",0);
        email_address=getActivity().getSharedPreferences("Emails_and_Password",0);

        //Notes_description_holders_using_SQLite being initialized
        Notes_description_holders_using_SQLite notesDescriptionHoldersUsingSqLite=new Notes_description_holders_using_SQLite(getContext());


        //Ill be checking if notes passed on this fragment contains id or not
        //And if yes then this fragment is called to edit notes and if not then its called to add new notes
        if (notes_id_holder.contains("id")){

            //initializing as well as setting text in title and description
            //will be fetching description using sql because SharedPreferences only allows up to 1000characters to be exported
            //also setting length of description_str in length
            //also clearing notes_id_holder so that next time someone creates or edits a new note the previous passed on data doesn't gets called automatically
            id_str=notes_id_holder.getInt("id",-1)+"";
            title_str=notes_id_holder.getString("title",null);
            description_str=notesDescriptionHoldersUsingSqLite.fetch_desc_according_to_id(id_str);
            title.setText(title_str);
            description.setText(description_str);
            length.setText(description_str.length() +" Characters");
            notes_id_holder.edit().clear().apply();
        }


        //adding text changed listeners in description and will be setting its current length in length textview
        description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                length.setText(s.length() +" Characters");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        //save button on click listener
        save.setOnClickListener(new View.OnClickListener() {
            Notes_description_holders_using_SQLite notesDescriptionHoldersUsingSqLite=new Notes_description_holders_using_SQLite(getContext());
            @Override
            public void onClick(View v) {

                //if id string is not null or is not -1 then we will edit the notes else we will add new note using sqlite
                if (id_str!=null && id_str!="-1"){

                    //checking if title of the edited note is not null and also checking its description and if yes then showing appropriate toast message
                    if (title.getText() !=null && !title.getText().toString().isEmpty()){
                        if (description.getText()!=null && !description.getText().toString().isEmpty()){

                            //Trying to update note in the sql using email and id and new title and description and if done successfully we will be heading back to all notes fragment
                            notesDescriptionHoldersUsingSqLite.update_note(title.getText().toString(),description.getText().toString(),email_address.getString("email","-1"), Integer.valueOf(id_str));
                            Toast.makeText(getContext(), "File saved successfully", Toast.LENGTH_SHORT).show();

                            //heading back to list of all notes
                            getActivity().getSupportFragmentManager().popBackStack();
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_main,new List_of_notes()).commit();
                        }
                        else {
                            Toast.makeText(getContext(), "Please Enter Description", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(getContext(), "Please Enter Title", Toast.LENGTH_SHORT).show();
                    }
                }else {

                    //checking if title of the new add note is not null and also checking its description and if yes then showing appropriate toast message
                    if (title.getText() !=null && !title.getText().toString().isEmpty()){
                        if (description.getText()!=null && !description.getText().toString().isEmpty()){

                            //Trying to add new note in the sql using email and id and new title and description and if done successfully we will be heading back to all notes fragment
                            notesDescriptionHoldersUsingSqLite.add_new_note(title.getText().toString(),description.getText().toString(),email_address.getString("email","-1"));
                            Toast.makeText(getContext(), "File saved successfully", Toast.LENGTH_SHORT).show();

                            //heading back to list of all notes
                            getActivity().getSupportFragmentManager().popBackStack();
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_main,new List_of_notes()).commit();
                        }
                        else {
                            Toast.makeText(getContext(), "Please Enter Description", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(getContext(), "Please Enter Title", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //back clicked listener
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //popping the backstack to keep main page free from too many replacements/ stack of fragments which can result in to many back clicks requirement to kill main activity
                //also going back to list of notes when back clicked using fragment replacement
                getActivity().getSupportFragmentManager().popBackStack();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_main,new List_of_notes()).commit();
            }
        });
    }
}