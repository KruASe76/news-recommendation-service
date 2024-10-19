package org.hsse.news;

public class App {
    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) {
        App app = new App();
        System.out.println(app.getGreeting());

        String garbage = "unused string";
    }
}
