package test;

import main.PluginWatcher;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by Duart on 30/03/2017.
 */
class PluginWatcherTest {
    File newFile = new File("src/main/shapes/user/Test.java");

    @Test
    void run() {

        PluginWatcher pluginWatcher = new PluginWatcher();
        Thread thread = new Thread(pluginWatcher);

        try {
            thread.start();
            Thread.sleep(1000);
            File newFile = new File("src/main/shapes/user/Test.java");
            newFile.createNewFile();
            Thread.sleep(1000);
            assertNotNull(pluginWatcher.getCurrentKey());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @AfterEach
    void tearDown(){
       if(newFile.exists()){
           newFile.delete();
       }
    }
}