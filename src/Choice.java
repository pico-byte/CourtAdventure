public class Choice {
    String text;
    String nextDialogueID;
    String conditionItem;
    String paymentItem;
    String rewardItem; // New field for items that get added to inventory



    public  Choice(String text, String nextID){
        this.text = text;
        this.nextDialogueID = nextID;
    }

    public void setConditionItem(String conditionItem) {
        this.conditionItem = conditionItem;
    }

    public void setPaymentItem(String paymentItem) {
        this.paymentItem = paymentItem;
    }

    public void setRewardItem(String rewardItem) {
        this.rewardItem = rewardItem;
    }
}
