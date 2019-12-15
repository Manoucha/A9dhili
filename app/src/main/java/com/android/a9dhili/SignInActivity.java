package com.android.a9dhili;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.a9dhili.Models.User;
import com.android.a9dhili.Retrofit.INodeJS;
import com.android.a9dhili.Retrofit.RetrofitClient;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignInActivity extends AppCompatActivity {

    INodeJS myAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    EditText edt_email,edt_password;
    Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);




        //Init the API
        Retrofit retrofit = RetrofitClient.getInstance();

        myAPI = retrofit.create(INodeJS.class);

        //View
        btn_login = (Button) findViewById(R.id.btn_continueSignIn);

        edt_email = (EditText) findViewById(R.id.edt_email_login);
        edt_password = (EditText)findViewById(R.id.edt_pass_login);

        //Event
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser(edt_email.getText().toString(),edt_password.getText().toString());
            }
        });



    }

    private void loginUser(final String email, final String password)
    {
        compositeDisposable.add(myAPI.loginUser(email,password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        if(s.contains("encrypted_password"))
                        {

                            Call<User> call = myAPI.getUserByEmail(email);
                            call.enqueue(new Callback<User>() {
                                @Override
                                public void onResponse(Call<User> call, Response<User> response) {

                                    int statusCode = response.code();
                                    User user = response.body();
                                    Log.d("test User ","nom"+user.getNom());
                                    Log.d("test User ","prenom"+user.getPrenom());
                                    // set
                                    ((myApp) getApplication()).setUser(user);
                                }

                                @Override
                                public void onFailure(Call<User> call, Throwable t) {
                                    // Log error here since request failed
                                    Log.d("fail ","fail");
                                    Log.e("erreur",t.toString());
                                }

                            });



                            Toast.makeText(SignInActivity.this,"Login success",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignInActivity.this,ChoixActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else
                            Toast.makeText(SignInActivity.this,""+s,Toast.LENGTH_SHORT).show();
                    }
                })

        );
    }
    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }




        public void goToSignUp(View v)
    {
        Intent intent = new Intent(SignInActivity.this,SignUpActivity.class);
        startActivity(intent);
        finish();

    }
}
