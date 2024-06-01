package com.mindskip.xzs.domain;

import java.io.Serializable;
import java.util.Date;

public class TextBook implements Serializable {
    private static final long serialVersionUID = -6334283535554611819L;

    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 科目ID
     */
    private Integer subjectId;

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 创建人
     */
    private Integer createUser;


    /**
     * 创建时间
     */
    private Date createTime;

    public TextBook() {
    }

    public TextBook(String name, Integer subjectId, String filePath, Integer createUser, Date createTime) {
        this.name = name;
        this.subjectId = subjectId;
        this.filePath = filePath;
        this.createUser = createUser;
        this.createTime = createTime;
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

    public Integer getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Integer subjectId) {
        this.subjectId = subjectId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Integer getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Integer createUser) {
        this.createUser = createUser;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
