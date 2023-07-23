package org.example;

import com.example.grpc.BarrierOpenServiceGrpc;
import com.example.grpc.LicenseService;
import io.grpc.stub.StreamObserver;

public class BarrierOpenServiceImpl extends BarrierOpenServiceGrpc.BarrierOpenServiceImplBase {
    public static boolean ManagedByAdmin = false;
    @Override
    public void greeting3(LicenseService.BarrierOpenRequest barrierOpenRequest,
                          StreamObserver<LicenseService.BarrierOpenResponse> responseObserver3){

        ManagedByAdmin = barrierOpenRequest.getBarrierOpenRequest();
        //System.out.println("Sent value is: " + barrierOpenRequest.getBarrierOpenRequest());
        System.out.println();
        if(ManagedByAdmin ){
            System.out.println("The gate is opened by admin");
        }
        LicenseService.BarrierOpenResponse response = LicenseService
                .BarrierOpenResponse.newBuilder().setBarrierOpenResponse(ManagedByAdmin)
                .build();
        responseObserver3.onNext(response);
        responseObserver3.onCompleted();
    }
    public boolean ReturnTheValue(){
        return ManagedByAdmin;
    }
}
