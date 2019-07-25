package util;

import com.google.protobuf.util.JsonFormat;
import proto.HyperCube;
import proto.Raft;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

public class universalUtil {

    public static Raft.HyperCubeConfig parseConfig(URL path) throws IOException {
        InputStream input = path.openStream();
        try {
            Reader reader = new InputStreamReader(input, Charset.forName("UTF-8"));
            try {
                Raft.HyperCubeConfig.Builder config = Raft.HyperCubeConfig.newBuilder();
                JsonFormat.parser().merge(reader, config);
                return config.build();
            } finally {
                reader.close();
            }

        }finally {
            input.close();
        }
    }

    public static HyperCube.HyperResponse buildSuccessResponse(){


        return HyperCube.HyperResponse.newBuilder().setResCode(
                HyperCube.ResCode.SUCCESS
        ).setMsg(HyperCube.ResMessage.newBuilder().setMsg("SUCCESS").build()).build();
    }

    public static HyperCube.HyperResponse buildVerbosFailResponse(
            String where, String detail, String serverId, int port
    ){
        HyperCube.ResMessage newVerboseMessage =
                HyperCube.ResMessage.newBuilder()
                .setMsg("At "+where +" [ "+detail+" ]"+"\r\n"+
                        " server information "+serverId +":"+port).build();

        return HyperCube.HyperResponse.newBuilder()
                .setResCode(HyperCube.ResCode.FAIL)
                .setMsg(newVerboseMessage).build();
    }

    public static HyperCube.HyperResponse buildFailResponse(){
        return HyperCube.HyperResponse.newBuilder()
                .setResCode(HyperCube.ResCode.FAIL)
                .build();
    }

    public static Raft.Node buildSender(String server_id, int port){
        return Raft.Node.newBuilder().setServerId(server_id).setPort(port).build();
    }
}

