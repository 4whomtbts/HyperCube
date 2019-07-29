package cube;
import org.junit.Assert;
import org.junit.Test;
import proto.Raft;
import raft.Cube;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import static org.junit.Assert.*;


public class CubeTest {


    @Test
    public void makeRandomAccessFileTest(){

    }

    @Test
    public void appendUntilAcceptableTest(){

        List<Raft.LogEntry> entries = new ArrayList<>();
        List<Raft.LogEntry> expectedEntries = new ArrayList<>();

        Raft.LogEntry item = Raft.LogEntry.newBuilder().setIndex(1).build();
        for(int i=0; i < 10; i++){
            entries.add(item);
        }
        Cube cube = new Cube(0);
        List<Raft.LogEntry> result = cube.appendUntilAcceptable(entries);



        cube = new Cube(0);
        entries = new ArrayList<>();
        for(int i=0; i < 10000; i++){
            entries.add(item);

        }
        List<Raft.LogEntry> result1 = cube.appendUntilAcceptable(entries);
        assertNull(result1);


        cube = new Cube(0);
        entries = new ArrayList<>();
        expectedEntries = new ArrayList<>();
        expectedEntries.add(item);
        for(int i=0; i < 10001; i++){
            entries.add(item);
        }
        List<Raft.LogEntry> result2 = cube.appendUntilAcceptable(entries);
        assertEquals(result2.toArray(),expectedEntries.toArray());


        cube = new Cube(0);
        entries = new ArrayList<>();
        expectedEntries = new ArrayList<>();
        for(int i=0; i < 9999; i++){
            expectedEntries.add(item);

        }
        for(int i=0; i < 19999; i++){
            entries.add(item);

        }

        List<Raft.LogEntry> result3 = cube.appendUntilAcceptable(entries);
        assertEquals(result3.toArray(),expectedEntries.toArray());

        assertEquals(9999,cube.getLastIndex());

    }

    @Test
    public void append(){

    }
}
