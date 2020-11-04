package com.example.loginapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.VoiceInteractor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText email,password;
    Button btn_login;
    String url="https://kotlaarabalikhan.com/app.kotlaarabalikhan.com/login_user_api.php";
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        casting();
        clickevents();
    }

    private void clickevents() {
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.getText().toString().isEmpty() || email.getText().toString().isEmpty() ) {
                    Toast.makeText(MainActivity.this, "Email or Password are Empty", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    progressDialog = new ProgressDialog(MainActivity.this);
                    progressDialog.setMessage("Please Waite");
                    progressDialog.show();
                    getdataformapi();
                }
            }
        });

    }

    private void getdataformapi() {
        StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    Toast.makeText(MainActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    if(!jsonObject.getString("message").isEmpty())
                    {
                        progressDialog.dismiss();
                    }
                    JSONArray jsonArray=jsonObject.getJSONArray("user");
                    JSONObject object=jsonArray.getJSONObject(0);
                    String id=":"+object.getString("id");
                    String fName=":"+object.getString("fname");
                    String lName=":"+object.getString("lname");
                    String phone=":"+object.getString("phone");
                    String email=":"+object.getString("email");
                    showMyDialog(id,fName,lName,phone,email);


                } catch (JSONException e) {
                    e.printStackTrace();
                } }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<String, String>();
                map.put("email",email.getText().toString());
                map.put("password",password.getText().toString());
                return map;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(request);
    }

    private void showMyDialog(String id, String fName, String lName, String phone, String email) {
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        View mView=getLayoutInflater().inflate(R.layout.customdialogbox,null);
        builder.setView(mView);
        TextView tv_id=mView.findViewById(R.id.tv_id);
        TextView tv_fName=mView.findViewById(R.id.tv_fname);
        TextView tv_lName=mView.findViewById(R.id.tv_lname);
        TextView tv_phone=mView.findViewById(R.id.tv_phone);
        TextView tv_email=mView.findViewById(R.id.tv_email);
        tv_id.setText(id);
        tv_fName.setText(fName);
        tv_lName.setText(lName);
        tv_phone.setText(phone);
        tv_email.setText(email);
        AlertDialog dialog=builder.create();
        dialog.show();


    }

    private void casting() {
        email=findViewById(R.id.et_email);
        password=findViewById(R.id.et_password);
        btn_login=findViewById(R.id.btn_login);
    }
}