package com.intexsoft.analytics.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.UUID;

import com.intexsoft.analytics.TestDataProvider;
import com.intexsoft.analytics.repository.DepartmentRepository;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@AutoConfigureWebTestClient(timeout = "PT10S")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DepartmentControllerTests {

    private static final String URL_TEMPLATE = "/api/v1/departments/";

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private DepartmentRepository departmentRepository;

    @MockBean
    private ConnectionFactoryInitializer initializer;

    @Test
    void testRetrieveDepartmentById() throws IOException {
        var departmentId = UUID.fromString("22a1db07-80a6-4578-b2d8-1d4b7326cae0");
        var department = TestDataProvider.getDepartmentStub(departmentId);

        when(departmentRepository.findById(departmentId)).thenReturn(Mono.just(department));

        final var expectedJson = IOUtils
                .toString(Objects.requireNonNull(this.getClass()
                        .getClassLoader()
                        .getResourceAsStream("expected/retrieve-department-by-id.json")), StandardCharsets.UTF_8);

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(URL_TEMPLATE + departmentId)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody().json(expectedJson);
    }

    @Test
    void testRetrieveAllDepartments() throws IOException {
        var departmentId = UUID.fromString("22a1db07-80a6-4578-b2d8-1d4b7326cae0");
        var departmentId2 = UUID.fromString("83110a26-fa26-4cc2-8963-d86520a918f9");
        var department = TestDataProvider.getDepartmentStub(departmentId);
        var department2 = TestDataProvider.getDepartmentStub(departmentId2);

        when(departmentRepository.findAll()).thenReturn(Flux.just(department, department2));

        final var expectedJson = IOUtils
                .toString(Objects.requireNonNull(this.getClass()
                        .getClassLoader()
                        .getResourceAsStream("expected/retrieve-all-departments.json")), StandardCharsets.UTF_8);

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(URL_TEMPLATE)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody().json(expectedJson);
    }

}
