package org.altervista.ultimaprovaprimadi.ciromelody.fotogreenpass;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final int OPEN_DOCUMENT_CODE = 2;
    private ImageView immaginegreenpass;
    private TextView urifoto;
    private Button bottone_sceli_foto;
    private Button bottone_cancella_percorso_file;
    SharedPreferences sharedPreferences;
    String percorso_green_pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        percorso_green_pass = sharedPreferences.getString("key_percorso_green_pass", "percorso_green_pass");
        immaginegreenpass=findViewById(R.id.imageview_green);
        urifoto=findViewById(R.id.textView);

        Uri myUri = Uri.parse(percorso_green_pass);
        immaginegreenpass.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        immaginegreenpass.setImageURI(myUri);
        urifoto.setText(percorso_green_pass);

        bottone_sceli_foto=findViewById(R.id.button);
        bottone_sceli_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selezionaFotoPerContato(v);
            }
        });
        bottone_cancella_percorso_file=findViewById(R.id.button3);
        bottone_cancella_percorso_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //immaginegreenpass.setScaleType(ImageView.ScaleType.FIT_END);
                immaginegreenpass.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                immaginegreenpass.setImageResource(R.drawable.fotogreenpass);
                //Toast.makeText(getApplicationContext(),"cancella percorso file:da fare",Toast.LENGTH_LONG).show();
            }
        });
    }



    public void selezionaFotoPerContato(View view) {
        if (Build.VERSION.SDK_INT <19){
            Intent intent = new Intent();
            intent.setType("*/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, OPEN_DOCUMENT_CODE);
        } else {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, OPEN_DOCUMENT_CODE);}

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {

        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == OPEN_DOCUMENT_CODE && resultCode == RESULT_OK) {
            Uri imageUri;
            if (resultData != null) {
                imageUri = resultData.getData();


                if (Build.VERSION.SDK_INT < 19) {

                } else {
                    final int takeFlags = resultData.getFlags()
                            & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                            | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                    try {
                        getApplication().getContentResolver().takePersistableUriPermission(imageUri, takeFlags);
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }
                    // this is the image selected by the user
                }

                urifoto.setText(imageUri.toString());
                //immaginecontatto.setScaleType(ImageView.ScaleType.CENTER_CROP);
                immaginegreenpass.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
               // immaginegreenpass.setScaleType(ImageView.ScaleType.FIT_END);
                immaginegreenpass.setImageURI(imageUri);
                // questo flag e' necessario per versioni sdk >19

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("key_percorso_green_pass", imageUri.toString());
                editor.commit();
            }
        }

    }
    @Override
    public void onBackPressed() {
   /*     if (interstitial != null && interstitial.isLoaded()) {
            if((Build.DEVICE.contains("HWPRA-H"))||(Build.DEVICE.contains("j5"))){
           }else {
               interstitial.show();
        }
            //interstitial.show();
        }*/
        if(isNetworkAvailable()){
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://ultimaprovaprimadi.altervista.org/le-mie-app-android/"));
            startActivity(browserIntent);

        }
        super.onBackPressed();
    }
    protected  boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}