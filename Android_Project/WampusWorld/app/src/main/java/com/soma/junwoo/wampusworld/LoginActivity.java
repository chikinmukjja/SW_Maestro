package com.soma.junwoo.wampusworld;

import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {


    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }

    @Override
    protected void onStart() {
        super.onStart();

        callbackManager = CallbackManager.Factory.create();

        LoginButton facebookLoginButton = (LoginButton) findViewById(R.id.facebook_login_button);
        List<String> permissionNeeds = Arrays.asList("user_photos", "email", "user_birthday", "public_profile");

        if (facebookLoginButton != null) {
            facebookLoginButton.setReadPermissions(permissionNeeds);

            facebookLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(final LoginResult result) {

                    GraphRequest request;

                    request = GraphRequest.newMeRequest(result.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                        @Override
                        public void onCompleted(JSONObject user, GraphResponse response) {

                            if (response.getError() != null) {

                            } else {
                                Log.i("TAG", "user: " + user.toString());
                                Log.i("TAG", "userid: " + user.optString("id"));
                                Log.i("TAG", "AccessToken: " + result.getAccessToken().getToken());
                                setResult(RESULT_OK);

                                Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(),
                                        PlayActivity.class);
                                intent.putExtra("id",user.optString("id"));
                                intent.putExtra("name",user.optString("name"));
                                startActivity(intent);
                            }

                        }

                    });

                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id,name,email,gender,birthday");
                    request.setParameters(parameters);
                    request.executeAsync();
                }

                @Override
                public void onError(FacebookException error) {
                    Log.e("test", "Error: " + error);
                    finish();
                }

                @Override
                public void onCancel() {
                    finish();
                }
            });
        }
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        //Toast.makeText(getApplicationContext(), "onStop 로그 아웃", Toast.LENGTH_LONG).show();
        LoginManager.getInstance().logOut();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}
