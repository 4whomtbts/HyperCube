package raft.impl;

import org.rocksdb.Checkpoint;
import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import proto.KVStoreMessage;
import raft.StateMachine;

import java.io.File;

/**
 * Raft状态机接口类
 * Created by wenweihu86 on 2017/5/10.
 *
 */


/** Notice
 *  This source code file is included in
 *  raft-java project
 *  https://github.com/wenweihu86/raft-java
 *  Original author of this source code is wenweihu86 under apache license.
 *
 */


public class RockStateMachineImpl implements StateMachine {


    static {
        RocksDB.loadLibrary();
    }

    private RocksDB db;
    private String raftDataDir;

    public RockStateMachineImpl(String raftDataDir) {
        this.raftDataDir = raftDataDir;
    }

    @Override
    public void writeSnapshot(String snapshotDir) {
        Checkpoint checkpoint = Checkpoint.create(db);
        try {
            checkpoint.createCheckpoint(snapshotDir);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void readSnapshot(String snapshotDir) {
        try {
            // copy snapshot dir to data dir
            if (db != null) {
                db.close();
            }

            // open rocksdb data dir
            Options options = new Options();
            options.setCreateIfMissing(true);
            db = RocksDB.open(options, "");
        } catch (Exception ex) {
        }
    }

    @Override
    public void apply(byte[] dataBytes) {
        try {
            KVStoreMessage.SetRequest request = KVStoreMessage.SetRequest.parseFrom(dataBytes);
            db.put(request.getKey().getBytes(), request.getValue().getBytes());
        } catch (Exception ex) {
        }
    }

    public KVStoreMessage.GetResponse get(KVStoreMessage.GetRequest request) {
        try {
            KVStoreMessage.GetResponse.Builder responseBuilder = KVStoreMessage.GetResponse.newBuilder();
            byte[] keyBytes = request.getKey().getBytes();
            byte[] valueBytes = db.get(keyBytes);
            if (valueBytes != null) {
                String value = new String(valueBytes);
                responseBuilder.setValue(value);
            }
            KVStoreMessage.GetResponse response = responseBuilder.build();
            return response;
        } catch (RocksDBException ex) {
            return null;
        }
    }

}
