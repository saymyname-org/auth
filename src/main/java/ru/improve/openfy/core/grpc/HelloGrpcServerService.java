package ru.improve.openfy.core.grpc;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import ru.improve.skufify.grpc.HelloTestGrpc;
import ru.improve.skufify.grpc.HelloTestProto;

@GrpcService
public class HelloGrpcServerService extends HelloTestGrpc.HelloTestImplBase {

    @Override
    public void sayHello(HelloTestProto.HelloRequest request,
                         StreamObserver<HelloTestProto.HelloReply> responseObserver) {

        System.out.println(request.getName());

        HelloTestProto.HelloReply helloReply = HelloTestProto.HelloReply.newBuilder()
                .setMessage("response from server " + request.getName())
                .build();

        responseObserver.onNext(helloReply);
        responseObserver.onCompleted();
    }
}