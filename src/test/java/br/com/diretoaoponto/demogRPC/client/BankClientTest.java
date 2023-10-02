package br.com.diretoaoponto.demogRPC.client;

import br.com.diretoaoponto.demogRPC.models.*;
import com.google.common.util.concurrent.Uninterruptibles;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BankClientTest {
    private BankServiceGrpc.BankServiceBlockingStub bankServiceBlockingStub;
    private BankServiceGrpc.BankServiceStub bankServiceStub;

    @BeforeAll
    public void setup() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 6565)
                .usePlaintext()
                .build();

        bankServiceBlockingStub = BankServiceGrpc.newBlockingStub(channel);
        bankServiceStub = BankServiceGrpc.newStub(channel);
    }

    @Test
    public void balanceTest() {
        BalanceCheckRequest balanceCheckRequest = BalanceCheckRequest.newBuilder()
                .setAccountNumber(4)
                .build();

        Balance balance = bankServiceBlockingStub.getBalance(balanceCheckRequest);

        System.out.println("Received: " + balance.getAmount());
    }

    @Test
    public void withdrawTest() {
        WithdrawRequest withdrawRequest = WithdrawRequest.newBuilder()
                .setAccountNumber(5)
                .setAmount(40)
                .build();

        //Necessary to use forEachRemaining() because the server is sending a stream of messages
        bankServiceBlockingStub.withdraw(withdrawRequest)
                .forEachRemaining(money -> System.out.println("Received: " + money.getValue()));
    }

    @Test
    public void withdrawAsyncTest() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        WithdrawRequest withdrawRequest = WithdrawRequest.newBuilder()
                .setAccountNumber(5)
                .setAmount(40)
                .build();

        bankServiceStub.withdraw(withdrawRequest, new MoneyStreamingResponse(latch));
        latch.await();
    }

    @Test
    public void cashDepositTest() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        StreamObserver<DepositRequest> streamObserver = this.bankServiceStub.cashDeposit(new BalanceStreamObserver(latch));

        for (int i=0; i<10; i++){
            DepositRequest depositRequest = DepositRequest.newBuilder()
                    .setAccountNumber(7)
                    .setAmount(20).build();
            streamObserver.onNext(depositRequest);
        }

        streamObserver.onCompleted();
        latch.await();
    }
}
