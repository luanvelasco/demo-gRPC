package br.com.diretoaoponto.demogRPC.server;

import br.com.diretoaoponto.demogRPC.models.*;
import io.grpc.Status;
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

    @Override
    public void withdraw(WithdrawRequest request, StreamObserver<Money> responseObserver) {
        int accountNumber = request.getAccountNumber();
        int amount = request.getAmount();
        int balance = AccountDataBase.getBalance(accountNumber);

        if(balance < amount){
            Status status = Status.FAILED_PRECONDITION.withDescription("No enough money. You have only " + balance);
            responseObserver.onError(status.asRuntimeException());
            return;
        }

        for (int i = 0; i < (amount/10); i++) {
            Money money = Money.newBuilder()
                    .setValue(10)
                    .build();

            responseObserver.onNext(money);
            AccountDataBase.deductBalance(accountNumber, 10);
        }

        responseObserver.onCompleted();
    }
}