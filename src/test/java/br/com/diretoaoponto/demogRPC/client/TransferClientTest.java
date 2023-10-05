package br.com.diretoaoponto.demogRPC.client;

import br.com.diretoaoponto.demogRPC.models.BankServiceGrpc;
import br.com.diretoaoponto.demogRPC.models.TransferRequest;
import br.com.diretoaoponto.demogRPC.models.TransferServiceGrpc;
import br.com.diretoaoponto.demogRPC.models.TransferStatus;
import br.com.diretoaoponto.demogRPC.server.TransferService;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TransferClientTest {
    private TransferServiceGrpc.TransferServiceStub transferServiceStub;

    @BeforeAll
    public void setup() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 6565)
                .usePlaintext()
                .build();

        transferServiceStub = TransferServiceGrpc.newStub(channel);
    }

    @Test
    public void transferTest() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        TransferStreamingResponse transferStreamingResponse = new TransferStreamingResponse(latch);

        StreamObserver< TransferRequest> requestStreamObserver = this.transferServiceStub.transfer(transferStreamingResponse);

        for (int i=0; i < 100; i++){
            TransferRequest request = TransferRequest.newBuilder()
                    .setFromAccount(ThreadLocalRandom.current().nextInt(1, 10))
                    .setToAccount(ThreadLocalRandom.current().nextInt(1, 10))
                    .setAmount(ThreadLocalRandom.current().nextInt(1, 21))
                    .build();

            requestStreamObserver.onNext(request);
        }

        requestStreamObserver.onCompleted();
        latch.await();
    }
}
