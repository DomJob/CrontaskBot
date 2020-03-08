package application;

import domain.Time;

public class TaskExecutor implements Runnable {
    private CrontaskBot bot;

    public TaskExecutor(CrontaskBot bot) {
        this.bot = bot;
    }

    @Override
    public void run() {
        bot.checkTasks(Time.now());
    }
}
