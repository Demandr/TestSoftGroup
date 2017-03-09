package com.example.oleksandr.k_complementary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.StringBuilderPrinter;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private EditText mEditTextK;
    private EditText mEditTextSize;
    private Button mButtonSearch;
    private TextView mTextViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEditTextK = (EditText) findViewById(R.id.editTextK);
        mEditTextSize = (EditText) findViewById(R.id.editTextSize);
        mButtonSearch = (Button) findViewById(R.id.search_button);
        mTextViewResult = (TextView) findViewById(R.id.textViewResult);

        mButtonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkDataK() || checkDataSize()){
                    Toast toast = Toast.makeText(getApplicationContext(), "    Error \nEnter data", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    int k = Integer.valueOf(mEditTextK.getText().toString());
                    int size = Integer.valueOf(mEditTextSize.getText().toString());
                    int[] mArr = new int[size];
                    int result = 0;
                    setArray(mArr, size);
                    StringBuilder sb = new StringBuilder("K complementary pairs:\n");
                    for (int i = 0; i < mArr.length; i++) {
                        for (int j = 1; j < mArr.length - 1; j++)
                            if (mArr[i] + mArr[j] == k) {
                                sb.append("{" + mArr[i] + "," + mArr[j] + "}" + ", ");
                                result++;
                            }
                    }
                    if (result == 0){
                        sb.append("do not have");
                    }
                    sb.append("\nArray:\n");
                    for (int i = 0; i < size; i++) {
                        sb.append(mArr[i]).append("  ");
                    }
                    mTextViewResult.setMovementMethod(new ScrollingMovementMethod());
                    mTextViewResult.setText(sb.toString());
                }
            }
        });

    }

    private boolean checkDataK(){
        if ( mEditTextK.getText().toString().equals("")) {
            mEditTextK.setError("Enter data");
            return true;
        }
            return false;
    }

    private boolean checkDataSize(){
        if (mEditTextSize.getText().toString().equals("")){
            mEditTextSize.setError("Enter data");
            return true;
        }
        return false;
    }

    private void setArray(int[] Arr, int size){
        Random random = new Random();
        for (int i = 0; i < size; i++){
            Arr[i] = random.nextInt(31) - 15;
        }
    }
}
