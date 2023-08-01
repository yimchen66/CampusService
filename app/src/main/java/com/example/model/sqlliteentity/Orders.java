package com.example.model.sqlliteentity;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @TableName orders
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Orders  extends DataSupport implements Serializable {
    /**
     * 
     */
    private String id;

    /**
     * 
     */
    private String hostId;

    /**
     * 
     */
    private String orderTitle;

    /**
     * 
     */
    private String orderContent;

    /**
     * 
     */
    private String orderPictures;

    /**
     * 
     */
    private String orderAddress;

    /**
     * 
     */
    private Double orderMoney;

    /**
     * 
     */
    private String orderDeadline;

    /**
     * 
     */
    private Integer orderIsDelete;

    /**
     * 
     */
    private String orderCreateTime;

    /**
     * 
     */
    private Integer orderState;

    /**
     * 
     */
    private String orderLabel;

    /**
     * 
     */
    private String acceptId;

    /**
     * 
     */
    private Long orderHot;

    /**
     * 是否是推荐
     */
    private Integer orderIsRecommend;

    /**
     * 版本号
     */
    private Integer version;

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
        Orders other = (Orders) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getHostId() == null ? other.getHostId() == null : this.getHostId().equals(other.getHostId()))
            && (this.getOrderTitle() == null ? other.getOrderTitle() == null : this.getOrderTitle().equals(other.getOrderTitle()))
            && (this.getOrderContent() == null ? other.getOrderContent() == null : this.getOrderContent().equals(other.getOrderContent()))
            && (this.getOrderPictures() == null ? other.getOrderPictures() == null : this.getOrderPictures().equals(other.getOrderPictures()))
            && (this.getOrderAddress() == null ? other.getOrderAddress() == null : this.getOrderAddress().equals(other.getOrderAddress()))
            && (this.getOrderMoney() == null ? other.getOrderMoney() == null : this.getOrderMoney().equals(other.getOrderMoney()))
            && (this.getOrderDeadline() == null ? other.getOrderDeadline() == null : this.getOrderDeadline().equals(other.getOrderDeadline()))
            && (this.getOrderIsDelete() == null ? other.getOrderIsDelete() == null : this.getOrderIsDelete().equals(other.getOrderIsDelete()))
            && (this.getOrderCreateTime() == null ? other.getOrderCreateTime() == null : this.getOrderCreateTime().equals(other.getOrderCreateTime()))
            && (this.getOrderState() == null ? other.getOrderState() == null : this.getOrderState().equals(other.getOrderState()))
            && (this.getOrderLabel() == null ? other.getOrderLabel() == null : this.getOrderLabel().equals(other.getOrderLabel()))
            && (this.getAcceptId() == null ? other.getAcceptId() == null : this.getAcceptId().equals(other.getAcceptId()))
            && (this.getOrderHot() == null ? other.getOrderHot() == null : this.getOrderHot().equals(other.getOrderHot()))
            && (this.getOrderIsRecommend() == null ? other.getOrderIsRecommend() == null : this.getOrderIsRecommend().equals(other.getOrderIsRecommend()))
            && (this.getVersion() == null ? other.getVersion() == null : this.getVersion().equals(other.getVersion()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getHostId() == null) ? 0 : getHostId().hashCode());
        result = prime * result + ((getOrderTitle() == null) ? 0 : getOrderTitle().hashCode());
        result = prime * result + ((getOrderContent() == null) ? 0 : getOrderContent().hashCode());
        result = prime * result + ((getOrderPictures() == null) ? 0 : getOrderPictures().hashCode());
        result = prime * result + ((getOrderAddress() == null) ? 0 : getOrderAddress().hashCode());
        result = prime * result + ((getOrderMoney() == null) ? 0 : getOrderMoney().hashCode());
        result = prime * result + ((getOrderDeadline() == null) ? 0 : getOrderDeadline().hashCode());
        result = prime * result + ((getOrderIsDelete() == null) ? 0 : getOrderIsDelete().hashCode());
        result = prime * result + ((getOrderCreateTime() == null) ? 0 : getOrderCreateTime().hashCode());
        result = prime * result + ((getOrderState() == null) ? 0 : getOrderState().hashCode());
        result = prime * result + ((getOrderLabel() == null) ? 0 : getOrderLabel().hashCode());
        result = prime * result + ((getAcceptId() == null) ? 0 : getAcceptId().hashCode());
        result = prime * result + ((getOrderHot() == null) ? 0 : getOrderHot().hashCode());
        result = prime * result + ((getOrderIsRecommend() == null) ? 0 : getOrderIsRecommend().hashCode());
        result = prime * result + ((getVersion() == null) ? 0 : getVersion().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", hostId=").append(hostId);
        sb.append(", orderTitle=").append(orderTitle);
        sb.append(", orderContent=").append(orderContent);
        sb.append(", orderPictures=").append(orderPictures);
        sb.append(", orderAddress=").append(orderAddress);
        sb.append(", orderMoney=").append(orderMoney);
        sb.append(", orderDeadline=").append(orderDeadline);
        sb.append(", orderIsDelete=").append(orderIsDelete);
        sb.append(", orderCreateTime=").append(orderCreateTime);
        sb.append(", orderState=").append(orderState);
        sb.append(", orderLabel=").append(orderLabel);
        sb.append(", acceptId=").append(acceptId);
        sb.append(", orderHot=").append(orderHot);
        sb.append(", orderIsRecommend=").append(orderIsRecommend);
        sb.append(", version=").append(version);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}