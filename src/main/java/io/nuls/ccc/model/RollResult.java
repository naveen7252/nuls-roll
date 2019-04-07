package io.nuls.ccc.model;

import java.math.BigInteger;

public class RollResult {

    private Integer rolledNumber;
    private Integer userSelectedNumber;
    private BigInteger rewardAmount;
    private BigInteger commissionAmount;
    private String payoutAddress;
    private String remarks;

    public Integer getRolledNumber() {
        return rolledNumber;
    }

    public void setRolledNumber(Integer rolledNumber) {
        this.rolledNumber = rolledNumber;
    }

    public BigInteger getRewardAmount() {
        return rewardAmount;
    }

    public void setRewardAmount(BigInteger rewardAmount) {
        this.rewardAmount = rewardAmount;
    }

    public BigInteger getCommissionAmount() {
        return commissionAmount;
    }

    public void setCommissionAmount(BigInteger commissionAmount) {
        this.commissionAmount = commissionAmount;
    }

    public String getPayoutAddress() {
        return payoutAddress;
    }

    public void setPayoutAddress(String payoutAddress) {
        this.payoutAddress = payoutAddress;
    }

    public Integer getUserSelectedNumber() {
        return userSelectedNumber;
    }

    public void setUserSelectedNumber(Integer userSelectedNumber) {
        this.userSelectedNumber = userSelectedNumber;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RollResult that = (RollResult) o;

        if (!rolledNumber.equals(that.rolledNumber)) return false;
        if (!userSelectedNumber.equals(that.userSelectedNumber)) return false;
        if (!rewardAmount.equals(that.rewardAmount)) return false;
        if (!commissionAmount.equals(that.commissionAmount)) return false;
        return payoutAddress.equals(that.payoutAddress);
    }

    @Override
    public int hashCode() {
        int result = rolledNumber.hashCode();
        result = 31 * result + userSelectedNumber.hashCode();
        result = 31 * result + rewardAmount.hashCode();
        result = 31 * result + commissionAmount.hashCode();
        result = 31 * result + payoutAddress.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "{" +
                "\"rolledNumber\": " + rolledNumber +
                ", \"userSelectedNumber\": " + userSelectedNumber +
                ", \"rewardAmount\": " + rewardAmount +
                ", \"commissionAmount\": " + commissionAmount +
                ", \"payoutAddress\": " + prepareJSONString(payoutAddress) +
                ", \"remarks\": " + prepareJSONString(remarks) +
                "}";
    }

    public final static String prepareJSONString(Object value) {

        return (value != null ? ("\"" + value + "\"") : "\"\"");

    }
}
