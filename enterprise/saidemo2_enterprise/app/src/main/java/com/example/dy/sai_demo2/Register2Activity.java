package com.example.dy.sai_demo2;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class Register2Activity extends AppCompatActivity {
    EditText edit_legal_name;
    EditText edit_legal_IDcard;
    //EditText edit_IDcard_image;
    ImageView edit_IDcard_image;
    String IDcard_image_path = "";
    EditText edit_email;
    EditText edit_tel;
    EditText edit_code;
    EditText edit_password;
    Button submit_btn;
    String enterprise_name;
    String num;
    String postcode;
    String location;
    static Register2Activity me;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);
        me = this;
        //沉浸式，从郭霖那炒的，我也懒得去看。
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        initview();
//        submit_btn = (Button) findViewById(R.id.submit_btn);
//        submit_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                    Intent intent = new Intent(Register2Activity.this, Register3Activity.class);
//
//                    Log.d("start3","start3" );
//                    startActivity(intent);
//                    Log.d("start3","open3" );
//                    finish();
//
//            }
//        });
    }

    private void initview(){
        enterprise_name = getIntent().getStringExtra("enterprise_name");
        num = getIntent().getStringExtra("num");
        postcode = getIntent().getStringExtra("postcode");
        location = getIntent().getStringExtra("location");

        edit_IDcard_image = (ImageView) findViewById(R.id.IDcard_image);
        edit_IDcard_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //调用相册
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });
        edit_legal_IDcard = (EditText) findViewById(R.id.legal_IDcard);
        edit_legal_name = (EditText) findViewById(R.id.legal_name);
        edit_email = (EditText) findViewById(R.id.email);
        edit_tel = (EditText) findViewById(R.id.tel);
        edit_code = (EditText) findViewById(R.id.code);
        edit_password = (EditText) findViewById(R.id.password);

        submit_btn = (Button) findViewById(R.id.submit_btn);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(IDcard_image_path.equals("") ||
                        edit_legal_IDcard.getText().toString().equals("") ||
                        edit_legal_name.getText().toString().equals("") ||
                        edit_password.getText().toString().equals("") ||
                        edit_email.getText().toString().equals("") ||
                        edit_tel.getText().toString().equals("") ||
                        edit_code.getText().toString().equals("")) {
                    Toast.makeText(Register2Activity.this,"请检查信息是否输入完整",Toast.LENGTH_SHORT).show();

                }else{
                    Intent intent = new Intent(Register2Activity.this, Register3Activity.class);
                    intent.putExtra("IDcard_image", IDcard_image_path);
                    intent.putExtra("legal_IDcard", edit_legal_IDcard.getText().toString());
                    intent.putExtra("legal_name", edit_legal_name.getText().toString());
                    intent.putExtra("password", edit_password.getText().toString());
                    intent.putExtra("email", edit_email.getText().toString());
                    intent.putExtra("tel", edit_tel.getText().toString());
                    intent.putExtra("code", edit_code.getText().toString());

                    intent.putExtra("location", location);
                    intent.putExtra("enterprise_name", enterprise_name);
                    intent.putExtra("num", num);
                    intent.putExtra("postcode", postcode);
                    Log.d("start3","start3" );
                    startActivity(intent);
                    Log.d("start3","open3" );
                    finish();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //获取图片路径
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            String imagePath = c.getString(columnIndex);
            Log.d("image_path",imagePath);
            //showImage(imagePath);
            Bitmap bm = BitmapFactory.decodeFile(imagePath);
            edit_IDcard_image.setImageBitmap(bm);
            IDcard_image_path = imagePath;
            c.close();
        }
    }
}
