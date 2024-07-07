package com.om_tat_sat.OM_Notes;

import static android.content.ContentValues.TAG;

import android.animation.LayoutTransition;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultCaller;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.om_tat_sat.OM_Notes.SQLite_Helpers.Email_address_holders_using_SQLite;

public class MainActivity extends AppCompatActivity {
    FrameLayout frameLayout;
    LinearLayout liner_layout_sign_in_process;
    SharedPreferences email_address;
    private SignInClient oneTapClient;
    private BeginSignInRequest signUpRequest;
    com.google.android.gms.common.SignInButton Google_sign_in_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        email_address=getSharedPreferences("Emails_and_Password",0);
        frameLayout=findViewById(R.id.frame_layout_main);
        liner_layout_sign_in_process=findViewById(R.id.liner_layout_sign_in_process);

        oneTapClient = Identity.getSignInClient(MainActivity.this);
        signUpRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        // Your server's client ID, not your Android client ID.
                        .setServerClientId(getString(R.string.web_client_id))
                        // Show all accounts on the device.
                        .setFilterByAuthorizedAccounts(false)
                        .build())
                .build();
        ActivityResultLauncher<IntentSenderRequest> activityResultLauncher=registerForActivityResult(new ActivityResultContracts.StartIntentSenderForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult o) {
                if (o.getResultCode()==RESULT_OK){
                    try {
                        SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(o.getData());
                        String idToken = credential.getGoogleIdToken();
                        if (idToken != null) {
                            // Got an ID token from Google. Use it to authenticate
                            // with your backend.
                            Log.e("onActivityResult: >>>>>>>>",credential.getId().replace("@gmail.com",""));
                            Log.e("onActivityResult: >>>>>>>>",credential.getDisplayName().replace("@gmail.com",""));
                            SharedPreferences.Editor editor=email_address.edit();
                            editor.putString("email",credential.getId());
                            editor.putString("password",credential.getDisplayName());
                            editor.apply();
                            Email_address_holders_using_SQLite email_password =new Email_address_holders_using_SQLite(MainActivity.this);
                            Toast.makeText(MainActivity.this,email_password.login_try(credential.getId(),credential.getDisplayName()), Toast.LENGTH_SHORT).show();
                            Log.e("onActivityResult: >>>>>>>>",credential.getId().replace("@gmail.com",""));
                            Log.e("onActivityResult: >>>>>>>>",credential.getDisplayName().replace("@gmail.com",""));
                        }
                    }catch (Exception e){
                        Log.e( "onActivityResult:--------",e.toString());
                    }
                }
            }
        });


        if (email_address.contains("email")){
            //
        }else {
            frameLayout.setVisibility(View.GONE);
            liner_layout_sign_in_process.setVisibility(View.VISIBLE);


            Google_sign_in_button=findViewById(R.id.ontap);
            Google_sign_in_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    oneTapClient.beginSignIn(signUpRequest)
                            .addOnSuccessListener(MainActivity.this, new OnSuccessListener<BeginSignInResult>() {
                                @Override
                                public void onSuccess(BeginSignInResult result) {
                                    IntentSenderRequest intentSenderRequest=new IntentSenderRequest.Builder(result.getPendingIntent().getIntentSender()).build();
                                    activityResultLauncher.launch(intentSenderRequest);
                                }
                            })
                            .addOnFailureListener(MainActivity.this,new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // No Google Accounts found. Just continue presenting the signed-out UI.
                                    Log.d("Errors in Google Sign In",e.toString());
                                }
                            });
                }
            });
        }




    }
}