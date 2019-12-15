package com.android.a9dhili;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.a9dhili.Retrofit.INodeJS;
import com.android.a9dhili.Retrofit.RetrofitClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class SignUpActivity extends AppCompatActivity {

    INodeJS myAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    EditText edt_prenom,edt_nom,edt_emailR,edt_password,edt_passwordConf;
    CheckBox ck_location;
    Button btn_register;

    //location
    int PERMISSION_ID = 44;
    //Now we'll use the actual Fused Location Provider API to get users current position. For this you should declare a variable first
    FusedLocationProviderClient mFusedLocationClient;
    private double latitude ;
    private double longitude ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        edt_nom = (EditText)findViewById(R.id.edt_nom);
        edt_prenom = (EditText)findViewById(R.id.edt_prenom);
        edt_emailR = (EditText)findViewById(R.id.edt_email);
        edt_password= (EditText)findViewById(R.id.edt_password);
        edt_passwordConf= (EditText)findViewById(R.id.edt_confirmPassword);
        ck_location=(CheckBox)findViewById(R.id.location);
        btn_register = findViewById(R.id.btn_continueSignIn);

        //location
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

    }

    public void goToSignIn(View v)
    {
        Intent intent = new Intent(SignUpActivity.this,SignInActivity.class);
        startActivity(intent);
        finish();
    }
    public void goToLogIn(View v)
    {
        if(ck_location.isChecked())
        {
            //location
            getLastLocation();
            //Init the API
            Retrofit retrofit = RetrofitClient.getInstance();

           myAPI = retrofit.create(INodeJS.class);
            compositeDisposable.add(myAPI.registerUser(edt_nom.getText().toString(),edt_prenom.getText().toString(),edt_emailR.getText().toString(),edt_password.getText().toString(),latitude,longitude)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Exception {
                            Toast.makeText(SignUpActivity.this,"register succefully"+s,Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignUpActivity.this,SignInActivity.class);
                            startActivity(intent);
                            finish();

                        }
                    }));

        }else
        {
            Toast.makeText(SignUpActivity.this,"cocher la position",Toast.LENGTH_SHORT).show();
        }





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

    //location Methods


    //use the API and return the last recorder location information of the device. Also this method will check first if our permission is granted or not and if the location setting is turned on.
    @SuppressLint("MissingPermission")
    private void getLastLocation(){
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {
                                    latitude=location.getLatitude();
                                    longitude=location.getLongitude();
                                    Log.d("latitude est : ",""+latitude);
                                    Log.d("longitude est : ",""+longitude);
                                }
                            }
                        }
                );
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }

    //Pour éviter ces rares cas location == null, nous avons appelé une nouvelle méthode requestNewLocationData()
    // permettant d’enregistrer les informations de localisation au moment de l’exécution
    @SuppressLint("MissingPermission")
    private void requestNewLocationData(){

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }
    //When an update of location receives it'll call a callBack method named mLocationCallback
    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            latitude=mLastLocation.getLatitude();
            longitude=mLastLocation.getLongitude();
        }
    };

    //Cette méthode nous dira si oui ou non l'utilisateur nous accorder d'accès ACCESS_COARSE_LOCATIONet ACCESS_FINE_LOCATION.
    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    //Cette méthode demandera nos autorisations nécessaires à l'utilisateur si elles ne sont pas déjà accordées.
    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    //This will check if the user has turned on location from the setting, Cause user may grant the app to user location but if the location setting is off then it'll be of no use.
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }


    //This method is called when a user Allow or Deny our requested permissions. So it will help us to move forward if the permissions are granted.
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }

    }
}
