package main;

import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.TimeUnit;
import sun.nio.fs.*;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

/**
 * Created by Duart on 30/03/2017.
 */
public class PluginWatcher implements Runnable{

    private WatchService watcher;
    Path dir = Paths.get("");
    private WatchKey key;


    public PluginWatcher(){
        try {
            watcher = FileSystems.getDefault().newWatchService();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public WatchKey getCurrentKey(){
        return key;
    }

    public void run(){
        register();
        for (;;) {

            // wait for key to be signaled
            try {
                key = watcher.take();
            } catch (InterruptedException x) {
                return;
            }

            for (WatchEvent<?> event: key.pollEvents()) {
                WatchEvent.Kind<?> kind = event.kind();

                // This key is registered only
                // for ENTRY_CREATE events,
                // but an OVERFLOW event can
                // occur regardless if events
                // are lost or discarded.
                if (kind == OVERFLOW) {
                    continue;
                }

                // The filename is the
                // context of the event.
                WatchEvent<Path> ev = (WatchEvent<Path>)event;
                Path filename = ev.context();

                // Verify that the new
                //  file is a text file.
                try {
                    // Resolve the filename against the directory.
                    // If the filename is "test" and the directory is "foo",
                    // the resolved name is "test/foo".
                    Path child = dir.resolve(filename);
                    if (!Files.probeContentType(child).equals("text/plain")) {
                        System.err.format("New file '%s'" +
                                " is not a plain text file.%n", filename);
                        continue; }
                } catch (IOException x) {
                    System.err.println(x);
                    continue;
                }

                // Email the file to the
                //  specified email alias.
                System.out.println("New file detected: " + filename);
                //Details left to reader....
            }

            // Reset the key -- this step is critical if you want to
            // receive further watch events.  If the key is no longer valid,
            // the directory is inaccessible so exit the loop.
            boolean valid = key.reset();
            if (!valid) {
                break;
            }
        }
    }

    private void register() {
        try {
            // TODO: 30/03/2017 Support modify
            WatchKey key = dir.register(watcher,ENTRY_CREATE);
        } catch (IOException x) {
            System.err.println(x);
        }
    }

    /**
     * Ver se é java
     * Ver se extende shape
     * Ver se implementa os métodos
     */
}
