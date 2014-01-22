package servertools.core.task;

public interface ITickTask {

    abstract void tick();

    void onComplete();

    abstract boolean isComplete();
}
