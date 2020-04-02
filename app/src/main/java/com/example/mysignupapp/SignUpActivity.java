package com.example.mysignupapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SignUpActivity extends AppCompatActivity {

    private static final int REQUEST_ACT = 400;

    private EditText signup_id;
    private EditText signup_pw;
    private EditText signup_pwcheck;
    private EditText signup_name;
    private TextView signup_adress1;
    private EditText signup_address2;
    private EditText signup_celphone;
    private EditText signup_phone;
    private EditText signup_email;
    private EditText signup_birth;
    private EditText signup_gender;
    private CheckBox signup_check1;
    private CheckBox signup_check2;

    private AsyncTask<String, String, String> signupTask;
    private String id;
    private String pw;
    private String pwcheck;
    private String name;
    private String address1;
    private String address2;
    private String celphone;
    private String phone;
    private String email;
    private String birth;
    private String gender;
    private String check1;
    private String check2;

    private String addresspost;
    private String resultArg2;
    private String addresslocal;
    private String resultArg4;
    private String address;
    private String todayDate;
    private String pkid;

    private String lastChar = "";
    private ImageView signup_setImage;
    private Button btn_searchAdd;
    private CheckBox ck_all;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signup_id = findViewById(R.id.signup_id);
        signup_pw = findViewById(R.id.signup_pw);
        signup_pwcheck = findViewById(R.id.signup_pwcheck);
        signup_name = findViewById(R.id.signup_name);
        signup_adress1 = findViewById(R.id.signup_address1);
        signup_address2 = findViewById(R.id.signup_address2);
        signup_celphone = findViewById(R.id.signup_celphone);
        signup_phone = findViewById(R.id.signup_phone);
        signup_email = findViewById(R.id.signup_email);
        signup_birth = findViewById(R.id.signup_birth);
        signup_gender = findViewById(R.id.signup_gender);
        signup_check1 = findViewById(R.id.signup_check1);
        signup_check2 = findViewById(R.id.signup_check2);
        signup_setImage = findViewById(R.id.signup_setImage);
        btn_searchAdd = findViewById(R.id.btn_searchAdd);
        ck_all = findViewById(R.id.ck_all);

        signup_pwcheck.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(signup_pw.getText().toString().equals(signup_pwcheck.getText().toString())){
                    signup_setImage.setImageResource(R.drawable.ic_check);
                }else{
                    signup_setImage.setImageResource(R.drawable.ic_wrong);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        signup_celphone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                int digits = signup_celphone.getText().toString().length();
                if (digits > 1)
                    lastChar = signup_celphone.getText().toString().substring(digits - 1);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int digits = signup_celphone.getText().toString().length();
                Log.d("LENGTH", "" + digits);
                if (!lastChar.equals("-")) {
                    if (digits == 3 || digits == 8) {
                        signup_celphone.append("-");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        btn_searchAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, DaumWebViewActivity.class);
                startActivityForResult(intent, REQUEST_ACT);
            }
        });
        btn_searchAdd.setMinWidth(80);

        findViewById(R.id.btn_signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = signup_id.getText().toString();
                pw = signup_pw.getText().toString();
                pwcheck = signup_pwcheck.getText().toString();
                name = signup_name.getText().toString();
                address1 = signup_adress1.getText().toString();
                address2 = signup_address2.getText().toString();
                celphone = signup_celphone.getText().toString();
                phone = signup_phone.getText().toString();
                email = signup_email.getText().toString();
                birth = signup_birth.getText().toString();
                gender = signup_gender.getText().toString();
                check1 = signup_check1.getText().toString();
                check2 = signup_check2.getText().toString();
                pkid = new SimpleDateFormat("yyyyMMdd").format(new Date())+celphone.substring(5,8)+celphone.substring(celphone.length()-4);

                todayDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

                if(signup_check1.isChecked() && signup_check2.isChecked()) {
                    signupTask = new SignUpTask().execute();
                }else if(!signup_check1.isChecked() && signup_check2.isChecked()){
                    Toast.makeText(SignUpActivity.this, "이용약관에 동의해 주세요", Toast.LENGTH_SHORT).show();
                }else if(signup_check1.isChecked() && !signup_check2.isChecked()){
                    Toast.makeText(SignUpActivity.this, "개인정보이용에 동의해 주세요", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(SignUpActivity.this, "약관에 모두 동의해 주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ck_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ck_all.isChecked()){
                    signup_check1.setChecked(true);
                    signup_check2.setChecked(true);
                }else{
                    signup_check1.setChecked(false);
                    signup_check2.setChecked(false);
                }
            }
        });
        signup_check1.setOnClickListener(onCheckBoxClickListener);
        signup_check2.setOnClickListener(onCheckBoxClickListener);

        findViewById(R.id.btn_termsofservice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this,TermsofserviceActivity.class);
                intent.putExtra("agreement","terms");
                startActivity(intent);
            }
        });
        findViewById(R.id.btn_privacy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this,TermsofserviceActivity.class);
                intent.putExtra("agreement","privacy");
                startActivity(intent);
            }
        });


    }

    private View.OnClickListener onCheckBoxClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if(isAllChecked()){
                ck_all.setChecked(true);

            }else{
                ck_all.setChecked(false);
            }

        }
    };


    private boolean isAllChecked(){
        return (signup_check1.isChecked() && signup_check2.isChecked()) ?  true :  false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            Toast.makeText(SignUpActivity.this, "결과가 성공이 아님.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (requestCode == REQUEST_ACT) {
            addresspost = data.getStringExtra("result_arg1");
            resultArg2 = data.getStringExtra("result_arg2");
            addresslocal = data.getStringExtra("result_arg3");
            resultArg4 = data.getStringExtra("result_arg4");
            address = resultArg2 + " ( " + resultArg4 + " )";
            signup_adress1.setText(address);
            signup_address2.setVisibility(View.VISIBLE);

        } else {
            Toast.makeText(SignUpActivity.this, "REQUEST_ACT가 아님", Toast.LENGTH_SHORT).show();
        }
    }


    public class SignUpTask extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
        }
        @Override
        protected String doInBackground(String... strings) {
            if (isCancelled()) {
                return null;
            }
            signupQuery();
            return null;
        }
        protected void onPostExecute(String result) {

            AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
            builder.setTitle("인사말").setMessage("회원가입이 완료되었습니다\n감사합니다.");
            final AlertDialog alertDialog = builder.create();
            alertDialog.show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    alertDialog.dismiss();
                    startActivity(new Intent(SignUpActivity.this,MainActivity.class));
                }
            }, 3000);


        }
        protected void onCancelled() {
            super.onCancelled();
        }
    }

    public void signupQuery() {
        Connection connection = null;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:jtds:sqlserver://sql16ssd-005.localnet.kr/surefin1_db2020", "surefin1_db2020", "mam3535@@");
            PreparedStatement ps = connection.prepareStatement("INSERT INTO  Su_암내뷔고객정보(PKID,아이디,비밀번호,고객명,성별,생년월일,가입일자,전화,주소,우편번호,지역명,이메일)VALUES (?,?,?,?,?,?,?,?,?,?,?,?)");

            ps.setString(1, pkid);
            ps.setString(2, id);
            ps.setString(3, pw);
            ps.setString(4, name);
            ps.setString(5, gender);
            ps.setString(6, birth);
            ps.setString(7, todayDate);
            ps.setString(8, celphone);
            ps.setString(9, address1+address2);
            ps.setString(10, addresspost);
            ps.setString(11, addresslocal);
            ps.setString(12, email);

            ps.executeUpdate();
            ps.close();
            connection.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
