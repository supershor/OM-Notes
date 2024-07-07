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
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_main,new List_of_notes()).commit();
        super.onDestroy();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_edit_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        title=view.findViewById(R.id.title_notes);
        description=view.findViewById(R.id.notes_description);
        length=view.findViewById(R.id.description_characters);
        save=view.findViewById(R.id.save);
        back=view.findViewById(R.id.back);
        notes_id_holder=getActivity().getSharedPreferences("Add_edit_notes",0);
        email_address=getActivity().getSharedPreferences("Emails_and_Password",0);
        Notes_description_holders_using_SQLite notesDescriptionHoldersUsingSqLite=new Notes_description_holders_using_SQLite(getContext());


        if (notes_id_holder.contains("id")){
            id_str=notes_id_holder.getInt("id",-1)+"";
            title_str=notes_id_holder.getString("title",null);
            description_str=notesDescriptionHoldersUsingSqLite.fetch_desc_according_to_id(id_str);
            title.setText(title_str);
            description.setText(description_str);
            length.setText(description_str.length() +" Characters");
            notes_id_holder.edit().clear().apply();
        }

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

        save.setOnClickListener(new View.OnClickListener() {
            Notes_description_holders_using_SQLite notesDescriptionHoldersUsingSqLite=new Notes_description_holders_using_SQLite(getContext());
            @Override
            public void onClick(View v) {
                if (id_str!=null && id_str!="-1"){
                    if (title.getText() !=null && !title.getText().toString().isEmpty()){
                        if (description.getText()!=null && !description.getText().toString().isEmpty()){
                            notesDescriptionHoldersUsingSqLite.update_note(title.getText().toString(),description.getText().toString(),email_address.getString("email","-1"), Integer.valueOf(id_str));
                            Toast.makeText(getContext(), "File saved successfully", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getContext(), "Please Enter Description", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(getContext(), "Please Title Description", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    if (title.getText() !=null && !title.getText().toString().isEmpty()){
                        if (description.getText()!=null && !description.getText().toString().isEmpty()){
                            notesDescriptionHoldersUsingSqLite.add_new_note(title.getText().toString(),description.getText().toString(),email_address.getString("email","-1"));
                            Toast.makeText(getContext(), "File saved successfully", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getContext(), "Please Enter Description", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(getContext(), "Please Title Description", Toast.LENGTH_SHORT).show();
                    }
                }
                getActivity().getSupportFragmentManager().popBackStack();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_main,new List_of_notes()).commit();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_main,new List_of_notes()).commit();
            }
        });
    }
}