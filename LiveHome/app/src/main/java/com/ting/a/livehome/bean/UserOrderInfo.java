package com.ting.a.livehome.bean;

/**
 * 用户订单
 */

public class UserOrderInfo {
    private int ID;
    private String UserId;//用户ID
    private String OrderNo;//订单编号
    private String OrderSize;//订单规格
    private String OrderType;//订单类型
    private String OrderPrice;//订单单价
    private String OrderNum;//订单数量
    private String OrderTotalPrice;//订单总金额
    private String OrderSupplier;//订单供应商
    private String OrderState;//订单状态
    private String OrderCreatDate;//订单创建时间
    private String OrderDealDate;//订单交易时间
    private String OrderSenDate;//订单发货时间
    private String OrderConsignee;//订单签收人
    private String OrderConsigneeDate;//订单签收时间
    private String OrderConsigneeAdd;//订单签收地址
    private String OrderModifyDate;//订单更新时间


    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String orderNo) {
        OrderNo = orderNo;
    }

    public String getOrderSize() {
        return OrderSize;
    }

    public void setOrderSize(String orderSize) {
        OrderSize = orderSize;
    }

    public String getOrderType() {
        return OrderType;
    }

    public void setOrderType(String orderType) {
        OrderType = orderType;
    }

    public String getOrderPrice() {
        return OrderPrice;
    }

    public void setOrderPrice(String orderPrice) {
        OrderPrice = orderPrice;
    }

    public String getOrderNum() {
        return OrderNum;
    }

    public void setOrderNum(String orderNum) {
        OrderNum = orderNum;
    }

    public String getOrderTotalPrice() {
        return OrderTotalPrice;
    }

    public void setOrderTotalPrice(String orderTotalPrice) {
        OrderTotalPrice = orderTotalPrice;
    }

    public String getOrderSupplier() {
        return OrderSupplier;
    }

    public void setOrderSupplier(String orderSupplier) {
        OrderSupplier = orderSupplier;
    }

    public String getOrderState() {
        return OrderState;
    }

    public void setOrderState(String orderState) {
        OrderState = orderState;
    }

    public String getOrderCreatDate() {
        return OrderCreatDate;
    }

    public void setOrderCreatDate(String orderCreatDate) {
        OrderCreatDate = orderCreatDate;
    }

    public String getOrderDealDate() {
        return OrderDealDate;
    }

    public void setOrderDealDate(String orderDealDate) {
        OrderDealDate = orderDealDate;
    }

    public String getOrderSenDate() {
        return OrderSenDate;
    }

    public void setOrderSenDate(String orderSenDate) {
        OrderSenDate = orderSenDate;
    }

    public String getOrderConsignee() {
        return OrderConsignee;
    }

    public void setOrderConsignee(String orderConsignee) {
        OrderConsignee = orderConsignee;
    }

    public String getOrderConsigneeDate() {
        return OrderConsigneeDate;
    }

    public void setOrderConsigneeDate(String orderConsigneeDate) {
        OrderConsigneeDate = orderConsigneeDate;
    }

    public String getOrderConsigneeAdd() {
        return OrderConsigneeAdd;
    }

    public void setOrderConsigneeAdd(String orderConsigneeAdd) {
        OrderConsigneeAdd = orderConsigneeAdd;
    }

    public String getOrderModifyDate() {
        return OrderModifyDate;
    }

    public void setOrderModifyDate(String orderModifyDate) {
        OrderModifyDate = orderModifyDate;
    }
}
