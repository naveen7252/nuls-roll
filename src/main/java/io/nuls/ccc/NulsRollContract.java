package io.nuls.ccc;

import io.nuls.ccc.model.RollResult;
import io.nuls.ccc.util.Owner;
import io.nuls.contract.sdk.*;
import io.nuls.contract.sdk.annotation.Payable;
import io.nuls.contract.sdk.annotation.Required;
import io.nuls.contract.sdk.annotation.View;

import static io.nuls.contract.sdk.Utils.emit;
import static io.nuls.contract.sdk.Utils.require;
import static io.nuls.contract.sdk.Utils.sha3;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class NulsRollContract implements Contract {

    private final Integer ROLL_TIMES = 5;
    private final Integer DICE_RANGE = 100;
    private final Integer REWARD_COMMISSION_RATE = 1;
    private final Long ONE_NULS_IN_NA = 100000000L;

    private final BigInteger MIN_BET_AMOUNT = BigInteger.valueOf(ONE_NULS_IN_NA);
    private final BigInteger MAX_BET_AMOUNT = BigInteger.valueOf(ONE_NULS_IN_NA*100);

    private Owner owner;

    public NulsRollContract(){
        this.owner = Owner.getOwner(Msg.sender());
    }

    @Payable
    public void fundContract(){
        owner.requireOwner("Only owner of the contract can deposit to Contract");
    }

    @Payable
    public void withdrawFromContract(@Required double amount){
        owner.requireOwner("Only owner of the contract can withdraw");
        BigInteger amtNa = toNa(amount);
        require(amtNa.compareTo(contractBalance()) <= 0,"Contract doesn't have enough balance");
        Msg.sender().transfer(amtNa);
    }

    @View
    public BigDecimal getContactBalance(){
        BigInteger contractBal = contractBalance();
        if(contractBal.compareTo(BigInteger.ZERO) == 0){
            return new BigDecimal(contractBal);
        }
        return new BigDecimal(contractBal).movePointLeft(8);
    }

    @Payable
    public RollResult rollDice(@Required Integer selectedNumber){
        require(!(selectedNumber <= 4 || selectedNumber >= 96),"Select number between 5 and 95");
        Address sender = Msg.sender();
        BigInteger betAmount = Msg.value();
        require((betAmount.compareTo(MIN_BET_AMOUNT) > -1),"Minimum Bet Amount is 1 NULS");
        require((betAmount.compareTo(MAX_BET_AMOUNT) < 1), "Maximum Bet Amount is 100 NULS");
        List<Integer>  values = dice(Block.newestBlockHeader().getHeight(),128,DICE_RANGE,ROLL_TIMES);
        Integer rolledFaceValue = values.get(ROLL_TIMES/2);
        RollResult rollResult = new RollResult();
        rollResult.setRolledNumber(rolledFaceValue);
        rollResult.setUserSelectedNumber(selectedNumber);
        rollResult.setRemarks("Sorry!!! You Lost Bet");
        if(rolledFaceValue < selectedNumber){
            BigDecimal rewardBaseFolds = calculateWinPayoutFolds(selectedNumber);
            BigDecimal temp = rewardBaseFolds.multiply(BigDecimal.valueOf(betAmount.longValue()));
            BigInteger reward = temp.toBigInteger();
            BigDecimal tempComm = temp.multiply(BigDecimal.valueOf(REWARD_COMMISSION_RATE));
            BigDecimal commValue = tempComm.divide(BigDecimal.valueOf(100),8,BigDecimal.ROUND_DOWN);
            BigInteger finalCommAmt = commValue.toBigInteger();
            BigInteger finalUserReward = reward.subtract(finalCommAmt);
            sender.transfer(finalUserReward);
            owner.getOwnerAddress().transfer(finalCommAmt);
            rollResult.setCommissionAmount(finalCommAmt);
            rollResult.setRewardAmount(finalUserReward);
            rollResult.setPayoutAddress(sender.toString());
            rollResult.setRemarks("Congratulations!!! You Won Bet");
        }
        emit(new RollEvent(rollResult));
        return rollResult;
    }


    private BigDecimal calculateWinPayoutFolds(Integer selectedNo){
        Integer winPercent = selectedNo - 1;
        return BigDecimal.valueOf(100).divide(BigDecimal.valueOf(winPercent),8,BigDecimal.ROUND_DOWN);
    }

    private BigInteger contractBalance(){
        return Msg.address().balance();
    }

    private BigInteger toNa(double nuls){
        BigDecimal amt = BigDecimal.valueOf(nuls);
        return amt.multiply(BigDecimal.valueOf(ONE_NULS_IN_NA)).toBigInteger();
    }
    private List<Integer> dice(long endHeight, int count, int range, int times) {
        BigInteger originSeed = Utils.getRandomSeed(endHeight, count, "sha3");
        if (originSeed.equals(BigInteger.ZERO)) {
            return null;
        }
        BigInteger wrapperRange = BigInteger.valueOf((long) range);
        List<Integer> resultList = new ArrayList<>(times);
        for (int i = 0; i < times; i++) {
            if(i == 0) {
                BigInteger mod = originSeed.mod(wrapperRange);
                resultList.add(mod.intValue());
            } else {
                BigInteger multiply = originSeed.multiply(BigInteger.valueOf(i + 1));
                String s = sha3(multiply.toByteArray());
                byte[] decode = decode(s);
                BigInteger bigInteger = new BigInteger(decode);
                BigInteger mod = bigInteger.mod(wrapperRange);
                resultList.add(mod.intValue());
            }
        }
        return resultList;
    }

    private byte[] decode(String hexString) {
        byte[] bts = new byte[hexString.length() / 2];
        for (int i = 0; i < bts.length; i++) {
            bts[i] = (byte) Integer.parseInt(hexString.substring(2 * i, 2 * i + 2), 16);
        }
        return bts;
    }
}
