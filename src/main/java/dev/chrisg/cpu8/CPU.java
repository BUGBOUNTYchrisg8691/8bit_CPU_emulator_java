package dev.chrisg.cpu8;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CPU {
    private int[] ram;
    private int[] reg;
    private int PC = 0;

    // CMP Flags
    // reg[4]
    private final int FL = 4;
    // FL reg masks
    private final int LT_MASK = 0b100;
    private final int GT_MASK = 0b10;
    private final int EQ_MASK = 0b1;

    // Interrupts
    // Interrupts enabled
    private boolean IE;
    // reg[5]
    private final int IM = 5;
    // reg[6]
    private final int IS = 6;
    // Interrupt Vector Table
    private final int I7 = 0xFF;
    private final int I6 = 0xFE;
    private final int I5 = 0xFD;
    private final int I4 = 0xFC;
    private final int I3 = 0xFB;
    private final int I2 = 0xFA;
    private final int I1 = 0xF9;
    private final int I0 = 0xF8;

    // Stack Pointer
    // reg[7]
    private final int SP = 7;

    public CPU() {
        this.ram = new int [256];
        this.reg = new int [8];
        this.reg[this.SP] = 0xF4;
        this.IE = true;
    }

    public int[] getRam() {
        return ram;
    }

    public void setRam(int[] ram) {
        this.ram = ram;
    }

    public int[] getReg() {
        return reg;
    }

    public void setReg(int[] reg) {
        this.reg = reg;
    }

    public int getPC() {
        return PC;
    }

    public void setPC(int PC) {
        this.PC = PC;
    }

    public int getFL() {
        return FL;
    }

    public int getLT_MASK() {
        return LT_MASK;
    }

    public int getGT_MASK() {
        return GT_MASK;
    }

    public int getEQ_MASK() {
        return EQ_MASK;
    }

    public boolean isIE() {
        return IE;
    }

    public void setIE(boolean IE) {
        this.IE = IE;
    }

    public int getIM() {
        return IM;
    }

    public int getIS() {
        return IS;
    }

    public int getI7() {
        return I7;
    }

    public int getI6() {
        return I6;
    }

    public int getI5() {
        return I5;
    }

    public int getI4() {
        return I4;
    }

    public int getI3() {
        return I3;
    }

    public int getI2() {
        return I2;
    }

    public int getI1() {
        return I1;
    }

    public int getI0() {
        return I0;
    }

    public int getSP() {
        return SP;
    }

    public int ram_read(int mar) {
        return this.ram[mar];
    }

    public void ram_write(int mdr, int mar) {
        this.ram[mar] = mdr;
    }

    public void load(String filename) {
        try {
            int address = 0;
            File program = new File(filename);

            Scanner reader = new Scanner(program);
            Pattern pattern = Pattern.compile("[0-1]{8}");

            while (reader.hasNextLine()) {
                String[] data = reader.nextLine().trim().split("\\s+");
                Matcher matcher = pattern.matcher(data[0]);

                if (matcher.matches()) {
                    System.out.println(data[0]);
                    this.ram_write(Integer.parseInt(data[0]), address);
                    address++;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error loading program.");
            e.printStackTrace();
        }
    }

    public void trace() {
        String out = String.format(
                "TRACE: PC: %02X | RAM@PC: %02X, RAM@PC+1: %02X, RAM@PC+2: %02X",
                this.PC,
                this.ram_read(this.PC),
                this.ram_read(this.PC+1),
                this.ram_read(this.PC+2)
        );
        System.out.println(out);
    }

    private int[] decode_instruction(int instruction) {
        private final int MOV_PC_MASK = 0b11;
        private final int FIRST_BIT_MASK = 0b1;
        private final int INSTRUCTION_IDENTIFIER_MASK = 0b1111;

        int mov_pc = ((instruction >> 6) & MOV_PC_MASK) + 1;
        int is_alu = (instruction >> 5) & FIRST_BIT_MASK;
        int sets_pc = (instruction >> 4) & FIRST_BIT_MASK;
        int instruction_identifier = instruction & INSTRUCTION_IDENTIFIER_MASK;

        int[] out = new int[4];
        out[0] = mov_pc;
        out[1] = is_alu;
        out[2] = sets_pc;
        out[3] = instruction_identifier;

        return out;
    }
}
