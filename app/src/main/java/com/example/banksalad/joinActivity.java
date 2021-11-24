package com.example.banksalad;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class joinActivity extends AppCompatActivity {
    private EditText et_id, et_pass, et_name, et_birth,et_height,et_weight;
    private Button btn_register,validateButton;
    RadioButton selectedRadioButton;
    private String userGender;
    private AlertDialog dialog;
    private boolean validate=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join);
        //아이디 값 찾아주기
        et_id=findViewById(R.id.join_id);
        et_pass=findViewById(R.id.join_password);
        et_name=findViewById(R.id.join_name);
        et_birth=findViewById(R.id.join_birth);
        et_height=findViewById(R.id.join_height);
        et_weight=findViewById(R.id.join_weight);
        validateButton=findViewById(R.id.validateButton);

        RadioGroup genderGroup = (RadioGroup)findViewById(R.id.sex);
        int genderGroupID = genderGroup.getCheckedRadioButtonId();
        userGender = ((RadioButton)findViewById(genderGroupID)).getText().toString();//초기화 값을 지정해줌

       //라디오버튼이 눌리면 값을 바꿔주는 부분
        genderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                RadioButton genderButton = (RadioButton)findViewById(i);
                userGender = genderButton.getText().toString();
            }
        });

        validateButton.setOnClickListener(new View.OnClickListener() {//id중복체크
            @Override
            public void onClick(View view) {
                String userID=et_id.getText().toString();
                if(validate)
                {
                    return;
                }
                if(userID.equals("")){
                    AlertDialog.Builder builder=new AlertDialog.Builder( joinActivity.this );
                    dialog=builder.setMessage("아이디는 빈 칸일 수 없습니다")
                            .setPositiveButton("확인",null)
                            .create();
                    dialog.show();
                    return;
                }
                Response.Listener<String> responseListener=new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse=new JSONObject(response);
                            boolean success=jsonResponse.getBoolean("success");
                            if(success){
                                AlertDialog.Builder builder=new AlertDialog.Builder( joinActivity.this );
                                dialog=builder.setMessage("사용할 수 있는 아이디입니다.").setPositiveButton("확인",null).create();
                                dialog.show();
                                et_id.setEnabled(false);
                                validate=true;

                            }
                            else{
                                AlertDialog.Builder builder=new AlertDialog.Builder( joinActivity.this );
                                dialog=builder.setMessage("사용할 수 없는 아이디입니다.")
                                        .setNegativeButton("확인",null)
                                        .create();
                                dialog.show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                ValidateRequest validateRequest=new ValidateRequest(userID,responseListener);
                RequestQueue queue= Volley.newRequestQueue(joinActivity.this);
                queue.add(validateRequest);

            }
        });


        btn_register=findViewById(R.id.lets_join);
        btn_register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //editText에 입력되어있는 값을 get(가져온다)해온다
                String userID=et_id.getText().toString();
                String userPass=et_pass.getText().toString();
                String userName=et_name.getText().toString();
                String userBirth=et_birth.getText().toString();


                Double userHeight= Double.parseDouble(et_height.getText().toString());
                Double userweight= Double.parseDouble(et_weight.getText().toString());

                Response.Listener<String> responseListener=new Response.Listener<String>() {//volley
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jasonObject=new JSONObject(response);//Register2 php에 response
                            boolean success=jasonObject.getBoolean("success");//Register2 php에 sucess
                            if(success){ // 회원가입이 가능한다면
                                AlertDialog.Builder builder = new AlertDialog.Builder(joinActivity.this);
                                dialog = builder.setMessage("Register Your ID")
                                        .setPositiveButton("OK", null)
                                        .create();
                                dialog.show();
                                finish();
                            }else{// 회원가입이 안된다면
                                AlertDialog.Builder builder = new AlertDialog.Builder(joinActivity.this);
                                dialog = builder.setMessage("Register fail")
                                        .setNegativeButton("OK", null)
                                        .create();
                                dialog.show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                //서버로 volley를 이용해서 요청을 함
                joinRequest registerRequest=new joinRequest(userID,userPass, userName, userBirth,userHeight,userweight,userGender,responseListener);
                RequestQueue queue= Volley.newRequestQueue(joinActivity.this);
                queue.add(registerRequest);
            }
        });
    }

}