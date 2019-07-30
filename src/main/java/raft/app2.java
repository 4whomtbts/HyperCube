package raft;

import proto.HyperCube;
import util.Config;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class app2 {
    /*
    public static URL getDefaultFeaturesFiles(int index){
        String configName = "HyperConfig.proto"+index+".json";
        System.out.println("Config File Name : "+configName);
        try {
            return new File("D:\\desktop\\naver_d2_fest\\hyper_cube\\src\\main\\resources\\HyperConfig.proto"+index+".json").toURI()
                    .toURL();
        }catch (Exception e){
            return null;
        }
    }


    public static void main(String[] args ) throws IOException {

        HyperCube.Config c1 = Config.parseConfig("HyperConfig1.json"
        );
        HyperCube.Config c2 = Config.parseConfig("HyperConfig2.json"
        );
        HyperCube.Config c3 = Config.parseConfig("HyperConfig3.json"
        );

        String localHost = "127.0.0.1";
        RaftNode s1 = new RaftNode("seoul_region",localHost,1996);
        Peer peerS1 = new Peer(s1);
        s1.setHyperCubeConfig(c1);
        RaftNode s2 = new RaftNode("us_east_region",localHost,1997);
        Peer peerS2 = new Peer(s2);
        s2.setHyperCubeConfig(c2);
        RaftNode s3 = new RaftNode("east_europe_region",localHost,1998);
        Peer peerS3 = new Peer(s3);
        s3.setHyperCubeConfig(c3);
        s1.start();s2.start();s3.start();
        List<Peer> s1_peer = new ArrayList<>(2);
        s1_peer.add(peerS2);s1_peer.add(peerS3);
        List<Peer> s2_peer = new ArrayList<>(2);
        s2_peer.add(peerS1);s2_peer.add(peerS3);
        List<Peer> s3_peer = new ArrayList<>(2);
        s3_peer.add(peerS1);s3_peer.add(peerS2);


        s1.init();s2.init();s3.init();
        try {
            peerS1.shutdown();

            peerS2.shutdown();
            peerS3.shutdown();

        }catch (Exception e){


        }
        try {
            s1.blockUntilShutdown();
            s2.blockUntilShutdown();
            s3.blockUntilShutdown();

        }catch (Exception e){

            e.printStackTrace();
        }

    }
    */
}
