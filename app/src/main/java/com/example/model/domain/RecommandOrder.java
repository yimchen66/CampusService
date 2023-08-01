package com.example.model.domain;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @TableName recommand_order
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecommandOrder extends DataSupport implements Serializable {
    /**
     * 订单id
     */
    private String id;

    /**
     * 订单标题
     */
    private String orderTitle;

    /**
     * 订单图片
     */
    private String orderPicture;

    /**
     * 订单热度
     */
    private Integer orderHot;

    /**
     * 订单标签
     */
    private String orderLabel;

    /**
     * 卖家昵称
     */
    private String hostName;

    /**
     * 卖家头像
     */
    private String hostFigureUrl;
    private double money;

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
        RecommandOrder other = (RecommandOrder) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getOrderTitle() == null ? other.getOrderTitle() == null : this.getOrderTitle().equals(other.getOrderTitle()))
            && (this.getOrderPicture() == null ? other.getOrderPicture() == null : this.getOrderPicture().equals(other.getOrderPicture()))
            && (this.getOrderHot() == null ? other.getOrderHot() == null : this.getOrderHot().equals(other.getOrderHot()))
            && (this.getOrderLabel() == null ? other.getOrderLabel() == null : this.getOrderLabel().equals(other.getOrderLabel()))
            && (this.getHostName() == null ? other.getHostName() == null : this.getHostName().equals(other.getHostName()))
            && (this.getHostFigureUrl() == null ? other.getHostFigureUrl() == null : this.getHostFigureUrl().equals(other.getHostFigureUrl()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getOrderTitle() == null) ? 0 : getOrderTitle().hashCode());
        result = prime * result + ((getOrderPicture() == null) ? 0 : getOrderPicture().hashCode());
        result = prime * result + ((getOrderHot() == null) ? 0 : getOrderHot().hashCode());
        result = prime * result + ((getOrderLabel() == null) ? 0 : getOrderLabel().hashCode());
        result = prime * result + ((getHostName() == null) ? 0 : getHostName().hashCode());
        result = prime * result + ((getHostFigureUrl() == null) ? 0 : getHostFigureUrl().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", orderTitle=").append(orderTitle);
        sb.append(", orderPicture=").append(orderPicture);
        sb.append(", orderHot=").append(orderHot);
        sb.append(", orderLabel=").append(orderLabel);
        sb.append(", hostName=").append(hostName);
        sb.append(", hostFigureUrl=").append(hostFigureUrl);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}