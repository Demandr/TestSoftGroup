package com.example.oleksandr.checkpalindrome;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText mEditText;
    Button mButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEditText = (EditText) findViewById(R.id.edit_text);
        mButton = (Button) findViewById(R.id.check_button);

        /*mEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    mEditText.setText("");
                }else {
                    mEditText.setText(R.string.edit_text);
                }
            }
        });*/

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = mEditText.getText().toString();
                if (text.length() == 0) {
                    showToast("Enter Text!");
                } else {
                    if (isPalindrome(text)) {
                        showToast("Is palindrome");
                    } else {
                        showToast("Is not palindrome");
                    }
                }
            }
        });
    }

    private void showToast(String text){
        Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    private static boolean isPalindrome(String text) {
        text= text.toLowerCase();
        int textLength = text.length();
        for(int i = 0; i < textLength / 2; i++)
            if (text.charAt(i) != text.charAt(textLength - i - 1))
                return false;
        return true;
    }

}
