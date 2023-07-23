package org.example;

import com.example.grpc.BarrierServiceGrpc;
import com.example.grpc.LicensePlateServiceGrpc;
import com.example.grpc.LicenseService;
import io.grpc.stub.StreamObserver;


public class BarrierServiceImpl extends BarrierServiceGrpc.BarrierServiceImplBase {
    public static boolean isitopen = false;
    @Override
    public void greeting2(LicenseService.BarrierRequest barrierRequest,
                          StreamObserver<LicenseService.BarrierResponse> responseObserver2){

        isitopen = barrierRequest.getBarrierRequest();
        //System.out.println("Sent value is: " + barrierRequest.getBarrierRequest());
        System.out.println();
        LicenseService.BarrierResponse response = LicenseService
                .BarrierResponse.newBuilder().setBarrierResponse(isitopen)
                .build();

        responseObserver2.onNext(response);
        responseObserver2.onCompleted();
    }
    public boolean ReturnTheValue(){
        return isitopen;
    }

}
