package com.example.model.domain;


import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 * @TableName user
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class User extends DataSupport implements Serializable {
    /**
     * 唯一标识
     */
    private String id;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 头像
     */
    private String figureUrl;

    /**
     * 余额
     */
    private Double money;

    /**
     * 总订单数
     */
    private Integer sumOrder;

    /**
     * 信誉
     */
    private Double credit;

    /**
     * 账号
     */
    private String userName;

    /**
     * 密码
     */
    private String password;

    /**
     * 性别
     */
    private String sex;

    /**
     * 最后登录时间
     */
    private String lastLogin;

    /**
     * 地址
     */
    private String location;

    /**
     * 标签
     */
    private String label;

    private static final long serialVersionUID = 1L;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getFigureUrl() {
        return figureUrl;
    }

    public void setFigureUrl(String figureUrl) {
        this.figureUrl = figureUrl;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public Integer getSumOrder() {
        return sumOrder;
    }

    public void setSumOrder(Integer sumOrder) {
        this.sumOrder = sumOrder;
    }

    public Double getCredit() {
        return credit;
    }

    public void setCredit(Double credit) {
        this.credit = credit;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", nickName=").append(nickName);
        sb.append(", figureUrl=").append(figureUrl);
        sb.append(", money=").append(money);
        sb.append(", sumOrder=").append(sumOrder);
        sb.append(", credit=").append(credit);
        sb.append(", userName=").append(userName);
        sb.append(", password=").append(password);
        sb.append(", sex=").append(sex);
        sb.append(", lastLogin=").append(lastLogin);
        sb.append(", location=").append(location);
        sb.append(", label=").append(label);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}