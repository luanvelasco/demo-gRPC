package br.com.diretoaoponto.demogRPC.server;

import br.com.diretoaoponto.demogRPC.models.Account;
import br.com.diretoaoponto.demogRPC.models.TransferRequest;
import br.com.diretoaoponto.demogRPC.models.TransferResponse;
import br.com.diretoaoponto.demogRPC.models.TransferStatus;
import io.grpc.stub.StreamObserver;

public class TransferStreamingRequest implements StreamObserver<TransferRequest> {

    private StreamObserver<TransferResponse> transferResponseStreamObserver;

    public TransferStreamingRequest(StreamObserver<TransferResponse> transferResponseStreamObserver) {
        this.transferResponseStreamObserver = transferResponseStreamObserver;
    }

    @Override
    public void onNext(TransferRequest transferRequest) {
        int fromAccount = transferRequest.getFromAccount();
        int toAccount = transferRequest.getToAccount();
        int amount = transferRequest.getAmount();
        int balance = AccountDataBase.getBalance(fromAccount);

        TransferStatus transferStatus = TransferStatus.FAILURE;
        if (balance >= amount && fromAccount != toAccount) {
            AccountDataBase.deductBalance(fromAccount, amount);
            AccountDataBase.addBalance(toAccount, amount);
            transferStatus = TransferStatus.SUCCESS;
        }

        Account fromAccountInfo = Account.newBuilder().setAccountNumber(fromAccount).setAmount(AccountDataBase.getBalance(fromAccount)).build();
        Account toAccountInfo = Account.newBuilder().setAccountNumber(toAccount).setAmount(AccountDataBase.getBalance(toAccount)).build();

        TransferResponse transferResponse = TransferResponse.newBuilder()
                .setStatus(transferStatus)
                .addAccounts(fromAccountInfo)
                .addAccounts(toAccountInfo)
                .build();

        this.transferResponseStreamObserver.onNext(transferResponse);

    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onCompleted() {
        AccountDataBase.printAccountDetails();
        this.transferResponseStreamObserver.onCompleted();
    }
}
