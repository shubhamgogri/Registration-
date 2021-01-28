package com.example.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import static java.nio.charset.StandardCharsets.*;
import static java.nio.charset.StandardCharsets.UTF_8;

public class MainActivity extends AppCompatActivity {

    private EditText first;
    private EditText last;
    private EditText email;
    private EditText pass;
    private Button register_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        first = findViewById(R.id.first_name);
        last = findViewById(R.id.last_name);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.password);
        register_button = findViewById(R.id.register_button);


        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!first.getText().toString().isEmpty() &&
                        !last.getText().toString().isEmpty() &&
                        !email.getText().toString().isEmpty() &&
                        !pass.getText().toString().isEmpty()) {

                    String first_name = first.getText().toString().trim();
                    String last_name = last.getText().toString().trim();
                    String email_str = email.getText().toString().trim();
                    String password = pass.getText().toString().trim();

                    Background bg = new Background(MainActivity.this);
                    bg.execute(first_name,last_name,email_str,password);
                } else {
                    Toast.makeText(MainActivity.this, "Enter All Fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public class Background extends AsyncTask <String, Void,String> {

        AlertDialog dialog;
        Context context;
        public Background(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            dialog = new AlertDialog.Builder(context).create();
            dialog.setTitle("Login Status");
        }
        @Override
        protected void onPostExecute(String s) {
            dialog.setMessage(s);
            dialog.show();
        }
        @Override
        protected String doInBackground(String... voids) {
            String result = "";
            String firstname = voids[0];
            String lastname = voids[1];
            String email= voids[2];
            String pass = voids[3];
            String connstr = "https://103.204.135.142:3306/db.php";

            try {
                URL url = new URL(connstr);
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod("POST");
                http.setDoInput(true);
                http.setDoOutput(true);

                OutputStream ops = http.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(ops,UTF_8));
                String data = URLEncoder.encode("firstname","UTF-8")+"="+ URLEncoder.encode(firstname,"UTF-8")
                        +"&&"+URLEncoder.encode("lastname","UTF-8")+"="+URLEncoder.encode(lastname,"UTF-8")
                        +"&&"+URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(email,"UTF-8")
                        +"&&"+URLEncoder.encode("pass","UTF-8")+"="+URLEncoder.encode(pass,"UTF-8");

                writer.write(data);
                writer.flush();
                writer.close();
                ops.close();

                InputStream ips = http.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(ips, ISO_8859_1));
                String line ="";
                while ((line = reader.readLine()) != null)
                {
                    result += line;
                }
                reader.close();
                ips.close();
                http.disconnect();
                Log.d("final", "doInBackground: " + result);

                return result;

            } catch (MalformedURLException e) {
                result = e.getMessage();
                Log.d("final url", "doInBackground: " + e);
            } catch (IOException e) {
                result = e.getMessage();
                Log.d("final e", "doInBackground: " + e);
            }
            return result;
        }
    }
}
