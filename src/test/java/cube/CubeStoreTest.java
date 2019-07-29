package cube;

import org.junit.Test;
import proto.Raft;
import raft.Cube;
import raft.CubeStore;
import util.HyperUtil;

import static org.junit.Assert.*;

import java.util.ArrayList;

public class CubeStoreTest {


    @Test
    public void appendTest() {

        HyperUtil.deleteAllCubeFiles();


        CubeStore store = new CubeStore();
        ArrayList<Raft.LogEntry> entries = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            Raft.LogEntry item = Raft.LogEntry.newBuilder().setIndex(i).setTerm(256).build();
            entries.add(item);
        }
        store.append(entries);

        assertEquals(0, store.getLastEntry().getStartIndex());


        store = new CubeStore();
        entries = new ArrayList<>();
        for (int i = 0; i < 10001; i++) {
            Raft.LogEntry item = Raft.LogEntry.newBuilder().setIndex(i).build();
            entries.add(item);
        }
        store.append(entries);

        assertEquals(10000, store.getLastEntry().getStartIndex());


        store = new CubeStore();
        entries = new ArrayList<>();
        for (int i = 0; i < 20001; i++) {
            Raft.LogEntry item = Raft.LogEntry.newBuilder().setIndex(i).setTerm(1000000000).build();
            entries.add(item);
        }
        store.append(entries);

        assertEquals(30000, store.getLastEntry().getStartIndex());

        store = new CubeStore();
        entries = new ArrayList<>();
        for (int i = 0; i < 100001; i++) {
            Raft.LogEntry item = Raft.LogEntry.newBuilder().setIndex(i).setTerm(16).build();
            entries.add(item);
        }
        store.append(entries);

        assertEquals(130000, store.getLastEntry().getStartIndex());


    }

    @Test
    public void loadCubesToMemoryTest() {


        CubeStore store = new CubeStore();
        ArrayList<Raft.LogEntry> entries = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            Raft.LogEntry item = Raft.LogEntry.newBuilder().setIndex(i).setTerm(1).build();
            entries.add(item);
        }
        store.append(entries);


        assertEquals(0, store.getLastEntry().getStartIndex());
        assertEquals(Raft.LogEntry.newBuilder().setIndex(0).setTerm(1).build()
                , store.getLastEntry().getBlockLogEntry(0));

        assertEquals(Raft.LogEntry.newBuilder().setIndex(1).setTerm(1).build()
                , store.getLastEntry().getBlockLogEntry(1));


    }

    @Test
    public void appendUntilAcceptable() {

        CubeStore store = new CubeStore();
        ArrayList<Raft.LogEntry> entries = new ArrayList<>();
        Raft.LogEntry item = null;
        for (int i = 0; i < 9998; i++) {
            item = Raft.LogEntry.newBuilder().setIndex(i).setTerm(1).build();
            entries.add(item);
        }
        store.append(entries);
        long oldFileSize = store.getLastEntry().getFileSize();

        ArrayList<Raft.LogEntry> entries1 = new ArrayList<>();
        entries1.add(item);
        store.append(entries1);
        int entrySize = item.getSerializedSize();
        long expectedFileSize = store.getLastEntry().getFileSize();

        assertEquals(
                expectedFileSize,
                oldFileSize + entrySize

        );


    }

}
