package com.example.ikeguess.Activity;

import android.os.Bundle;
import android.os.StrictMode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ikeguess.QuestionAdapter;
import com.example.ikeguess.customClass.QuizzCreator;
import com.example.ikeguess.R;
import com.example.ikeguess.customClass.DataList;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class listQuestionActivity extends AppCompatActivity {

    public static final String URL = "https://students.gryt.tech/api/L2/ikea/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_question);
        DataList dataList;

        try {
            dataList = getData();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        QuizzCreator creator = new QuizzCreator();
        QuizzCreator creatorM = new QuizzCreator();
        creator.createMultipleSimpleQuizz(dataList, dataList.furnitureSimple.size(), "simple");
        creatorM.createMultipleSimpleQuizz(dataList, dataList.furnitureMedium.size(), "medium");
        creator.listQuizz.addAll(creatorM.listQuizz);
        RecyclerView recyclerView = findViewById(R.id.listRecyclerView);
        QuestionAdapter adapter =  new QuestionAdapter(creator.listQuizz);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private DataList getData() throws IOException {
        OkHttpClient client = new OkHttpClient();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Request request = new Request.Builder()
                .url(URL)
                .build();

        try (Response response = client.newCall(request).execute()) {
            Gson gson = new Gson();

            DataList furnitureData = gson.fromJson(response.body().string(), DataList.class);
            return furnitureData;
        }
    }
}