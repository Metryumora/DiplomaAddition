package com.chdtu.fitis.dipladd.ajax;

/**
 * Created by Metr_yumora on 02.07.2017.
 */
public class KeyItem {

    private Integer id;

    private String name;

    public KeyItem() {
    }

    public KeyItem(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
