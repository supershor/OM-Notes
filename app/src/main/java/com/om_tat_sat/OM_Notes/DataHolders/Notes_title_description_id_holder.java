package com.om_tat_sat.OM_Notes.DataHolders;

public class Notes_title_description_id_holder {
    //Here ill be storing all the titles and description of the notes
    //Ill also be storing id of them as it will allow me to directly manipulate data when needed
    public String title;
    public String description;
    public Integer id;

    public Notes_title_description_id_holder(String description, Integer id, String title) {
        this.description = description;
        this.id = id;
        this.title = title;
    }
}
