package raft;

import io.grpc.stub.StreamObserver;
import proto.HyperCube;
import proto.Raft;
import proto.RaftServiceGrpc;

import java.util.logging.Logger;

public class RaftServiceImpl extends RaftServiceGrpc.RaftServiceImplBase {
    private static final Logger logger =
            Logger.getLogger(RaftServiceImpl.class.getName());
    private RaftNode server;

    RaftServiceImpl() {

    }

    RaftServiceImpl(RaftNode server) {
        this.server = server;
    }

    @Override
    public void requestVotes(Raft.VoteRequest req, StreamObserver<Raft.VoteResponse> observer) {

        System.out.println("RequestVotes" +
                this.server + " received vote request by " + req.getSender().getServerId() +
                "");

        Raft.VoteResponse res;
        this.server.resetElectionTimeOut();


        if (req.getTerm() > this.server.getTerm() || req.getTerm() > this.server.getLatestVotedTerm()) {

            this.server.setTerm(req.getTerm());
            this.server.setLatestVotedTerm(req.getTerm());
            res = Raft.VoteResponse.newBuilder().setTerm(
                    req.getTerm()).setVoteGranted(true).build();
        } else {
            res = Raft.VoteResponse.newBuilder().setTerm(
                    req.getTerm()).setVoteGranted(false).build();
        }

        observer.onNext(res);
        observer.onCompleted();
    }

    @Override
    public void appendEntries(Raft.AppendEntriesRequest req,
                              StreamObserver<Raft.AppendEntriesResponse> resObserver) {

        this.server.cancelElection();
        this.server.resetHeartBeatTimer();
        this.server.setLeaderId(req.getSender().getServerId());
        this.server.beFollower();

        if(req.getEntriesCount() != 0){


        }
        System.out.println(this.server.getServerId()+" 가 "+req.getSender().getServerId()+" 에게 하트비트 받음");
        Raft.AppendEntriesResponse res = Raft.AppendEntriesResponse.newBuilder().build();
        resObserver.onNext(res);
        resObserver.onCompleted();
    }

}
