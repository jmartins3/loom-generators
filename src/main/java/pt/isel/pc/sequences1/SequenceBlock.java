package pt.isel.pc.sequences1;

import pt.isel.pc.sequences1.Sequence;

public interface SequenceBlock<T>   {
    void  exec(Sequence.SequenceIterator<T> seq);
}
