package org.example;

import com.example.grpc.LicensePlateServiceGrpc;
import com.example.grpc.LicenseService;
import io.grpc.stub.StreamObserver;
import java.io.*;
import java.sql.*;
import java.util.*;
import org.json.simple.*;
import org.json.simple.parser.*;

import javax.persistence.criteria.Root;

public class LicenseServiceImpl extends LicensePlateServiceGrpc.LicensePlateServiceImplBase {

    @Override
    public void greeting(LicenseService.LicensePlateRequest request,

                         StreamObserver<LicenseService.LicensePlateResponse> responseObserver) {
        BarrierServiceImpl barrierServiceimpl = new BarrierServiceImpl();
        BarrierOpenServiceImpl barrierOpenServiceimpl = new BarrierOpenServiceImpl();
        System.out.println(request);
        LicenseService.LicensePlateResponse response = LicenseService.
                LicensePlateResponse.newBuilder().setPlateResponse("Your plate number is " + request.getPlateRequest()) // вывод полученного номера
                .build();
        System.out.println();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
        boolean IsThereAnyFreePlaces = false;
        try {
            boolean AdminOpenGateCheck = barrierOpenServiceimpl.ReturnTheValue();
            if (AdminOpenGateCheck) {
                System.out.println("The gate is already opened by admin");
            } else{
                boolean AdminGateCheck = barrierServiceimpl.ReturnTheValue();
            if (!AdminGateCheck) {
                System.out.println("The gates function normally");
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost/users", "root", "passw0rd"); // нужно для соединения с БД
                Statement stmt = con.createStatement();
                System.out.println();
                String SQL_Places = "SELECT * FROM empty_places WHERE idempty_places=1"; // нужно для проверки кол-ва свободных мест
                int free_places;
                ResultSet rs_decider = stmt.executeQuery(SQL_Places);
                if (rs_decider.next()) {
                    free_places = rs_decider.getInt(2);  // выбор столбца с количеством мест
                    if (free_places > 0) {
                        IsThereAnyFreePlaces = true;
                    } else {
                        System.out.println("All places are taken, the gate is closed");
                        return;
                    }
                }
                rs_decider.close();
                if (IsThereAnyFreePlaces) {
                    String SQL = "SELECT * FROM car_table WHERE plateref='" + request.getPlateRequest() + "'"; // нужно для проверки есть ли полученный номер в БД
                    ResultSet rs = stmt.executeQuery(SQL);
                    int id_cartable_variable;
                    int id_user_variable;
                    String SQL2 = "UPDATE empty_places SET places= places + 1 WHERE idempty_places=2"; // обе строчки нужны для обновления
                    String SQL3 = "UPDATE empty_places SET places= places - 1 WHERE idempty_places=1"; // количества свободных и занятых мест
                    if (rs.next()) { //выбор строки, где находится нужны номер
                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        JSONParser parser = new JSONParser();
                        FileReader reader = new FileReader("C:\\auto_model\\results.json");
                        try {
                            //путь где надо считать
                            Object obj = parser.parse(reader); //считываем
                            JSONArray jsonArray = (JSONArray) obj;
                            JSONObject address = (JSONObject) jsonArray.get(0);
                            String car_model = (String) address.get("label");
                            // назначаем переменной значение "Label" в котором хранится модель машины
                            String car_model_from_DB = rs.getNString(5); // назначаем переменной значение из бд
                            System.out.println("car model from db: " + car_model_from_DB);
                            System.out.println("car model: " + car_model); //вывод для теста
                            if (car_model_from_DB.equals(car_model)) { // сравниваем значение из бд с теми, что прочитал в json
                                id_cartable_variable = rs.getInt(1); // нужно для сохранения id машины
                                id_user_variable = rs.getInt(3); // нужно для сохранения владельца машины
                                String SQL4 = "INSERT INTO journal (action_type, timestamp, idcar_table, id_user, plate) VALUES ('Entered', current_timestamp(), '" + id_cartable_variable + "','" + id_user_variable + "', '" + request.getPlateRequest() + "')";
                                System.out.println("The gate is opened");
                                int res = stmt.executeUpdate(SQL2);
                                System.out.println("Taken places has changed");
                                int res2 = stmt.executeUpdate(SQL3);
                                System.out.println("Free places has changed");
                                int res3 = stmt.executeUpdate(SQL4); // производится логгирование*/
                                rs.close();
                            } else {
                                System.out.println("The model of the car was not recognized");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println("There's no such number, the gate is closed");
                    }
                } else {
                    System.out.println("All places are taken, the gate is closed 2");
                }


            } else {
                System.out.println("The gate is closed by admin");
            }
        } } catch (Exception e) {
            System.out.println("ERROR:" + e.getMessage());
        }

    }
}
