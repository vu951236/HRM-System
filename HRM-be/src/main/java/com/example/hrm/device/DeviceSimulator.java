package com.example.hrm.device;

import com.example.hrm.dto.request.AttendanceLogRequest;
import com.example.hrm.entity.AttendanceLog;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;

public class DeviceSimulator {

    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    public static void main(String[] args) {
        String host = "localhost";
        int port = 9999;

        try (Socket socket = new Socket(host, port);
             PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            AttendanceLogRequest checkIn = new AttendanceLogRequest();
            checkIn.setEmployeeId(19);
            checkIn.setWorkScheduleId(442);
            checkIn.setDeviceId("2");
            checkIn.setCheckInTime(LocalDateTime.now());
            checkIn.setMethod(AttendanceLog.AttendanceMethod.FINGERPRINT);

            String checkInJson = objectMapper.writeValueAsString(checkIn);
            out.println(checkInJson);
            System.out.println("Gửi check-in: " + checkInJson);

            String resp1 = in.readLine();
            System.out.println("Phản hồi từ server: " + resp1);

            Thread.sleep(5000);

            AttendanceLogRequest checkOut = new AttendanceLogRequest();
            checkOut.setEmployeeId(19);
            checkOut.setWorkScheduleId(442);
            checkOut.setDeviceId("2");
            checkOut.setCheckOutTime(LocalDateTime.now());
            checkOut.setMethod(AttendanceLog.AttendanceMethod.FINGERPRINT);

            String checkOutJson = objectMapper.writeValueAsString(checkOut);
            out.println(checkOutJson);
            System.out.println("Gửi check-out: " + checkOutJson);

            String resp2 = in.readLine();
            System.out.println("Phản hồi từ server: " + resp2);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
