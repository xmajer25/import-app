package com.xmajer.importapp.persistence.config;

import com.xmajer.importapp.persistence.entity.Municipality;
import com.xmajer.importapp.persistence.repository.MunicipalityRepository;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration(proxyBeanMethods = false)
@EnableJpaAuditing
@EntityScan(basePackageClasses = Municipality.class)
@EnableJpaRepositories(basePackageClasses = MunicipalityRepository.class)
public class PersistenceConfig {
}