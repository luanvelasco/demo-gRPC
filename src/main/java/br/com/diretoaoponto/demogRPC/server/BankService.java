package br.com.diretoaoponto.demogRPC.server;

import br.com.diretoaoponto.demogRPC.models.Balance;
import br.com.diretoaoponto.demogRPC.models.BalanceCheckRequest;
import br.com.diretoaoponto.demogRPC.models.BankServiceGrpc;
import io.grpc.stub.StreamObserver;

public class BankService extends BankServiceGrpc.BankServiceImplBase {

    /**
    * There are 2 channels of communication:
    * 1. Method onNext() - used to send a message from the server to the client
    * 2. Method onCompleted() - used to tell the client that the server has completed processing
    * 3. Method onError() - used to tell the client that the server has encountered an error
    **/
    @Override
    public void getBalance(BalanceCheckRequest request, StreamObserver<Balance> responseObserver) {
        System.out.println("Received balance check request for " + request.getAccountNumber());

        int accountNumber = request.getAccountNumber();

        Balance balance = Balance.newBuilder()
                .setAmount(AccountDataBase.getBalance(accountNumber))
                .build();

        responseObserver.onNext(balance);

        responseObserver.onCompleted();
    }
}