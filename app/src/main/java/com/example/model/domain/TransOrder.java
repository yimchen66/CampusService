package com.example.model.domain;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @TableName trans_order
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransOrder extends DataSupport implements Serializable {
    /**
     * 订单id
     */
    private String id;

    /**
     * 发布订单的用户id
     */
    private String hostId;

    /**
     * 接受订单的用户id
     */
    private String acceptId;

    /**
     * 接受时间
     */
    private String acceptTime;

    /**
     * 卖家状态
     */
    private Integer hostState;

    /**
     * 买家状态
     */
    private Integer acceptState;

    /**
     * 卖家确认时间
     */
    private String hostFinishTime;

    /**
     * 买家确认时间
     */
    private String acceptFinishTime;

    /**
     * 订单金额
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
        TransOrder other = (TransOrder) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getHostId() == null ? other.getHostId() == null : this.getHostId().equals(other.getHostId()))
            && (this.getAcceptId() == null ? other.getAcceptId() == null : this.getAcceptId().equals(other.getAcceptId()))
            && (this.getAcceptTime() == null ? other.getAcceptTime() == null : this.getAcceptTime().equals(other.getAcceptTime()))
            && (this.getHostState() == null ? other.getHostState() == null : this.getHostState().equals(other.getHostState()))
            && (this.getAcceptState() == null ? other.getAcceptState() == null : this.getAcceptState().equals(other.getAcceptState()))
            && (this.getHostFinishTime() == null ? other.getHostFinishTime() == null : this.getHostFinishTime().equals(other.getHostFinishTime()))
            && (this.getAcceptFinishTime() == null ? other.getAcceptFinishTime() == null : this.getAcceptFinishTime().equals(other.getAcceptFinishTime()))
            && (this.getOrderMoney() == null ? other.getOrderMoney() == null : this.getOrderMoney().equals(other.getOrderMoney()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getHostId() == null) ? 0 : getHostId().hashCode());
        result = prime * result + ((getAcceptId() == null) ? 0 : getAcceptId().hashCode());
        result = prime * result + ((getAcceptTime() == null) ? 0 : getAcceptTime().hashCode());
        result = prime * result + ((getHostState() == null) ? 0 : getHostState().hashCode());
        result = prime * result + ((getAcceptState() == null) ? 0 : getAcceptState().hashCode());
        result = prime * result + ((getHostFinishTime() == null) ? 0 : getHostFinishTime().hashCode());
        result = prime * result + ((getAcceptFinishTime() == null) ? 0 : getAcceptFinishTime().hashCode());
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
        sb.append(", hostId=").append(hostId);
        sb.append(", acceptId=").append(acceptId);
        sb.append(", acceptTime=").append(acceptTime);
        sb.append(", hostState=").append(hostState);
        sb.append(", acceptState=").append(acceptState);
        sb.append(", hostFinishTime=").append(hostFinishTime);
        sb.append(", acceptFinishTime=").append(acceptFinishTime);
        sb.append(", orderMoney=").append(orderMoney);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}