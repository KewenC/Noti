package com.kewenc.noti.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.kewenc.noti.R;
import com.kewenc.noti.dao.NotiDao;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText etWord;
    private ImageView imgSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        etWord = findViewById(R.id.etWord);
        imgSearch = findViewById(R.id.imgSearch);
        imgSearch.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgSearch:
                String word = etWord.getText().toString();
                if (word.equals("")) return;
                NotiDao notiDao = new NotiDao(this);
                notiDao.queryByWordOrTranlate(word,0);
                notiDao.queryByWordOrTranlate(word,1);
                notiDao.queryByWordOrTranlate(word,2);
                notiDao.queryByWordOrTranlate(word,3);
                notiDao.queryByWordOrTranlate(word,4);
                break;
        }
    }
}
