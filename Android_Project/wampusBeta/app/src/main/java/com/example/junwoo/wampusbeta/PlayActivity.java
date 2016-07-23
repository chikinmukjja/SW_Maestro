package com.example.junwoo.wampusbeta;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PlayActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textView1;
    private EditText nameField;
    private EditText passwordField;
    private EditText emailField;
    private ImageView image1;

    boolean first = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        // toast message
        String message = "onCreate !!" ;
        Toast toast = Toast.makeText(this,message,Toast.LENGTH_SHORT);
        toast.show();

        // log message
        Log.i("test","print i log !!");

        // widget button click
        Button button1 = (Button)findViewById(R.id.button1);
        button1.setOnClickListener(this);

        ImageButton button2 = (ImageButton)findViewById(R.id.imageButton1);
        button2.setOnClickListener(this);

        // text view
        textView1 = (TextView)findViewById(R.id.textView1);

        // edit text
        nameField = (EditText)findViewById(R.id.editText);
        passwordField = (EditText)findViewById(R.id.editText2);
        emailField = (EditText)findViewById(R.id.editText3);

        // image view
        image1 = (ImageView)findViewById(R.id.imageView);

    }

    // make button event listener
    @Override
    public void onClick(View v) {


        switch(v.getId()){

            case R.id.button1:
                Toast.makeText(this,"this button clicked!!",Toast.LENGTH_LONG).show();
                textView1.setText("Button clicked !");
                textView1.setText("Name : "+nameField.getText().toString()+"\n");
                textView1.append("password : "+passwordField.getText().toString()+"\n");
                textView1.append("Email : "+emailField.getText().toString()+"\n");
                break;

            case R.id.imageButton1:
                Toast.makeText(this,"star button clicked!",Toast.LENGTH_LONG).show();
                textView1.setText("imageButton Clicked !");

                if(first){
                    image1.setImageResource(R.drawable.image1);
                    first = false;
                }
                else {
                    image1.setImageResource(R.drawable.image2);
                    first = true;
                }
                break;


        }

    }
}

