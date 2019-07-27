package raft;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import proto.Raft;
import proto.RaftServiceGrpc;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static util.universalUtil.buildSender;

public class Peer {

    RaftNode server;
    private String serverId;
    private String serverIp;
    private int port;
    private final ManagedChannel channel;

    private final RaftServiceGrpc.RaftServiceBlockingStub blockingStub;
    private final RaftServiceGrpc.RaftServiceStub asyncStub;
    private Raft.VoteResponse voteResponse;
    private Raft.AppendEntriesResponse appendEntriesResponse;
    private Raft.Node mySender;


    Peer(RaftNode server) {
        this.server = server;
        this.serverIp = server.getServerIp();
        this.serverId = server.getServerId();
        this.port = server.getPort();
        System.out.println(port + "번에 peer 생성");
        this.channel = ManagedChannelBuilder.forAddress(
                "127.0.0.1", this.port
        ).usePlaintext().build();
        this.blockingStub = RaftServiceGrpc.newBlockingStub(channel);
        this.asyncStub = RaftServiceGrpc.newStub(channel);
    }

    /*
    Peer(Raft.Node server){
            this(new RaftNode(server.getServerId(),
                    server.getServerIp(), server.getPort()));
    }
    */

    public int getPort() {
        return this.port;
    }

    public String getServerId() {
        return this.serverId;
    }

    public String getServerIp() {
        return this.server.getServerIp();
    }

    public void shutdown() throws InterruptedException {
        // 클라이언트 사용이 완료된 후, 5초가 지나면 채널을 닫아줍니다.
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public Raft.AppendEntriesResponse appendEntries(Raft.Node sender) {

        Raft.AppendEntriesRequest appendEntriesRequest = Raft.AppendEntriesRequest
                .newBuilder()
                .setSender(sender)
                .build();
        Raft.AppendEntriesResponse res =null;
        try {
            res = blockingStub.appendEntries(appendEntriesRequest);
        }catch (Exception e){}

        return res;
    }


    public Raft.VoteResponse requestVote(Raft.Node sender) {

        // System.out.println("리퀘스트 보트 : "+this.server.nextTerm());
        final Raft.VoteRequest req = Raft.VoteRequest.newBuilder()
                .setSender(sender)
                .setTerm(this.server.getTerm() + 1)
                .build();

        Raft.VoteResponse response = null;

        try {
             response= blockingStub.requestVotes(req);
        }catch (Exception e){

        }
        return response;


    }



}
