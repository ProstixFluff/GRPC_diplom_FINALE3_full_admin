package org.example;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.apache.log4j.PropertyConfigurator;

import java.io.IOException;
import java.util.List;

public class GRPC_Server {
    public static void main(String[] args) throws IOException, InterruptedException {

    Server server = ServerBuilder.forPort(8989)
            .addService(new BarrierServiceImpl())
            .addService(new BarrierOpenServiceImpl())
            .addService(new LicenseServiceImpl())
            .build();

        server.start();

        System.out.println("Server started");

        server.awaitTermination();
    }
}
