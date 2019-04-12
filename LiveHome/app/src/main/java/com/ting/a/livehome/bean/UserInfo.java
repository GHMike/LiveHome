package com.ting.a.livehome.bean;

/**
 * 用户个人信息
 */
public class UserInfo {
    private int ID;
    private String userId;//后台用户ID
    private String userName;//用户昵称
    private String password;//用户密码
    private int userType;//用户类型
    private int userLeve;//用户等级
    private String phone;//用户电话
    private String userEmail;//用户邮箱
    private String userHand;//用户头像
    private String userAdds;//用户收貨地址
    private String CreatDate;//用户创建时间

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public int getUserLeve() {
        return userLeve;
    }

    public void setUserLeve(int userLeve) {
        this.userLeve = userLeve;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserHand() {
        return userHand;
    }

    public void setUserHand(String userHand) {
        this.userHand = userHand;
    }

    public String getUserAdds() {
        return userAdds;
    }

    public void setUserAdds(String userAdds) {
        this.userAdds = userAdds;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCreatDate() {
        return CreatDate;
    }

    public void setCreatDate(String creatDate) {
        CreatDate = creatDate;
    }
}
