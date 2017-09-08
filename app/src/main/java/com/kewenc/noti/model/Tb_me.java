package com.kewenc.noti.model;

/**
 * Created by KewenC on 2016/12/29.
 */

public class Tb_me {
    private int id;//存储编号
    private String word;
    private String marken;
    private String markus;
    private String translate;
    public Tb_me()//默认构造函数
    {
        super();
    }
    public Tb_me(int id,String word,String marken,String markus,String translate)//定义有参构造函数，用来初始化信息实体类的各个字段
    {
        super();
        this.id=id;//为编号赋值
        this.word=word;//为备忘录赋值
        this.marken=marken;
        this.markus=markus;
        this.translate=translate;//为月日赋值
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
}


