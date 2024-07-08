package com.om_tat_sat.OM_Notes.OnClicks;

public interface OnClick_Recycler {
    //OnClick listener are used as interface for recycler view and helps know position and item tapped on
    //position will be the index of the item and clicked on will be the edit or delete button tapped on
    void onClick(int position,int clicked_on);
}
