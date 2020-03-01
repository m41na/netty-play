package works.hop.wait.notify.process;

import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchService;

import static java.nio.file.StandardOpenOption.*;

public class FileQueue {

    private final CharBuffer charBuf;
    private final int bufferSize;
    private final Object lock = new Object();

    private final WatchService watcher = FileSystems.getDefault().newWatchService();
    private final int READING = 0;
    private final int WRITING = 1;
    private int mode = 0;

    public FileQueue(int mode) throws IOException {
        this(System.getProperty("user.dir"), "shared.mem", 4096);
    }

    public FileQueue(String folder, String file, int bufferSize) throws IOException {
        this.mode = mode;
        this.bufferSize = bufferSize;
        Path path = Paths.get(folder, file);
        FileChannel channel = FileChannel.open(path, READ, WRITE, CREATE);

        MappedByteBuffer b = channel.map(FileChannel.MapMode.READ_WRITE, 0, bufferSize);
        this.charBuf = b.asCharBuffer();
    }

    public void put(char[] data) {
        synchronized (lock) {
            try {
                while (mode == WRITING) {
                    lock.wait();
                }

                //write to buffer
                charBuf.put(data);
                System.out.println("Waiting for client.");
                while (charBuf.get(0) != '\0') ;
                System.out.println("Finished waiting.");

                //update current
                mode = READING;
                lock.notifyAll();
            } catch (InterruptedException e) {
                e.printStackTrace(System.err);
            }
        }
    }

    public char[] take() {
        synchronized (lock) {
            try {
                while (mode == READING) {
                    lock.wait();
                }

                //read from buffer
                CharBuffer out = CharBuffer.allocate(bufferSize);
                char c;
                while ((c = charBuf.get()) != 0) {
                    out.put(c);
                }
                charBuf.put(0, '\0');

                //update current
                mode = WRITING;
                lock.notifyAll();
                return out.array();
            } catch (InterruptedException e) {
                e.printStackTrace(System.err);
                return null;
            }
        }
    }
}
