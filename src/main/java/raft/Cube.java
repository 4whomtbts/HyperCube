package raft;

import hyperCube.Option;
import proto.HyperCube;
import proto.Raft;
import util.Config;
import util.HyperUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.activation.ActivationGroup_Stub;
import java.util.ArrayList;
import java.util.List;

public class Cube {

    private static class Block {

        public long offset;
        public Raft.LogEntry entry;

        public Block(long offset, Raft.LogEntry entry) {
            this.offset = offset;
            this.entry = entry;
        }

        public int getSize(){
            return entry.getSerializedSize();
        }
    }

    private long _startIndex;
    private long _endIndex;
    private volatile long _totalSize;
    private int _fileSize;
    private String _fileName;
    private String _fileAbsPath;
    private String _fileRelPath;
    private RandomAccessFile _RAF;
    private List<Block> _entryList;
    private long __MOCK_FILESIZE = (int)Math.pow(1024,3);
    private HyperCube.Config config = Config.getConfig();
    private boolean _canWrite = false;
    private int _maxEntry =  10000;



    private String _logDirPath;

    public Cube(long startIndex){
        this._startIndex = startIndex;

        _entryList = new ArrayList<>();
        _logDirPath = HyperUtil.getCubeDirPath();

        makeRandomAccessFile();
    }

    public Cube(long startIndex, String fileName){
        this(startIndex);
    }


    public void loadCubeDataToMemory(){

        try {

            long totalLength = _RAF.length();
            long offset = 0;
            while(offset < totalLength){

                    Raft.LogEntry entry = HyperUtil.readProtoFromFile(_RAF,Raft.LogEntry.class);

                    if(entry == null){
                        throw new RuntimeException("read segment log failed");
                    }

                    Block newBlock = new Block(offset,entry);
                    _entryList.add(newBlock);
                    _fileSize += newBlock.getSize();

                    offset = _RAF.getFilePointer();
            }

        }catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void appendEntry(List<Raft.LogEntry> entries){
        try {

            for(Raft.LogEntry entry : entries){

                long fp = this._RAF.getFilePointer();
                Block newBlock = new Block(fp, entry);

                _entryList.add(newBlock);
                _fileSize += newBlock.getSize();
                _endIndex += entry.getSerializedSize();


                HyperUtil.writeProtoToFile(_RAF,entry);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public List<Raft.LogEntry> appendUntilAcceptable(List<Raft.LogEntry> entries){

        int numberOfEntry = entries.size();
        int residue = _maxEntry - _entryList.size();


        if(residue  >= numberOfEntry ){
            appendEntry(entries);
            return null;
        }else{

            appendEntry(entries.subList(0,residue));

            try {
                _RAF.close();
                setCanWrite(false);

            } catch (IOException e) {
                e.printStackTrace();

                //TODO roll-back 기능
            }

            return entries.subList(residue, numberOfEntry-1);
        }
    }


    private void makeRandomAccessFile(){

        File logStoreFile = new File(HyperUtil.getCubeFileFullPath(_startIndex,_endIndex));


        try {

            if(!logStoreFile.exists()){
                logStoreFile.createNewFile();
            }

            _RAF = new RandomAccessFile(logStoreFile,"rw");
            setCanWrite(true);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public long getLastIndex(){
        return _startIndex + _maxEntry -1;
    }
    public long getStartIndex(){
        return _startIndex;
    }
    public Raft.LogEntry getBlockLogEntry(int index){
        return _entryList.get(index).entry;
    }
    public long getFileSize(){
        return _fileSize;
    }
    private void setCanWrite(boolean flag){
        _canWrite = flag;
    }







}
