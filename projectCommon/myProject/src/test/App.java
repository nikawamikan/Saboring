package test;

import projectCommon.ConsoleReaderNext;

public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");

        ConsoleReaderNext mikan = new ConsoleReaderNext();
        mikan.getString("入力テストです。");

        System.out.println(mikan);

        System.out.println("コンソール入力文字化け問題解決に至らず困ってるなう");
    }
}
