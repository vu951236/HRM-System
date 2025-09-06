package com.example.hrm.repository;

import com.example.hrm.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeviceRepository extends JpaRepository<Device, Long> {
    Device findByIpAddressAndPort(String ip, Integer port);

    Optional<Device> findByDeviceName(String deviceName);

}
