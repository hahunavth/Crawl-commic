package com.hahunavth;

import java.io.IOException;
import java.util.Scanner;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class Help {

    Scanner sc = new Scanner(System.in);

    public String getDirect() throws IOException {
        String path = "./";
        JFileChooser file_chooser = new JFileChooser();
        file_chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (!(file_chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)) {
            path = file_chooser.getCurrentDirectory().getPath();
        }
        return path;
    }

    public static void main(String[] args) throws IOException {
        System.out.println(new Help().getDirect());
    }

    public String inputString() {
        String chapName = JOptionPane.showInputDialog("Enter Chap number : ");
        System.out.println(chapName);
        return chapName;
    }

    public int getIntInRange(int min , int max) {
        int choose  = 0;
        while (true) {
            try {
                System.out.println("Enter number : ");
                choose = Integer.parseInt(sc.nextLine());
                if(choose >= min && choose <= max){
                    return choose;
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Choosen wrong !!!");
            }
        }
    }
}