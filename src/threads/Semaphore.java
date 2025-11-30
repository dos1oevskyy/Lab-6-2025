package threads;

public class Semaphore {
    private int readers = 0;
    private int writers = 0;
    private int writeRequests = 0;

    public synchronized void beginRead() throws InterruptedException {
        while (writers > 0 || writeRequests > 0) {
            wait();
        }
        readers++;
    }

    public synchronized void endRead() {
        readers--;
        notifyAll();
    }

    public synchronized void beginWrite() throws InterruptedException {
        writeRequests++;
        while (readers > 0 || writers > 0) {
            wait();
        }
        writeRequests--;
        writers++;
    }

    public synchronized void endWrite() {
        writers--;
        notifyAll();
    }
}
