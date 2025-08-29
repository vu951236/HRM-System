package com.example.hrm.device;

import com.example.hrm.dto.request.AttendanceLogRequest;
import com.example.hrm.dto.response.AttendanceLogResponse;
import com.example.hrm.service.AttendanceLogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

@Component
@RequiredArgsConstructor
public class DeviceSocketListener implements CommandLineRunner {

    private final AttendanceLogService attendanceLogService;

    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @Override
    public void run(String... args) throws Exception {
        int port = 9999;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("DeviceSocketListener đang chạy trên port " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(() -> handleClient(socket)).start();
            }
        }
    }

    private void handleClient(Socket socket) {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println("Nhận dữ liệu từ thiết bị: " + line);

                // Kiểm tra dữ liệu có phải JSON không
                if (!line.trim().startsWith("{")) {
                    System.out.println("Bỏ qua dữ liệu không phải JSON: " + line);
                    continue;
                }

                try {
                    AttendanceLogRequest request = objectMapper.readValue(line, AttendanceLogRequest.class);

                    AttendanceLogResponse response;
                    if (request.getCheckInTime() != null) {
                        response = attendanceLogService.checkIn(request);
                    } else {
                        response = attendanceLogService.checkOut(request);
                    }

                    String respJson = objectMapper.writeValueAsString(response);
                    out.println(respJson);
                    System.out.println("Gửi phản hồi: " + respJson);

                } catch (Exception e) {
                    System.err.println("Lỗi parse JSON: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi xử lý client: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
