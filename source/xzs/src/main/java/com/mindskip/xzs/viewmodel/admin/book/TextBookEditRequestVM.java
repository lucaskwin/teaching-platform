package com.mindskip.xzs.viewmodel.admin.book;

import com.mindskip.xzs.viewmodel.BaseVM;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;


public class TextBookEditRequestVM extends BaseVM {

    private Integer id;

    @NotBlank
    private String name;

    @NotNull
    private Integer subjectId;

    private List<TextBookEditItemVM> filePathItems;

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

    public Integer getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Integer subjectId) {
        this.subjectId = subjectId;
    }

    public List<TextBookEditItemVM> getFilePathItems() {
        return filePathItems;
    }

    public void setFilePathItems(List<TextBookEditItemVM> filePathItems) {
        this.filePathItems = filePathItems;
    }
}
