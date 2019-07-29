package raft;

import javafx.geometry.HPos;
import proto.Raft;
import sun.util.resources.cldr.ln.CalendarData_ln_CD;
import util.HyperUtil;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Snapshot {

    private String _snapshotDir = HyperUtil.getCubeDirPath();
    private Lock _lock = new ReentrantLock();

    public class SnapshotDataFile{
        public SnapshotDataFile(String fileName, RandomAccessFile dataFile) {
            this._fileName = fileName;
            this._dataFile = dataFile;
        }

        public String _fileName;
        public RandomAccessFile _dataFile;
    }

    private TreeMap<String, SnapshotDataFile> openSnapshotDataFile() {
        TreeMap<String,SnapshotDataFile> snapshotDataFileTreeMap = new TreeMap<>();

        try {
            List<String> fileNames = HyperUtil.getSortedFilesInDirectory(_snapshotDir,_snapshotDir,false);

            for( String fileName : fileNames ){
                RandomAccessFile raf = HyperUtil.openFile(_snapshotDir, fileName, "r");
                SnapshotDataFile dataFile = new SnapshotDataFile(fileName, raf);
                snapshotDataFileTreeMap.put(fileName,dataFile);
            }

        }catch (Exception e){

        }

        return snapshotDataFileTreeMap;
    }


    private Raft.InstallSnapshotRequest buildInstallSnapshotRequest(
            String lastFileName, long lastOffset, long lastLength
    ){

        Raft.InstallSnapshotRequest.Builder builder = Raft.InstallSnapshotRequest.newBuilder();
        TreeMap<String,Snapshot.SnapshotDataFile> treeMap = new TreeMap<>();

        _lock.lock();






        return null;
    }



}

