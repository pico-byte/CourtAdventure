public class Choice {
    String text;
    String nextDialogueID;
    String conditionItem;
    String paymentItem;



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



}
