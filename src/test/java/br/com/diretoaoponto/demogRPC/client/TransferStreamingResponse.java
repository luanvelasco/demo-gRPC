package br.com.diretoaoponto.demogRPC.client;

import br.com.diretoaoponto.demogRPC.models.TransferResponse;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;

public class TransferStreamingResponse implements StreamObserver<TransferResponse> {

    private CountDownLatch latch;

    public TransferStreamingResponse(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void onNext(TransferResponse transferResponse) {
        System.out.println("Transfer status: " + transferResponse.getStatus());
        transferResponse.getAccountsList()
                .stream()
                .map(account -> account.getAccountNumber() + " : " + account.getAmount())
                .forEach(System.out::println);

        System.out.println("--------------------------------------------------");
    }

    @Override
    public void onError(Throwable throwable) {
        this.latch.countDown();
    }

    @Override
    public void onCompleted() {
        System.out.println("All transfers done!");
        this.latch.countDown();
    }
}
