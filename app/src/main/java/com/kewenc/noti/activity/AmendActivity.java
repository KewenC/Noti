/*
 * Copyright (c) 2016-2017.
 * KewenC 版权所有
 */

package com.kewenc.noti.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.kewenc.noti.R;
import com.kewenc.noti.dao.MeDAO;

public class AmendActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView tv_title;
    private EditText acet_word,acet_marken,acet_markus,acet_translate;
    private Button btn_sure_create,btn_cancel_create;
    private int mposition;
    private String mword;
    private String mmarken;
    private String mmarkus;
    private String mtranslate;
    private int  ID;
    private ImageView ib;
    private String[] temp=new String[3];
    private WebView webView;
    private ProgressDialog progressDialog = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        Toolbar toolbar=(Toolbar)findViewById(R.id.tb_create);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        setSupportActionBar(toolbar);
        tv_title=(TextView)findViewById(R.id.tv_title);
        ib=(ImageView)findViewById(R.id.ib);
        ib.setOnClickListener(this);
        acet_word=(EditText)findViewById(R.id.acet_word);
        acet_marken=(EditText)findViewById(R.id.acet_marken);
        acet_markus=(EditText)findViewById(R.id.acet_markus);
        acet_translate=(EditText)findViewById(R.id.acet_translate);
        btn_sure_create=(Button)findViewById(R.id.btn_sure_create);
        btn_cancel_create=(Button)findViewById(R.id.btn_cancel_create);
        tv_title.setText("修改单词");//标题
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        mposition=bundle.getInt("POSITION");
        mword=bundle.getString("WORD");
        ID=bundle.getInt("ID");
        MeDAO meDAO=new MeDAO(AmendActivity.this);
        mmarken=meDAO.find(ID).getMarken();
        mmarkus=meDAO.find(ID).getMarkus();
        mtranslate=meDAO.find(ID).getTranslate();
        acet_word.setText(mword);
        acet_marken.setText(mmarken);
        acet_markus.setText(mmarkus);
        acet_translate.setText(mtranslate);
        btn_sure_create.setOnClickListener(new BtnSureOnClickListener());
        btn_cancel_create.setOnClickListener(new BtnCancelOnClickListener());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib:
                String word=acet_word.getText().toString();
                if (!word.equals("")){
                    LoadData(word,v);
                }
                break;
        }
    }

    /**
     * 确定按钮监听器
     */
    private class BtnSureOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String word=acet_word.getText().toString();
            String marken=acet_marken.getText().toString();
            String markus=acet_markus.getText().toString();
            String translate=acet_translate.getText().toString();
            if ((word.equals("")&&marken.equals("")&&markus.equals("")&&translate.equals(""))||(word.equals(mword)&&marken.equals(mmarken)&&
                    markus.equals(mmarkus)&&translate.equals(mtranslate))){
                finish();
            }else{
                //数据是使用Intent返回
                Intent intent = new Intent();
                //把返回数据存入Intent
                intent.putExtra("POSITION", mposition);
                intent.putExtra("ID",ID);
                intent.putExtra("WORD", word);
                intent.putExtra("MARKEN", marken);
                intent.putExtra("MARKUS", markus);
                intent.putExtra("TRANSLATE", translate);
                //设置返回数据
                AmendActivity.this.setResult(RESULT_OK, intent);
                //关闭Activity
                AmendActivity.this.finish();
            }
        }
    }

    /**
     * 取消按钮监听器
     */
    private class BtnCancelOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            finish();
        }
    }
    private void LoadData(String word,View v) {
        webView= new WebView(AmendActivity.this);
        String uri="http://dict.youdao.com/search?q="+word+"&keyfrom=fanyi.smartResult";
        try {
            webView.loadUrl(uri);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.addJavascriptInterface(new AmendActivity.Handler(),"handler");
            webView.setWebViewClient(new WebViewClient(){
                @Override
                public void onPageFinished(WebView view, String url) {
                    view.loadUrl("javascript:window.handler.show(document.body.innerHTML);");
                    super.onPageFinished(view, url);
                }
            });
            progressDialog= ProgressDialog.show(AmendActivity.this, "加载数据中...", "", true,true);
        }catch (Exception e){
            e.printStackTrace();
            Snackbar.make(v,"加载失败，请重试！",Snackbar.LENGTH_SHORT).show();
        }
    }
    class Handler {
        @JavascriptInterface
        public void show(String data) {
            int index=0;
            int start=0;
            int end=0;
            int isstart=0;
            boolean is_JieQuS=false;
            //无论这个词是不是英语单词或者拼写错误，查询的结果都在“结果内容”到“内容区域”之中
            //故先从原始数据截取出这部分内容即结果内容”到“内容区域”
            //通过判定，存在phrsListTab则证明是单词所以继续截取，不存在则证明非单词所以立即结束查询
            while(true){
                if ((data.charAt(index)+"").equals("结")){
                    if (data.substring(index+1,index+4).equals("果内容")){
                        start=index+4;
                        index=start;
                        break;
                    }
                }
                index++;
            }
            while(true){
                if ((data.charAt(index)+"").equals("内")){
                    if (data.substring(index+1,index+4).equals("容区域")){
                        end=index;
                        break;
                    }
                }
                index++;
            }
            while(true){
                if ((data.charAt(start)+"").equals("p")){
                    if (data.substring(start+1,start+11).equals("hrsListTab")){
                        isstart=start+11;
                        is_JieQuS=true;
                        break;
                    }
                }
                if (start>end){
                    break;
                }
                start++;
            }
            if (is_JieQuS){
                JieQuS(data.substring(isstart,end));
            }//查询无结果，检查单词拼写是否正确？
            else{
                Snackbar.make(findViewById(R.id.activity_create),"查询无果！",Snackbar.LENGTH_LONG).show();
            }


            acet_marken.post(new Runnable() {
                @Override
                public void run() {
                    acet_marken.setText(temp[0]);

                }
            });
            acet_markus.post(new Runnable() {
                @Override
                public void run() {
                    acet_markus.setText(temp[1]);

                }
            });
            acet_translate.post(new Runnable() {
                @Override
                public void run() {
                    acet_translate.setText(temp[2]);
                    temp[0]="";
                    temp[1]="";
                    temp[2]="";
                }
            });
            progressDialog.dismiss();

//            android.app.AlertDialog.Builder builder =
//                    new android.app.AlertDialog.Builder(CreateActivity.this);
//            builder.setTitle("oo") .setPositiveButton("确定",
//                            new DialogInterface.OnClickListener() {
//
//                                @Override
//                                public void onClick(DialogInterface arg0,
//                                                    int arg1) {
//                                    Fresh();
//                                }
//                            })
//                    .create().show();
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    acet_marken.setText(temp[0]);
//            acet_markus.setText(temp[1]);
//            acet_translate.setText(temp[2]);
//                }
//            }).start();
//            acet_marken.setText(temp[0]);
//            acet_markus.setText(temp[1]);
//            acet_translate.setText(temp[2]);
        }
    }

    /**
     * 内容解析
     * @param data
     */
    private void JieQuS(String data){
        int i=0;
        int tmp_start=0;
        String tmp="";
        int flag_baav=0;
        int flag_trans=0;
        int flag_phonetic0=0;
        int flag_phonetic1=0;
        while (true){//标志baav和trans-container
            if ((data.charAt(i)+"").equals("b")){
                if (data.substring(i,i+4).equals("baav")){
                    flag_baav=i+4;
                }
            }
            if ((data.charAt(i)+"").equals("t")){
                if (data.substring(i+1,i+9).equals("rans-con")){
                    flag_trans=i;
                    break;
                }
            }
            if (i+6>data.length()-2){
                break;
            }
            i++;
        }
        i=flag_baav;//将索引号定位到标志baav那
        boolean one=true;
        while (true){//标志phonetic,数量0或1或2
            if (one){
                if ((data.charAt(i)+"").equals("p")){
                    if (data.substring(i+1,i+9).equals("honetic\"")){
                        flag_phonetic0=i+10;
                        i=i+11;
                        one=false;
                    }
                }
            }
            if ((data.charAt(i)+"").equals("p")){
                if (data.substring(i+1,i+9).equals("honetic\"")){
                    flag_phonetic1=i+10;
                    break;
                }
            }
            if (i+8>flag_trans){
                break;
            }
            i++;
        }
        if (flag_phonetic0!=0){
            int k0=flag_phonetic0;
            while (true){
                if ((data.charAt(k0)+"").equals("]")){
                    temp[0]=data.substring(flag_phonetic0,k0+1);
                    break;
                }
                k0++;
            }
            if (flag_phonetic1!=0){
                int k1=flag_phonetic1;
                while (true){
                    if ((data.charAt(k1)+"").equals("]")){
                        temp[1]=data.substring(flag_phonetic1,k1+1);
                        break;
                    }
                    k1++;
                }
            }else{
                temp[1]=temp[0];
            }
        }else{
            temp[0]="";
            temp[1]="";
        }
        i=flag_trans;//以防出错
        while (true){
            if ((data.charAt(i)+"").equals("<")){
                if ((data.substring(i+1,i+3)).equals("li")){
                    tmp_start=i+4;
                }
            }
            if ((data.charAt(i)+"").equals("<")){
                if ((data.substring(i+1,i+4)).equals("/li")){
                    if (tmp.equals("")){
                        tmp=data.substring(tmp_start,i);
                    }else{
                        tmp=tmp+"\n"+data.substring(tmp_start,i);
                    }

                }
            }
            i++;
            if ((data.charAt(i)+"").equals("<")){
                if ((data.substring(i+1,i+4)).equals("/ul")){
                    break;
                }
            }
        }
        temp[2]=tmp;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if (id==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}
