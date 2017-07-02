package com.chdtu.fitis.dipladd.ajax;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Metr_yumora on 02.07.2017.
 */
public class AjaxResponseBody {

    private String message;

    private List<KeyItem> items;

    public AjaxResponseBody() {
        items = new ArrayList<>();
    }

    public AjaxResponseBody(String message, List<KeyItem> items) {
        this.message = message;
        this.items = items;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<KeyItem> getItems() {
        return items;
    }

    public void setItems(List<KeyItem> items) {
        this.items = items;
    }
}
