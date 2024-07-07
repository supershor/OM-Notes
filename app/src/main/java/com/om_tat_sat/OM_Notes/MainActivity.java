package com.om_tat_sat.OM_Notes;

import static android.content.ContentValues.TAG;

import android.animation.LayoutTransition;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

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

import com.airbnb.lottie.LottieAnimationView;
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
    LinearLayout liner_layout_main_view;
    Toolbar toolbar;
    RelativeLayout liner_layout_sign_in_process;
    SharedPreferences email_address;
    LottieAnimationView loading;
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
        //tool bar setup
        toolbar=findViewById(R.id.toolbar_main_page);
        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);


        email_address=getSharedPreferences("Emails_and_Password",0);
        frameLayout=findViewById(R.id.frame_layout_main);
        liner_layout_main_view=findViewById(R.id.liner_layout_main_view);
        loading=findViewById(R.id.loading);
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
                        Log.e( "onClick:-----------","44444444444444444444");
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
                            liner_layout_main_view.setVisibility(View.VISIBLE);
                            liner_layout_sign_in_process.setVisibility(View.GONE);
                            FragmentManager fragmentManager=getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.frame_layout_main,new List_of_notes());
                            fragmentTransaction.commit();
                            Toast.makeText(MainActivity.this,email_password.login_try(credential.getId(),credential.getDisplayName()), Toast.LENGTH_SHORT).show();
                            Log.e("onActivityResult: >>>>>>>>",credential.getId().replace("@gmail.com",""));
                            Log.e("onActivityResult: >>>>>>>>",credential.getDisplayName().replace("@gmail.com",""));
                            Log.e( "onClick:-----------","555555555555555555");
                        }
                    }catch (Exception e){
                        Log.e( "onActivityResult:--------",e.toString());
                    }
                }
            }
        });


        if (email_address.contains("email")){
            liner_layout_main_view.setVisibility(View.VISIBLE);
            liner_layout_sign_in_process.setVisibility(View.GONE);
            FragmentManager fragmentManager=getSupportFragmentManager();
            FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout_main,new List_of_notes());
            fragmentTransaction.commit();
        }else {
            liner_layout_main_view.setVisibility(View.GONE);
            liner_layout_sign_in_process.setVisibility(View.VISIBLE);


            Google_sign_in_button=findViewById(R.id.ontap);
            Google_sign_in_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    loading.setVisibility(View.VISIBLE);
                    Log.e( "onClick:-----------","11111111111111111");


                    oneTapClient.beginSignIn(signUpRequest)
                            .addOnSuccessListener(MainActivity.this, new OnSuccessListener<BeginSignInResult>() {
                                @Override
                                public void onSuccess(BeginSignInResult result) {
                                    Log.e( "onClick:-----------","2222222222222222");
                                    loading.setVisibility(View.GONE);
                                    IntentSenderRequest intentSenderRequest=new IntentSenderRequest.Builder(result.getPendingIntent().getIntentSender()).build();
                                    activityResultLauncher.launch(intentSenderRequest);
                                }
                            })
                            .addOnFailureListener(MainActivity.this,new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    loading.setVisibility(View.GONE);
                                    Log.e( "onClick:-----------","333333333333333333333");
                                    // No Google Accounts found. Just continue presenting the signed-out UI.
                                    Log.d("Errors in Google Sign In",e.toString());
                                }
                            });
                }
            });
        }




    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.logout){
            SharedPreferences.Editor editor=email_address.edit();
            editor.clear();
            editor.apply();
            liner_layout_main_view.setVisibility(View.GONE);
            liner_layout_sign_in_process.setVisibility(View.VISIBLE);
            startActivity(new Intent(MainActivity.this,MainActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}