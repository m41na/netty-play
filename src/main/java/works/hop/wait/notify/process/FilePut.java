package works.hop.wait.notify.process;

import java.io.File;
import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

public class FilePut {

    private static final Object lock = new Object();
    private static String FILE_NAME = "shared.mem";

    public static void main(String[] args) {
        Path path = Paths.get(System.getProperty("user.dir"), FILE_NAME);
        Thread fileWatcher = new Thread(new FileWatcher(path.toString()) {
            @Override
            public void onModified() {
                System.out.println("changed");
                synchronized (lock) {
                    lock.notifyAll();
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace(System.err);
                    }
                }
            }
        });
        fileWatcher.start();

        Thread fileQueue = new Thread(() -> {
            try {
                FileChannel channel = FileChannel.open(path, StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE);
                MappedByteBuffer b = channel.map(FileChannel.MapMode.READ_WRITE, 0, 4096);
                CharBuffer charBuf = b.asCharBuffer();

                char[] string = "Hello client\0".toCharArray();
                charBuf.put(string);

                System.out.println("Waiting for client.");
                synchronized (lock) {
                    lock.notifyAll();
                    try {
                        lock.wait();
                        while (charBuf.get(0) != '\0') ;
                    } catch (InterruptedException e) {
                        e.printStackTrace(System.err);
                    }
                }
                System.out.println("Finished waiting.");
            } catch (IOException e) {
                e.printStackTrace(System.err);
            }
        });
        fileQueue.start();
    }

    public static abstract class FileWatcher implements Runnable {

        private Path folderPath;
        private String watchFile;

        public FileWatcher(String watchFile) {
            Path filePath = Paths.get(watchFile);

            boolean isRegularFile = Files.isRegularFile(filePath);

            if (!isRegularFile) {
                // Do not allow this to be a folder since we want to watch files
                throw new IllegalArgumentException(watchFile + " is not a regular file");
            }

            // This is always a folder
            folderPath = filePath.getParent();

            // Keep this relative to the watched folder
            this.watchFile = watchFile.replace(folderPath.toString() + File.separator, "");
        }

        public void run() {
            // We obtain the file system of the Path
            FileSystem fileSystem = folderPath.getFileSystem();

            // We create the new WatchService using the try-with-resources block
            try (WatchService service = fileSystem.newWatchService()) {
                // We watch for modification events
                folderPath.register(service, ENTRY_MODIFY);

                // Start the infinite polling loop
                while (true) {
                    // Wait for the next event
                    WatchKey watchKey = service.take();

                    for (WatchEvent<?> watchEvent : watchKey.pollEvents()) {
                        // Get the type of the event
                        WatchEvent.Kind<?> kind = watchEvent.kind();

                        if (kind == ENTRY_MODIFY) {
                            Path watchEventPath = (Path) watchEvent.context();

                            // Call this if the right file is involved
                            if (watchEventPath.toString().equals(watchFile)) {
                                onModified();
                            }
                        }
                    }

                    if (!watchKey.reset()) {
                        // Exit if no longer valid
                        break;
                    }
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace(System.err);
            }
        }

        public abstract void onModified();
    }
}
