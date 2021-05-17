package dev.chrisg.cpu8;

public class Main {
    public static void main(String[] args) {
        CPU cpu = new CPU();
        cpu.load("/home/chrisg/Documents/projects/java/8bit_CPU/src/main/java/dev/chrisg/cpu8/programs/print8.cpu8");
        cpu.trace();
    }
}
