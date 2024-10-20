package org.hsse.news;

@SuppressWarnings({"PMD.AtLeastOneConstructor", "PMD.ShortClassName"})
public class App {
    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(final String[] args) {
        final App app = new App();
        System.out.println(app.getGreeting()); // NOPMD - suppressed SystemPrintln - temporal behaviour
    }
}
