package nia.chapter7;

import java.util.Collections;
import java.util.List;

/**
 * Listing 7.1 Executing tasks in an event loop
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */
public class EventLoopExamples {
    /**
     * Listing 7.1 Executing tasks in an event loop
     * 在事件循环中执行任务
     * */
    public static void executeTaskInEventLoop() {
        boolean terminated = true;
        //...
        while (!terminated) {
            // 阻塞，直到有事件已经就绪可被运行
            List<Runnable> readyEvents = blockUntilEventsReady();
            // 循环遍历，并处理所有的事件
            for (Runnable ev: readyEvents) {
                ev.run();
            }
        }
    }

    // 建立一个不可变的List<Runnable>
    private static final List<Runnable> blockUntilEventsReady() {
        return Collections.<Runnable>singletonList(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
