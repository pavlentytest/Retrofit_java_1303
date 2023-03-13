package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myapplication.api.YandexAPI;
import com.example.myapplication.model.Answer;

import java.io.StringBufferInputStream;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    public static final String BASE_URL = "https://predictor.yandex.net";
    public static final String KEY = "pdct.1.1.20220412T145449Z.ed53b660ddacdc8e.353ee4c0c5adc174b6be636450d97faa6e34a072";
    public static final String LANG = "en";
    public static final Integer LIMIT = 5;
    EditText editText;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.editTextTextPersonName);
        textView = findViewById(R.id.result);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().length() > 2) {
                    doRequest();
                }
            }
        });
    }

    public void doRequest() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        YandexAPI api = retrofit.create(YandexAPI.class);
        api.getComplete(KEY, editText.getText().toString(), LANG, LIMIT).enqueue(new Callback<Answer>() {
            @Override
            public void onResponse(Call<Answer> call, Response<Answer> response) {
                if(response.code() == 200) {
                    List<String> result = response.body().getText();
                    StringBuilder stringBuilder = new StringBuilder();
                    for(String s: result) {
                        stringBuilder.append(s + "\n");
                    }
                    textView.setText(stringBuilder.toString());
                }
            }

            @Override
            public void onFailure(Call<Answer> call, Throwable t) {
                Log.d("RRR",t.toString());
            }
        });

    }
}