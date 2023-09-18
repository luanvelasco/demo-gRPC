package br.com.diretoaoponto.demogRPC.client;

import br.com.diretoaoponto.demogRPC.models.Balance;
import br.com.diretoaoponto.demogRPC.models.BalanceCheckRequest;
import br.com.diretoaoponto.demogRPC.models.BankServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BankClient {
    private BankServiceGrpc.BankServiceBlockingStub bankServiceBlockingStub;

    @BeforeAll
    public void setup() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 6565)
                .usePlaintext()
                .build();

        bankServiceBlockingStub = BankServiceGrpc.newBlockingStub(channel);
    }

    @Test
    public void balanceTest() {
        BalanceCheckRequest balanceCheckRequest = BalanceCheckRequest.newBuilder()
                .setAccountNumber(4)
                .build();

        Balance balance = bankServiceBlockingStub.getBalance(balanceCheckRequest);

        System.out.println("Received: " + balance.getAmount());
    }
}
