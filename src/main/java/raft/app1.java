package raft;

import proto.HyperCube;
import util.Config;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class app1 {
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


        String localHost = "127.0.0.1";
        RaftNode s1 = new RaftNode();
        s1.setHyperCubeConfig(c1);
        s1.start();
        s1.init();

        try {
            s1.blockUntilShutdown();

        }catch (Exception e){

                e.printStackTrace();
        }

    }
}
