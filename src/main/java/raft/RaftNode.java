package raft;

import io.grpc.*;
import proto.HyperCube;
import proto.Raft;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.stream.IntStream;

import static util.HyperUtil.buildSender;

public class RaftNode {

    public enum NodeState {
        FOLLOWER,
        CANDIDATE,
        LEADER
    }

    //private final int host;
    private final String serverId;
    private final int port;
    private final Server server;
    private List<Peer> peerList;
    private ConcurrentMap<String, Peer> peerMap;
    private String serverIp;
    private String leaderId;
    private NodeState state;
    private HyperCube.Config config;
    private ExecutorService execService;
    private CompletionService<Raft.VoteResponse> voteResponseCompletionService;
    private ScheduledExecutorService electionScheduledExcutorService;
    private ScheduledExecutorService scheduledExecutorService;
    private ScheduledFuture heartBeatScheduledFuture;
    private ScheduledFuture electionScheduledFuture;
    private ScheduledFuture heartBeatLaunchFuture;
    private AtomicLong latestTerm;
    private AtomicLong latestVoteTerm;
    private AtomicInteger grantedVote;
    private CubeStore _cubeStore;
    private int peerCount;
    private Lock lock;
    private final Object syncObj = new Object();
    // 1초안에 peer 로 부터 응답이 없으면 삭제. 다음 투표에서 뒤늦게 응답이 오는 것을 방지하기 위함
    private int awaitingVoteResponseTime = 10000; //TODO should be defined in conf file
    private Raft.Node mySender;

    public RaftNode(String serverId, String serverIp, int port) {
        this.port = port;
        this.serverId = serverId;
        System.out.println(port + " 번 서버실행");
        this.server = ServerBuilder.forPort(port).addService(new RaftServiceImpl(this)).build();
        this.state = NodeState.FOLLOWER;
        this.leaderId = null;
        this.peerMap = new ConcurrentHashMap<>();
        this.serverIp = serverIp;
        this.grantedVote = new AtomicInteger();
        this.grantedVote.set(0);
        this.latestTerm = new AtomicLong();
        this.latestTerm.set(0);
        this.latestVoteTerm = new AtomicLong();
        this.latestVoteTerm.set(0);
        this.mySender = buildSender(this.getServerId(), this.getPort());
        scheduledExecutorService = Executors.newScheduledThreadPool(2);
        scheduledExecutorService.scheduleWithFixedDelay(() -> {

        }, 1000, 1000, TimeUnit.MILLISECONDS);

        _cubeStore = new CubeStore();

    }

    private int
    getRandomWaitingTime() {
        return (new Random().nextInt(150) + 150);
    }

    public void append(List<Raft.LogEntry> entries){
        _cubeStore.append(entries);




    }
    public void init() {


        execService = new ThreadPoolExecutor(
                60,
                60,
                60,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>()
        );

        voteResponseCompletionService = new ExecutorCompletionService<>(execService);
        registerPeersByConf();
        resetElectionTimeOut();
    }

    private void registerPeersByConf() {

        for (Raft.Node node : config.getPeerList()) {

            RaftNode newPeer = new RaftNode(node.getServerId(), node.getServerIp(), node.getPort());
            this.peerMap.put(newPeer.getServerId(), new Peer(newPeer));
        }
    }


