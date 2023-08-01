package com.example.model.domain;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @TableName history_track
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoryTrack extends DataSupport implements Serializable {
    /**
     * id
     */
    private String id;

    /**
     * 用户id
     */
    private String openId;

    /**
     * 订单id
     */
    private String orderId;

    /**
     * 订单标题
     */
    private String orderTitle;

    /**
     * 订单图片
     */
    private String orderPicture;

    /**
     * 订单佣金
     */
    private Double orderMoney;

    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        HistoryTrack other = (HistoryTrack) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getOpenId() == null ? other.getOpenId() == null : this.getOpenId().equals(other.getOpenId()))
            && (this.getOrderId() == null ? other.getOrderId() == null : this.getOrderId().equals(other.getOrderId()))
            && (this.getOrderTitle() == null ? other.getOrderTitle() == null : this.getOrderTitle().equals(other.getOrderTitle()))
            && (this.getOrderPicture() == null ? other.getOrderPicture() == null : this.getOrderPicture().equals(other.getOrderPicture()))
            && (this.getOrderMoney() == null ? other.getOrderMoney() == null : this.getOrderMoney().equals(other.getOrderMoney()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getOpenId() == null) ? 0 : getOpenId().hashCode());
        result = prime * result + ((getOrderId() == null) ? 0 : getOrderId().hashCode());
        result = prime * result + ((getOrderTitle() == null) ? 0 : getOrderTitle().hashCode());
        result = prime * result + ((getOrderPicture() == null) ? 0 : getOrderPicture().hashCode());
        result = prime * result + ((getOrderMoney() == null) ? 0 : getOrderMoney().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", openId=").append(openId);
        sb.append(", orderId=").append(orderId);
        sb.append(", orderTitle=").append(orderTitle);
        sb.append(", orderPicture=").append(orderPicture);
        sb.append(", orderMoney=").append(orderMoney);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}