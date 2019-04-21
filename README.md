## nuls-roll

- Smart Contract for Dice Game based on NULS block chain.
- Front end game application calls this contract to roll the dice.
- Player selects number between 1 to 100 and calls rollDice method.Smart Contract generates random number between 1 to 100 and if the user  selected number is less than the generated number then contract sends the winning amount to the player instantly.

### Smart Contract has following methods:

1. fundContract() : Owner(contract creator) of the contract can deposit NULS to contract address. Only owner can deposit
2. withdrawFromContract(amount): Amount can be withdrawn from Contract. Only owner of the contract can withdraw
3. getContactBalance() : Get the balance of the contract
4. rollDice(selectedNumber) : Player can select the number from 1 to 100. This method rolls random number between 1 to 100 based on NULS random number generation.

### Winning Reward Payout

- Rewards are payed out to the winners instantly
- Board commission is 1% of total reward
- Reward payout is depends on the number selected by the user. Following formula calculates the reward amount.
      
      rewardAmount = 100/(selectedNo -1) * betAmount
      finalReward = rewardAmount - (1% commission of rewardAmount)
      
      Example: betAmount = 10 NULS , SelectedNumber = 26
      rewardAmount = 100/(26-1) * 10
      finalReward  = 40 - (1% of 40) = 39.6
      
      
 - All bet and reward amounts are in NULS
 - Minimum bet amount is 1 NULS
 - maximum bet amount is 100 NULS
 
 Looking for someone to build UI for this smart contract.
