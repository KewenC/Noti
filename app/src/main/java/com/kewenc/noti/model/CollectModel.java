package com.kewenc.noti.model;

public class CollectModel {
    private int id;//存储编号
    private String word;
    private String marken;
    private String markus;
    private String translate;
    private String markenpath;
    private String markuspath;
    private int flag;

    public CollectModel()//默认构造函数
    {
        super();
    }

    public CollectModel(int id,String word,String marken,String markus,String translate, String markenpath, String markuspath, int flag)//定义有参构造函数，用来初始化信息实体类的各个字段
    {
        super();
        this.id=id;//为编号赋值
        this.word=word;//为备忘录赋值
        this.marken=marken;
        this.markus=markus;
        this.translate=translate;//为月日赋值
        this.markenpath = markenpath;
        this.markuspath = markuspath;
        this.flag = flag;
    }
    public int getId()//设置编号的可读属性
    {
        return id;
    }
    public void setId(int id)//设置编号的可写属性
    {
        this.id=id;
    }
    public String getWord()
    {
        return word;
    }
    public void setWord (String word)
    {
        this.word=word;
    }
    public String getMarken()
    {
        return marken;
    }
    public void setMarken (String marken)
    {
        this.marken=marken;
    }
    public String getMarkus()
    {
        return markus;
    }
    public void setMarkus (String markus)
    {
        this.markus=markus;
    }
    public String getTranslate()
    {
        return translate;
    }
    public void setTranslate (String translate)
    {
        this.translate=translate;
    }

    public String getMarkenpath(){
        return markenpath;
    }
    public void setMarkenpath(String markenpath){
        this.markenpath = markenpath;
    }
    public String getMarkuspath(){
        return markuspath;
    }
    public void setMarkuspath(String markuspath){
        this.markuspath = markuspath;
    }
    public int getFlag(){
        return flag;
    }
    public void setFlag(int flag){
        this.flag = flag;
    }
}
