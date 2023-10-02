package br.com.diretoaoponto.demogRPC.server;

import br.com.diretoaoponto.demogRPC.models.Balance;
import br.com.diretoaoponto.demogRPC.models.DepositRequest;
import io.grpc.stub.StreamObserver;

public class CashStreamingRequest implements StreamObserver<DepositRequest> {

    private StreamObserver<Balance> balanceResponseObserver;
    private int accountBalance;

    public CashStreamingRequest(StreamObserver<Balance> balanceResponseObserver) {
        this.balanceResponseObserver = balanceResponseObserver;
    }

    @Override
    public void onNext(DepositRequest depositRequest) {
        int accountNumber = depositRequest.getAccountNumber();
        int amount = depositRequest.getAmount();

        this.accountBalance = AccountDataBase.addBalance(accountNumber, amount);
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onCompleted() {
        Balance balance = Balance.newBuilder()
                .setAmount(this.accountBalance)
                .build();

        this.balanceResponseObserver.onNext(balance);
        this.balanceResponseObserver.onCompleted();
    }
}
