package com.xmajer.importapp.importer;

import com.xmajer.importapp.importer.cli.ImportOptions;
import com.xmajer.importapp.importer.model.source.ParsedAddressImport;
import com.xmajer.importapp.importer.parser.AddressXmlParser;
import com.xmajer.importapp.importer.service.ImportPersistenceService;
import com.xmajer.importapp.persistence.repository.MunicipalityExtendedRepository;
import com.xmajer.importapp.persistence.repository.MunicipalityPartRepository;
import com.xmajer.importapp.persistence.repository.MunicipalityRepository;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers(disabledWithoutDocker = true)
@SpringBootTest(
        classes = ImporterApplication.class,
        properties = "import.runner.enabled=false"
)
class ImportDefaultPathIntegrationTest {

    @Container
    @ServiceConnection
    private static final PostgreSQLContainer POSTGRES =
            new PostgreSQLContainer("postgres:16-alpine");

    @Autowired
    private AddressXmlParser parser;

    @Autowired
    private Validator validator;

    @Autowired
    private ImportPersistenceService persistenceService;

    @Autowired
    private MunicipalityRepository municipalityRepository;

    @Autowired
    private MunicipalityPartRepository municipalityPartRepository;

    @Autowired
    private MunicipalityExtendedRepository municipalityExtendedRepository;

    @Test
    void importsNormalSourceFile() throws Exception {
        ParsedAddressImport data = parser.parse(
                resource("xml/ruian-valid.xml"),
                new ImportOptions(false)
        );

        assertThat(validator.validate(data)).isEmpty();

        var summary = persistenceService.save(data);

        assertThat(summary.municipalitiesCreated()).isOne();
        assertThat(summary.municipalityPartsCreated()).isOne();

        assertThat(municipalityRepository.findById("573060"))
                .hasValueSatisfying(municipality ->
                        assertThat(municipality.getName())
                                .isEqualTo("Kopidlno")
                );
        assertThat(municipalityPartRepository.findAllWithMunicipality())
                .singleElement()
                .satisfies(part -> {
                    assertThat(part.getName()).isEqualTo("Kopidlno");
                    assertThat(part.getMunicipality().getCode())
                            .isEqualTo("573060");
                });
        assertThat(municipalityExtendedRepository.findAll()).isEmpty();
    }

    private InputStream resource(String path) {
        InputStream input = getClass().getClassLoader()
                .getResourceAsStream(path);

        assertThat(input)
                .as("test resource %s", path)
                .isNotNull();

        return input;
    }
}
