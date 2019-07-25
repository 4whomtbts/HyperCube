package proto;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.22.1)",
    comments = "Source: proto/raft.proto")
public final class RaftServiceGrpc {

  private RaftServiceGrpc() {}

  public static final String SERVICE_NAME = "proto.RaftService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<proto.Raft.VoteRequest,
      proto.Raft.VoteResponse> getRequestVotesMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "RequestVotes",
      requestType = proto.Raft.VoteRequest.class,
      responseType = proto.Raft.VoteResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<proto.Raft.VoteRequest,
      proto.Raft.VoteResponse> getRequestVotesMethod() {
    io.grpc.MethodDescriptor<proto.Raft.VoteRequest, proto.Raft.VoteResponse> getRequestVotesMethod;
    if ((getRequestVotesMethod = RaftServiceGrpc.getRequestVotesMethod) == null) {
      synchronized (RaftServiceGrpc.class) {
        if ((getRequestVotesMethod = RaftServiceGrpc.getRequestVotesMethod) == null) {
          RaftServiceGrpc.getRequestVotesMethod = getRequestVotesMethod = 
              io.grpc.MethodDescriptor.<proto.Raft.VoteRequest, proto.Raft.VoteResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "proto.RaftService", "RequestVotes"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.Raft.VoteRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.Raft.VoteResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new RaftServiceMethodDescriptorSupplier("RequestVotes"))
                  .build();
          }
        }
     }
     return getRequestVotesMethod;
  }

  private static volatile io.grpc.MethodDescriptor<proto.Raft.AppendEntriesRequest,
      proto.Raft.AppendEntriesResponse> getAppendEntriesMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "AppendEntries",
      requestType = proto.Raft.AppendEntriesRequest.class,
      responseType = proto.Raft.AppendEntriesResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<proto.Raft.AppendEntriesRequest,
      proto.Raft.AppendEntriesResponse> getAppendEntriesMethod() {
    io.grpc.MethodDescriptor<proto.Raft.AppendEntriesRequest, proto.Raft.AppendEntriesResponse> getAppendEntriesMethod;
    if ((getAppendEntriesMethod = RaftServiceGrpc.getAppendEntriesMethod) == null) {
      synchronized (RaftServiceGrpc.class) {
        if ((getAppendEntriesMethod = RaftServiceGrpc.getAppendEntriesMethod) == null) {
          RaftServiceGrpc.getAppendEntriesMethod = getAppendEntriesMethod = 
              io.grpc.MethodDescriptor.<proto.Raft.AppendEntriesRequest, proto.Raft.AppendEntriesResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "proto.RaftService", "AppendEntries"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.Raft.AppendEntriesRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.Raft.AppendEntriesResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new RaftServiceMethodDescriptorSupplier("AppendEntries"))
                  .build();
          }
        }
     }
     return getAppendEntriesMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static RaftServiceStub newStub(io.grpc.Channel channel) {
    return new RaftServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static RaftServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new RaftServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static RaftServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new RaftServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class RaftServiceImplBase implements io.grpc.BindableService {

    /**
     * <pre>
     *rpc SayHello(HelloRequest) returns (HelloReply){}
     *rpc RequestVotes(stream VoteRequest) returns (VoteResponse){}
     * </pre>
     */
    public void requestVotes(proto.Raft.VoteRequest request,
        io.grpc.stub.StreamObserver<proto.Raft.VoteResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getRequestVotesMethod(), responseObserver);
    }

    /**
     * <pre>
     *&#47;rpc AppendEntries(stream AppendEntriesRequest) returns (AppendEntriesResponse){}
     * </pre>
     */
    public void appendEntries(proto.Raft.AppendEntriesRequest request,
        io.grpc.stub.StreamObserver<proto.Raft.AppendEntriesResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getAppendEntriesMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getRequestVotesMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                proto.Raft.VoteRequest,
                proto.Raft.VoteResponse>(
                  this, METHODID_REQUEST_VOTES)))
          .addMethod(
            getAppendEntriesMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                proto.Raft.AppendEntriesRequest,
                proto.Raft.AppendEntriesResponse>(
                  this, METHODID_APPEND_ENTRIES)))
          .build();
    }
  }

  /**
   */
  public static final class RaftServiceStub extends io.grpc.stub.AbstractStub<RaftServiceStub> {
    private RaftServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private RaftServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RaftServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new RaftServiceStub(channel, callOptions);
    }

    /**
     * <pre>
     *rpc SayHello(HelloRequest) returns (HelloReply){}
     *rpc RequestVotes(stream VoteRequest) returns (VoteResponse){}
     * </pre>
     */
    public void requestVotes(proto.Raft.VoteRequest request,
        io.grpc.stub.StreamObserver<proto.Raft.VoteResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getRequestVotesMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     *&#47;rpc AppendEntries(stream AppendEntriesRequest) returns (AppendEntriesResponse){}
     * </pre>
     */
    public void appendEntries(proto.Raft.AppendEntriesRequest request,
        io.grpc.stub.StreamObserver<proto.Raft.AppendEntriesResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getAppendEntriesMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class RaftServiceBlockingStub extends io.grpc.stub.AbstractStub<RaftServiceBlockingStub> {
    private RaftServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private RaftServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RaftServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new RaftServiceBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     *rpc SayHello(HelloRequest) returns (HelloReply){}
     *rpc RequestVotes(stream VoteRequest) returns (VoteResponse){}
     * </pre>
     */
    public proto.Raft.VoteResponse requestVotes(proto.Raft.VoteRequest request) {
      return blockingUnaryCall(
          getChannel(), getRequestVotesMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     *&#47;rpc AppendEntries(stream AppendEntriesRequest) returns (AppendEntriesResponse){}
     * </pre>
     */
    public proto.Raft.AppendEntriesResponse appendEntries(proto.Raft.AppendEntriesRequest request) {
      return blockingUnaryCall(
          getChannel(), getAppendEntriesMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class RaftServiceFutureStub extends io.grpc.stub.AbstractStub<RaftServiceFutureStub> {
    private RaftServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private RaftServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RaftServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new RaftServiceFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     *rpc SayHello(HelloRequest) returns (HelloReply){}
     *rpc RequestVotes(stream VoteRequest) returns (VoteResponse){}
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<proto.Raft.VoteResponse> requestVotes(
        proto.Raft.VoteRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getRequestVotesMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     *&#47;rpc AppendEntries(stream AppendEntriesRequest) returns (AppendEntriesResponse){}
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<proto.Raft.AppendEntriesResponse> appendEntries(
        proto.Raft.AppendEntriesRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getAppendEntriesMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_REQUEST_VOTES = 0;
  private static final int METHODID_APPEND_ENTRIES = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final RaftServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(RaftServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_REQUEST_VOTES:
          serviceImpl.requestVotes((proto.Raft.VoteRequest) request,
              (io.grpc.stub.StreamObserver<proto.Raft.VoteResponse>) responseObserver);
          break;
        case METHODID_APPEND_ENTRIES:
          serviceImpl.appendEntries((proto.Raft.AppendEntriesRequest) request,
              (io.grpc.stub.StreamObserver<proto.Raft.AppendEntriesResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class RaftServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    RaftServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return proto.Raft.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("RaftService");
    }
  }

  private static final class RaftServiceFileDescriptorSupplier
      extends RaftServiceBaseDescriptorSupplier {
    RaftServiceFileDescriptorSupplier() {}
  }

  private static final class RaftServiceMethodDescriptorSupplier
      extends RaftServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    RaftServiceMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (RaftServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new RaftServiceFileDescriptorSupplier())
              .addMethod(getRequestVotesMethod())
              .addMethod(getAppendEntriesMethod())
              .build();
        }
      }
    }
    return result;
  }
}
