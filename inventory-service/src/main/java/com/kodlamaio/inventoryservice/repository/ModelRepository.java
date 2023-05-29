package com.kodlamaio.inventoryservice.repository;

import com.kodlamaio.commonpackage.utils.constants.Paths;
import com.kodlamaio.inventoryservice.entities.Model;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {Paths.ConfigurationBasePackage, Paths.Inventory.ServiceBasePackage})
public interface ModelRepository extends JpaRepository<Model, UUID> {
}
