package cube;

import org.junit.Test;
import proto.Raft;
import raft.Cube;
import raft.CubeStore;
import util.HyperUtil;
import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HyperUtilTest {

    public List<String> getNewCubeFiles() throws IOException {
        return HyperUtil.getSortedFilesInDirectory(
                HyperUtil.getCubeDirPath(),
                HyperUtil.getCubeDirPath(),
                true
        );
    }

    @Test
    public void getCubeDirPathTest(){

        String result = HyperUtil.getCubeDirPath();
        assertEquals(result,"C:\\Users\\use\\HyperCube");
    }

    @Test
    public void getCubeFileFullPathTest(){

        String result = HyperUtil.getCubeFileFullPath(0,0);
        assertEquals(
               "C:\\Users\\use\\HyperCube\\000000000000000-000000000000000.cube",
                result
        );
    }

    @Test
    public void deleteAllCubeFiles(){

        String result = HyperUtil.getCubeDirPath();
        HyperUtil.deleteAllCubeFiles();
    }

    @Test
    public void getSortedFilesInDirectoryTest(){

        HyperUtil.deleteAllCubeFiles();

        try {
            List<String> cubeFiles = getNewCubeFiles();
            assertEquals(new ArrayList<>(),cubeFiles);

            Cube cube = new Cube(0);

            cubeFiles = getNewCubeFiles();

            assertEquals(1,cubeFiles.size());


            assertEquals(
                    HyperUtil.getCubeFileFullPath(0,0),
                    cubeFiles.get(0)
            );


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void readProtoFromFileTest(){

        CubeStore store=  new CubeStore();

        //HyperUtil.readProtoFromFile()
    }

    @Test
    public void getMetaDirPath(){

        String metaDir = HyperUtil.getMetaDirPath();
        assertEquals("C:\\Users\\use\\HyperCube\\meta",metaDir);
    }

    @Test
    public void getMetaDataFullPath(){

        String metaDir = HyperUtil.getMetaFileFullPath();
        assertEquals("C:\\Users\\use\\HyperCube\\meta\\meta",metaDir);
    }


    @Test
    public void getHyperCubeRootDir(){
        assertEquals(HyperUtil.getHyperCubeParentDir(),"C:\\Users\\use");
    }

    @Test
    public void getCRC32() {

        Raft.LogEntry entry = Raft.LogEntry.newBuilder().setIndex(1).build();
        long crc = HyperUtil.getCRC32(entry.toByteArray());
        assertEquals(2082222136,crc);

    }


    }
