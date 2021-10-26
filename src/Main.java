public class Main {
    public static void main(String[] args) {
        //Define path of directories here (directories must be unzipped)
        String spamAnlern = "resources/spam-anlern";
        String hamAnlern = "resources/ham-anlern";
        String spamKallibrierung = "resources/spam-kallibrierung";
        String hamKallibrierung = "resources/ham-kallibrierung";
        String spamTest = "resources/spam-test";
        String hamTest = "resources/ham-test";

        //Init und anlernen
        Spamfilter sf = new Spamfilter();
        System.out.println("Anlernen...");
        sf.learnData(spamAnlern,hamAnlern);

        //Kalibrierung
        System.out.println("######## Kallibrierung #########");
        System.out.println("Schwellenwert: "+sf.getThreshold());
        System.out.println("Alpha: "+sf.getAlpha());
        System.out.println(sf.testData(spamKallibrierung,hamKallibrierung));

        //Test
        System.out.println("######## Test #########");
        System.out.println("Schwellenwert: "+sf.getThreshold());
        System.out.println("Alpha: "+sf.getAlpha());
        System.out.println(sf.testData(spamTest,hamTest));
    }
}