    public void start() throws IOException {
        server.start();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    blockUntilShutdown();

                } catch (Exception e) {
                }
                RaftNode.this.stop();
            }
        });
    }

    public void stop() {
        if (server != null) {
            server.shutdown();      // 서버를 종료합니다.
        }
    }

    public void resetElectionTimeOut() {

        cancelElection();

        int randomWaitingTime = getRandomWaitingTime();
        electionScheduledFuture = scheduledExecutorService.schedule(() -> {

                startVote();

        }, randomWaitingTime, TimeUnit.MILLISECONDS);
    }

    public void cancelElection(){

        this.state = NodeState.FOLLOWER;

        if (electionScheduledFuture != null && !electionScheduledFuture.isDone()) {
            electionScheduledFuture.cancel(true);
        }
    }

    public void startVote() {


        this.incGrantedVote();// 스스로에게 투표

        //System.out.println("구분선");
        IntStream.range(0, config.getPeerCount()).forEach(i -> {
            Peer peer = peerMap.get(config.getPeer(i).getServerId());
            System.out.println("startVote  " + i + " : " + peer.getServerId());
            //Raft.VoteResponse futureRes=null;
            voteResponseCompletionService.submit(new Callable<Raft.VoteResponse>() {
                @Override
                public Raft.VoteResponse call() throws Exception {
                    return peer.requestVote(mySender);
                }
            });
        });


            for(int i=0 ; i < config.getPeerCount(); i++){

                try {

                    Future<Raft.VoteResponse> fu = voteResponseCompletionService.take();

                Raft.VoteResponse voteResponse = fu.get(awaitingVoteResponseTime, TimeUnit.MILLISECONDS);


                if (voteResponse.getVoteGranted()) {
                    this.incGrantedVote();

                    System.out.println("node " + getServerId() + "'s received vote : " + grantedVote.get());


                    if (grantedVote.get() >= (peerCount / 2) + 1) {
                        System.out.println("node " + getServerId() + " will be leader");
                        becomeLeader();
                    }
                }

            } catch (InterruptedException e) {
                System.out.println("Interrupt has occurred while server was attempting to start vote ::" + e.getCause().getMessage());
                e.printStackTrace();
            } catch (ExecutionException e1) {
                System.out.println("Execution exception has occurred during server was attempting to start vote");
                e1.printStackTrace();

            } catch (TimeoutException e) {
                System.out.println("vote request has been rejected due to exceed maximum response waiting time");
                e.printStackTrace();

            }

        }

        if (grantedVote.get() < (peerCount / 2) + 1) {
            state = NodeState.FOLLOWER;


        }

        grantedVote.set(0);
        //resetElectionTimeOut();
    }



    public void resetHeartBeatTimer() {

        if (heartBeatScheduledFuture != null && !heartBeatScheduledFuture.isDone()) {
            heartBeatScheduledFuture.cancel(true);
        }

        heartBeatScheduledFuture = scheduledExecutorService.schedule(new Runnable() {
            @Override
            public void run() {

                if(state != NodeState.LEADER) {
                    System.out.println("리더가 불가용한 상태임을  [ " + getServerId() + " ] 가 알림 ");
                    resetElectionTimeOut();
                }
            }

        }, RaftOptions.heartBeatWaitingMilliSeconds, TimeUnit.MILLISECONDS);
    }


    public void startHeartBeat() {


        for (String key : peerMap.keySet()) {

            Peer peer = peerMap.get(key);
            System.out.println(getServerId() + " 가 하트비트 보냄 [ " + peer.getServerId() + " ] " + "map : " + peerMap.size());
            execService.submit(() -> {
                appendEntries(peer);

            });
        }
        //resetHeartBeatTimer();
        heartBeatLaunchFuture = scheduledExecutorService.schedule(new Runnable() {
            @Override
            public void run() {
                    startHeartBeat();
                }
        }, RaftOptions.heartBeatLaunchingInterval, TimeUnit.MILLISECONDS);

    }


    public void appendEntries(Peer peer) {


        if(this.state == NodeState.LEADER){
            Raft.AppendEntriesRequest.Builder reqBuilder = Raft.AppendEntriesRequest.newBuilder();





        }else{




        }

        peer.appendEntries(mySender);



    }


    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();  // 서버가 SIGTERM을 받아서 종료될 수 있도록 await 합니다.
        }
    }


    public void setPeerList(List<Peer> list) {

        this.peerList = list;
    }

    public void setHyperCubeConfig(HyperCube.Config conf) {

        System.out.println("------------------------------------------\r\n" +
                "HYPER CUBE CONFIG REGISTERED\r\n" +
                "Host ID : " + conf.getNode().getServerId() + "\r\n" +
                "Host IP : " + conf.getNode().getServerIp() + "\r\n" +
                "Port : " + conf.getNode().getPort()
        );

        System.out.println("Peer Info =>");
        for (Raft.Node node : conf.getPeerList()) {
            System.out.println("Peer Ip : " + node.getServerId());
            System.out.println("Peer Port : " + node.getPort());
        }

        this.config = conf;
    }

    public void becomeLeader() {
        this.grantedVote.set(0);
        this.state = NodeState.LEADER;
        this.leaderId = this.getServerId();
        System.out.println(this.getServerId() + " 가 리더가 됨");
        if (electionScheduledFuture != null && !electionScheduledFuture.isDone()) {
            electionScheduledFuture.cancel(true);
        }

        startHeartBeat();
    }

    public synchronized void incGrantedVote() {
        int currentVote = this.grantedVote.get();
        this.grantedVote.set(currentVote + 1);
    }


    public int getPort() {
        return this.port;
    }

    public long getTerm() {
        return this.latestTerm.get();
    }

    public synchronized void setTerm(long newLatestTerm) {
        this.latestTerm.set(newLatestTerm);
    }

    public synchronized long nextTerm() {
        this.latestTerm.set(getTerm() + 1);
        return getTerm();
    }

    public synchronized long getLatestVotedTerm() {
        return this.latestVoteTerm.get();
    }

    public void setLatestVotedTerm(long newLatestTerm) {
        this.latestVoteTerm.set(newLatestTerm);
    }

    public String getServerId() {
        return this.serverId;
    }

    public String getServerIp() {
        return this.serverIp;
    }

    public int getGrantedVote() {
        return this.grantedVote.get();
    }

    public synchronized boolean IsMajorityVoteGranted() {

        if (this.getGrantedVote() > (this.peerCount / 2)) {
            return true;
        }
        return false;
    }

    public NodeState getState(){
        return this.state;
    }

    public void beFollower(){
        this.state =  NodeState.FOLLOWER;
    }
    public void beCandidate(){
        this.state =  NodeState.CANDIDATE;
    }
    public void beLeader(){
        this.state =  NodeState.LEADER;
    }


    public void setLeaderId(String leaderId){
        this.leaderId = leaderId;
    }

    public void demoteLeader(){
        this.leaderId  = null;
    }

}
