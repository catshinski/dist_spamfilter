public class Word {
    private String word;
    private double amountHam;
    private double amountSpam;

    public Word(String word, double amountHam, double amountSpam) {
        this.word = word;
        this.amountHam = amountHam;
        this.amountSpam = amountSpam;
    }

    public String getWord() {
        return word;
    }

    public double getAmountHam() {
        return amountHam;
    }

    public void addOneHam() {
        this.amountHam = this.amountHam + 1.0;
    }

    public double getAmountSpam() {
        return amountSpam;
    }

    public void addOneSpam() {
        this.amountSpam = this.amountSpam + 1.0;
    }

}
