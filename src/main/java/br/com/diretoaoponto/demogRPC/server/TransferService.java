package br.com.diretoaoponto.demogRPC.server;

import br.com.diretoaoponto.demogRPC.models.TransferRequest;
import br.com.diretoaoponto.demogRPC.models.TransferResponse;
import br.com.diretoaoponto.demogRPC.models.TransferServiceGrpc;
import io.grpc.stub.StreamObserver;

public class TransferService extends TransferServiceGrpc.TransferServiceImplBase {
    @Override
    public StreamObserver<TransferRequest> transfer(StreamObserver<TransferResponse> responseObserver) {
        return new TransferStreamingRequest(responseObserver);
    }
}
