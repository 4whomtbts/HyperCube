package raft;

import proto.Raft;
import util.HyperUtil;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.rmi.activation.ActivationGroup_Stub;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

public class CubeStore {


    private TreeMap<Long, Cube> _startIndexKeyCubeMap;
    private String _logDirPath;
    private Raft.LogMetaData _metaData;


    public CubeStore(){
            _startIndexKeyCubeMap = new TreeMap<>();
            _logDirPath = HyperUtil.getCubeDirPath();

            loadCubesToMemory();

            if(_startIndexKeyCubeMap.size() > 0){
                for(Cube cube : _startIndexKeyCubeMap.values()){
                    cube.loadCubeDataToMemory();
                }
            }

    }

    public void readMetaData(){

        String fileName = HyperUtil.getMetaDirPath();
        File file = new File(fileName);

        //try(RandomAccessFile raf = new RandomAccessF//e(file,"r"))



    }
    public void updateMetadata(){


    }


    public void loadCubesToMemory(){
        // TODO file name error check
        try {

            List<String> cubeFiles = HyperUtil.getSortedFilesInDirectory(_logDirPath,_logDirPath,false);
            for(String cubeFile : cubeFiles){

                long startIndex = Long.parseLong(cubeFile.split("-")[0]);
                Cube cube = new Cube(startIndex);
                _startIndexKeyCubeMap.put(startIndex,cube);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void append(List<Raft.LogEntry> entries ){

        Cube lastEntry;
        if(_startIndexKeyCubeMap.size() == 0){
            lastEntry = new Cube(0);
            _startIndexKeyCubeMap.put(0L,lastEntry);

        }else{

            lastEntry =  _startIndexKeyCubeMap.lastEntry().getValue();
        }

        List<Raft.LogEntry> residueEntries = lastEntry.appendUntilAcceptable(entries);
        if(residueEntries != null){
            long  nextCubeStartLogIndex = lastEntry.getLastIndex() + 1;
            Cube newCube = new Cube(nextCubeStartLogIndex);
            _startIndexKeyCubeMap.put(nextCubeStartLogIndex,newCube);
            append(residueEntries);
        }
    }

    public Cube getLastEntry(){
        return _startIndexKeyCubeMap.lastEntry().getValue();
    }


}
