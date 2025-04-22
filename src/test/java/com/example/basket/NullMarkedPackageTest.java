package com.example.basket;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class NullMarkedPackageTest {

    private static final String BASE_PACKAGE = "com.example.basket.*";
    private static final String[] GENERATED_PACKAGES = {
            "com.example.basket.infrastructure.adapters.in.rest.api",
            "com.example.basket.infrastructure.adapters.in.rest.dto"
    };
    private static final String NULL_MARKED_ANNOTATION = "org.jspecify.annotations.NullMarked";

    @Test
    void allPackagesAreNullMarked() {
        try (ScanResult scanResult = new ClassGraph()
                .enableAnnotationInfo()
                .acceptPackages(BASE_PACKAGE)
                .rejectPackages(GENERATED_PACKAGES)
                .scan()) {

            var unmarkedPackages = scanResult.getAllClasses()
                    .stream()
                    .map(ClassInfo::getPackageInfo)
                    .distinct()
                    .filter(pkg -> !pkg.hasAnnotation(NULL_MARKED_ANNOTATION))
                    .toList();

            assertThat(unmarkedPackages).isEmpty();
        }
    }
}
