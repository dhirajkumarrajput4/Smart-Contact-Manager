package com.smart;

import java.util.Random;

public class Test {

    public static int[] allotStudSections(int[] sectionSize, int studCount) {
        Random random = new Random();
        int[] allotments = new int[studCount];

        for (int i = 0; i < studCount; i++) {
            int index = random.nextInt(sectionSize.length);
            allotments[i] = sectionSize[index];
        }

        return allotments;
    }

    public static void main(String[] args) {
        int[] sectionSize = {121, 12, 13, 45, 67};
        int numOfStudents = 10;

        int[] allotments = allotStudSections(sectionSize, numOfStudents);

        for (int i = 0; i < allotments.length; i++) {
            System.out.println("Student " + (i + 1) + " allotted section: " + allotments[i]);
        }
    }
}
