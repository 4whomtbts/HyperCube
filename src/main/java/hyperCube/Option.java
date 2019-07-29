package hyperCube;

import raft.StateMachine;

import java.io.File;

public class Option {

    private static Option Inst;

    private String _logFullPath;

    public static Option getInstance(){
        if(Inst == null){
            return Inst = new Option();
        }
        return Inst;
    }

    public int getMaxCubeFileSize(){
        return 0;
    }





}
