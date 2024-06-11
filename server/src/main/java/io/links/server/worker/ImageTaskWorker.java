package io.links.server.worker;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Queue;
import java.util.TimerTask;
import java.util.concurrent.*;

@Component
@RequiredArgsConstructor
public class ImageTaskWorker {
    private final TimerTask resizeAndSaveImageTask;
    public static Queue<String> queueOfImagesToProcess = new ConcurrentLinkedQueue<>();

    @PostConstruct
    public void runScheduledThreadPoolWithImageTasks() {
        ScheduledExecutorService executorService
                = Executors.newScheduledThreadPool(2);

        ScheduledFuture<?> resultFuture
                = executorService.scheduleAtFixedRate(
                        resizeAndSaveImageTask,
                        0,
                        10,
                        TimeUnit.MILLISECONDS
                );
    }

}
