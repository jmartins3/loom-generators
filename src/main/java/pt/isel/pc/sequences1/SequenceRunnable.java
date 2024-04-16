package pt.isel.pc.sequences1;

public abstract class SequenceRunnable<T> implements Runnable {
   
    private Sequence.SequenceIterator<T> currIt;
    
    protected SequenceRunnable( ) { }
    
    public void setIterator(Sequence.SequenceIterator<T> currIt) {
        this.currIt = currIt;
    }
    
    public void yield(T value) {
        currIt.yield(value);
    }
}
