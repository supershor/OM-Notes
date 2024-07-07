package com.om_tat_sat.OM_Notes;

import static android.app.Activity.RESULT_OK;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.om_tat_sat.OM_Notes.SQLite_Helpers.Email_address_holders_using_SQLite;

public class Login_Google extends Fragment {
    private SignInClient oneTapClient;
    private BeginSignInRequest signUpRequest;
    com.google.android.gms.common.SignInButton Google_sign_in_button;
    SharedPreferences email_address;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        email_address=getActivity().getSharedPreferences("Emails_and_Password",0);





        Google_sign_in_button=getActivity().findViewById(R.id.ontap);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_login__google, container, false);
        Google_sign_in_button=view.findViewById(R.id.ontap);
        return view;
    }
}