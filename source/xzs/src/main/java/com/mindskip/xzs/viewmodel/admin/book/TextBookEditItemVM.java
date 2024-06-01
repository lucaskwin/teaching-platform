package com.mindskip.xzs.viewmodel.admin.book;

import com.mindskip.xzs.viewmodel.BaseVM;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


public class TextBookEditItemVM extends BaseVM {

    private String name;

    private String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
