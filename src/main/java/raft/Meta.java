package raft;

import proto.Raft;
import sun.util.resources.cldr.mas.CalendarData_mas_KE;
import sun.util.resources.cldr.mer.CalendarData_mer_KE;
import util.HyperUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Meta {

    private long lastIndex;
    private String _metaFileFullPath= HyperUtil.getMetaFileFullPath();
    private String _metaFileName="meta";
    private Raft.LogMetaData _metaData;

    public Meta() {

    }

    public void readMetaData(){

        String fileName = HyperUtil.getMetaDirPath();
        File file = new File(fileName);

        try(RandomAccessFile raf = new RandomAccessFile(file,"r")){

            Raft.LogMetaData metaData = HyperUtil.readProtoFromFile(
                    raf, Raft.LogMetaData.class);

            _metaData = metaData;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateMetaData(Long currentTerm ,Integer votedFor, Long firstLogIndex){

        Raft.LogMetaData.Builder builder = Raft.LogMetaData.newBuilder(_metaData);

        if(currentTerm != null){
            builder.setCurrentTerm(currentTerm);
        }
        if(votedFor != null){
            builder.setVotedFor(votedFor);
        }
        if(firstLogIndex != null){
            builder.setFirstLogIndex(firstLogIndex);
        }
        _metaData = builder.build();

        File file = new File(_metaFileFullPath);
        try ( RandomAccessFile raf = new RandomAccessFile(file , "rw")){
            HyperUtil.writeProtoToFile(raf,_metaData);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Raft.LogMetaData getMetaData(){
        return _metaData;
    }
}
