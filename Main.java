package com.company;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;


public class Main {

    public static void main(String[] args) {
        File file = new File(args[0]);
        StringBuilder sentence = new StringBuilder();
        int sentenceCount;
        int wordCount;
        int charCount;
        int syllablesCount;
        int polyCount;
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                sentence.append(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        charCount = sentence.toString().replaceAll("\\s", "").length();
        wordCount = sentence.toString().split("\\s").length;
        sentenceCount = sentence.toString().split("[.!?]").length;
        syllablesCount = getSyllablesCount(sentence);
        polyCount = getPolysyllablesCount(sentence);
        float ari = ARI(sentenceCount, wordCount, charCount);
        float fk = FK(wordCount, syllablesCount, sentenceCount);
        float smog = SMOG(polyCount, sentenceCount);
        float cl = CL(charCount, sentenceCount, wordCount);

        System.out.println("Words: " + wordCount);
        System.out.println("Sentences: " + sentenceCount);
        System.out.println("Characters: " + charCount);
        System.out.println("Syllables: " + syllablesCount);
        System.out.println("Polysyllables: " + polyCount);
        System.out.print("Enter the score you want to calculate (ARI, FK, SMOG, CL, all): ");
        Scanner scanner = new Scanner(System.in);
        String option = scanner.next();
        System.out.println();
        switch (option) {
            case "ARI":
                System.out.printf("Automated Readability Index: %.2f (about %d year olds)\n", ari, gradeLevel(Math.round(ari)));
                break;
            case "FK":
                System.out.printf("Flesch–Kincaid readability tests: %.2f (about %d year olds)\n", fk, gradeLevel(Math.round(fk)));
                break;
            case "SMOG":
                System.out.printf("Simple Measure of Gobbledygook: %.2f (about %d year olds)\n", smog, gradeLevel(Math.round(smog)));
                break;
            case "CL":
                System.out.printf("Coleman–Liau index: 10.66: %.2f (about %d year olds)\n", cl, gradeLevel(Math.round(cl)));
                break;
            case "all":
                System.out.printf("Automated Readability Index: %.2f (about %d year olds)\n", ari, gradeLevel(Math.round(ari)));
                System.out.printf("Flesch–Kincaid readability tests: %.2f (about %d year olds)\n", fk, gradeLevel(Math.round(fk)));
                System.out.printf("Simple Measure of Gobbledygook: %.2f (about %d year olds)\n", smog, gradeLevel(Math.round(smog)));
                System.out.printf("Coleman–Liau index: 10.66: %.2f (about %d year olds)\n", cl, gradeLevel(Math.round(cl)));
                break;
            default:
                break;
        }
        System.out.println();
        System.out.printf("This text should be understood in average by %.2f year olds.", getAverageAge(
                gradeLevel(Math.round(ari)),
                gradeLevel(Math.round(fk)),
                gradeLevel(Math.round(smog)),
                gradeLevel(Math.round(cl)))
        );
    }

    public static float getAverageAge(int age1, int age2, int age3, int age4) {
        return ((float) age1 + age2 + age3 + age4) / 4;
    }

    public static int gradeLevel(int score) {
        if (score == 1) {
            return 6;
        } else if (score == 2) {
            return 7;
        } else if (score == 3) {
            return 9;
        } else if (score == 4) {
            return 10;
        } else if (score == 5) {
            return 11;
        } else if (score == 6) {
            return 12;
        } else if (score == 7) {
            return 13;
        } else if (score == 8) {
            return 14;
        } else if (score == 9) {
            return 15;
        } else if (score == 10) {
            return 16;
        } else if (score == 11) {
            return 17;
        } else if (score == 12) {
            return 18;
        } else if (score == 13) {
            return 24;
        } else {
            return 25;
        }
    }

    public static float ARI(int sentenceCount, int wordCount, int charCount) {
        return 4.71f * ((float) charCount / wordCount) + 0.5f * ((float) wordCount / sentenceCount) - 21.43f;
    }

    public static float FK(int wordCount, int syllableCount, int sentenceCount) {
        return  0.39f * ((float) wordCount / sentenceCount) + 11.8f * ((float) syllableCount / wordCount) - 15.59f;
    }

    public static float SMOG(int polyCount, int sentenceCount) {
        return 1.043f * (float) Math.sqrt(polyCount * ((float) 30 / sentenceCount)) + 3.1291f;
    }

    public static float CL(int charCount, int sentenceCount, int wordCount) {
        float L = (float) charCount / wordCount * 100;
        float S = (float) sentenceCount / wordCount * 100;
        return  0.0588f * L - 0.296f * S - 15.8f;
    }

    public static int getSyllablesCount (StringBuilder s) {
        String s1 = s.toString();

        s1 = s1.replaceAll("\\b[Tt]he\\b", " a ");
        s1 = s1.replaceAll("\\b[Ww]e\\b", " a ");
        s1 = s1.replaceAll("\\d+,?\\d?", "a");
        s1 = s1.replaceAll("e\\b", "");
        s1 = s1.replaceAll("[aeiouyAEIOUY]{2,}", "a");
        s1 = s1.replaceAll("[^aeiouyAEIOUY]", "");

        return s1.length();
    }

    public static int getPolysyllablesCount(StringBuilder s) {
        String[] s1 = s.toString().split("[ ]");
        int total = 0;
        for (int i = 0; i < s1.length; i++) {
            int count = getSyllablesCount(new StringBuilder(s1[i]));
            if (count >= 3) {
                total++;
            }
        }
        return total;
    }
}
