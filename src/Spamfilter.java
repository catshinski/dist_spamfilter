import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class Spamfilter {
    private int totalSpamMails = 0;
    private int totalHamMails = 0;
    private HashMap<String, Word> wordList = new HashMap<>();
    private final double alpha = 0.00001;
    private final double threshold = 0.8;
    private enum Type {SPAM, HAM}

    public double getAlpha() {
        return alpha;
    }

    public double getThreshold() {
        return threshold;
    }

    public void learnData(String pathSpam, String pathHam){
        learnDirectory(pathSpam,Type.SPAM);
        learnDirectory(pathHam,Type.HAM);
    }

    public String testData(String pathSpam, String pathHam){
        double pSpamSpam = getSpamPercentageFromDirectory(pathSpam);
        double pSpamHam = getSpamPercentageFromDirectory(pathHam);
        return "Korrekt Spam: "+pSpamSpam+"%, Korrekt Ham: "+(100-pSpamHam)+"%";
    }

    private void learnDirectory(String path, Type type){
        //Goes through a directory, updates the total Spam
        //or Ham count and adds the Words to the wordList
        File dir = new File(path);
        File filelist[] = dir.listFiles();
        if(filelist != null) {
            for (File file : filelist) {
                HashSet<String> wordSet = getWordSetFromFile(file);
                if (type == Type.SPAM) {
                    totalSpamMails++;
                    addSpamToMap(wordSet);
                } else {
                    totalHamMails++;
                    addHamToMap(wordSet);
                }
            }
        }
    }

    private HashSet<String> getWordSetFromFile(File file){
        //read words from file into set to prevent duplicates
        Scanner scLine = null;
        HashSet<String> wordSet = new HashSet<>();
        try {
            scLine = new Scanner(file);
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
        while(scLine != null && scLine.hasNextLine()){
            Scanner scWord = new Scanner(scLine.nextLine());
            while(scWord.hasNext()){
                wordSet.add(scWord.next());
            }
        }
        return wordSet;
    }

    private void addSpamToMap(HashSet wordSet){
        //Goes through a wordSet and updates the Spam amount of a word
        //or adds the word to the Map if not present yet
        for (String word : (Iterable<String>) wordSet) {
            if (wordList.containsKey(word)) {
                wordList.get(word).addOneSpam();
            } else {
                wordList.put(word, new Word(word, alpha, 1.0));
            }
        }
    }

    private void addHamToMap(HashSet wordSet){
        //Goes through a wordSet and updates the Ham amount of a word
        //or adds the word to the Map if not present yet
        for (String word : (Iterable<String>) wordSet) {
            if (wordList.containsKey(word)) {
                wordList.get(word).addOneHam();
            } else {
                wordList.put(word, new Word(word, 1.0, alpha));
            }
        }
    }

    private double getSpamProbabilityForMail(HashSet<String> mailSet){
        //Calculating spam probability according to https://en.wikipedia.org/wiki/Naive_Bayes_spam_filtering
        //pWS = Probability Word in Spam, pWH = Probability Word in Ham
        //pSW = Probability message is Spam, knowing Word is in it
        double n = 0;
        for (String mailWord: (Iterable<String>) mailSet){
            if(wordList.containsKey(mailWord)){
                Word w = wordList.get(mailWord);
                double pWS = w.getAmountSpam() / totalSpamMails;
                double pWH = w.getAmountHam() / totalHamMails;
                double pSW = pWS / (pWS + pWH);
                n += (Math.log(1 - pSW) - Math.log(pSW));
            }
        }
        return 1 / (1 + Math.exp(n));
    }

    private double getSpamPercentageFromDirectory(String path){
        //Goes through directory and returns the percentage of Spam-Mails that were found
        int total = 0;
        int isSpam = 0;
        File dir = new File(path);
        File filelist[] = dir.listFiles();
        if(filelist != null) {
            for (File file : filelist) {
                total++;
                HashSet<String> wordSet = getWordSetFromFile(file);
                double pSpam = getSpamProbabilityForMail(wordSet);
                if (pSpam >= threshold) {
                    isSpam++;
                }
            }
        }
        return 100.0 / total * isSpam;
    }
}
