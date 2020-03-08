package application;

import application.entities.Update;

public class EventHandler implements Runnable {
    private CrontaskBot bot;

    public EventHandler(CrontaskBot bot) {
        this.bot = bot;
    }

    @Override
    public void run() {
        bot.handleEvents();
    }
}
