package com.example.brian.orginder2;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class FacebookLoginActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MainFragment())
                    .commit();
        }
        FacebookSdk.sdkInitialize(getApplicationContext());
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.facebook.samples.hellofacebook",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class MainFragment extends Fragment {

        private CallbackManager mCallbackManager;
        private TextView mText, mToken,mID;
        private FacebookCallback<LoginResult> mCallback = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                AccessToken accessToken = loginResult.getAccessToken();

                Profile profile = Profile.getCurrentProfile();
                AccessToken accessToken2 = new AccessToken(accessToken.getToken(),"892507610824940",profile.getId(),null,null,null,null,null);

                if(profile != null){
                    //mText.setText("Welcome "+profile.getName());
                    //mID.setText("Facebook ID: "+profile.getId());
                    //mToken.setText("User Token: "+accessToken.getToken());
                    Log.d("facebook", profile.getId());
                    Log.d("facebook",accessToken2.getToken());
                }
                Intent intent;
                intent = new Intent(getActivity(),MainActivity.class);
                intent.putExtra("user_token",profile.getId());
                startActivity(intent);
            }

            @Override
            public void onCancel() {
                Log.d("facebook","cancel check");
                mText.setText("Canceled");
            }

            @Override
            public void onError(FacebookException e) {
                Log.d("error",e.toString());
                mText.setText(e.toString());
            }
        };
        public MainFragment() {
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
            mCallbackManager = CallbackManager.Factory.create();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            LoginButton loginButton = (LoginButton) rootView.findViewById(R.id.login_button);
            loginButton.setPublishPermissions("manage_pages");
            loginButton.setFragment(this);
            loginButton.registerCallback(mCallbackManager,mCallback);
            mText = (TextView) rootView.findViewById(R.id.face);
            mToken = (TextView) rootView.findViewById(R.id.token);
            mID = (TextView) rootView.findViewById(R.id.id);

            return rootView;
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            mCallbackManager.onActivityResult(requestCode,resultCode,data);
            Log.d("check","here");
        }
    }
}
