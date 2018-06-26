package com.robyrodriguez.stackbuster.processor;

import com.robyrodriguez.stackbuster.annotation.StackBusterData;
import com.robyrodriguez.stackbuster.annotation.StackBusterData.StructureType;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;

/**
 * Compile-time project specific annotations processor:
 *
 * - writes database structure type to configuration file according to @StackBusterData
 */
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class StackBusterMavenAnnotationsProcessor extends AbstractProcessor {

    private static final String FIREBASE_DATABASE_TYPE = "firebase.database.type";

    @Override
    public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment env) {
        for (TypeElement typeElement : annotations) {
            for (Element element : env.getElementsAnnotatedWith(typeElement)) {
                StackBusterData annotation = element.getAnnotation(StackBusterData.class);
                StructureType structureType = annotation.structureType();

                generateProperties(structureType);
                generateFunctions(structureType);
                generateRules(structureType);
                generateClient(structureType);
            }
        }
        return false;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return new HashSet<>(Arrays.asList(StackBusterData.class.getName()));
    }

    /**
     * Write-out of following properties to config.properties resource file:
     *
     * - database-type: used by Spring to bootstrap database paths resolution component
     *
     * @param structureType database structure type
     */
    private void generateProperties(StructureType structureType) {
        processingEnv.getMessager().printMessage(Kind.NOTE,
            "@StackBusterData: running generateProperties with structure type: " + structureType);
        try {
            URL url = getClass().getResource("/config.properties");
            PropertiesConfiguration config = new PropertiesConfiguration(url);
            config.setProperty(FIREBASE_DATABASE_TYPE, structureType);
            config.save();
        } catch (Exception e) {
            processingEnv.getMessager().printMessage(Kind.ERROR,
                "@StackBusterData: could not find/load 'config.properties' resource file");
        }
    }

    /**
     * Generates `index.js` in functions project according to database structure type
     *
     * @param structureType database structure type
     */
    private void generateFunctions(StructureType structureType) {
        processingEnv.getMessager().printMessage(Kind.NOTE,"@StackBusterData: running generateFunctions");

        processingEnv.getMessager().printMessage(Kind.NOTE,Paths.get("../functions/structures/" + structureType
                .name().toLowerCase() + "/index.js").toString());
        try {
            Files.copy(
                Paths.get("../functions/structures/" + structureType.name().toLowerCase() + "/index.js"),
                Paths.get("../functions/functions/index.js"),
                StandardCopyOption.REPLACE_EXISTING
            );
        } catch (Exception e) {
            processingEnv.getMessager().printMessage(Kind.WARNING,
                "@StackBusterData: could not move 'index.js' for functions");
        }
    }

    /**
     * Generates `rules.json` in database-rules project according to database structure type
     *
     * @param structureType database structure type
     */
    private void generateRules(StructureType structureType) {
        processingEnv.getMessager().printMessage(Kind.NOTE,"@StackBusterData: running generateRules");

        try {
            Files.copy(
                Paths.get("../database-rules/structures/" + structureType.name().toLowerCase() + "/rules.json"),
                Paths.get("../database-rules/rules.json"),
                StandardCopyOption.REPLACE_EXISTING
            );
        } catch (Exception e) {
            processingEnv.getMessager().printMessage(Kind.WARNING,
                "@StackBusterData: could not move 'rules.json' for database rules");
        }
    }

    /**
     * Generates `sb-api.js` in client project according to database structure type
     *
     * @param structureType database structure type
     */
    private void generateClient(StructureType structureType) {
        processingEnv.getMessager().printMessage(Kind.NOTE,"@StackBusterData: running generateFunctions");

        try {
            Files.copy(
                Paths.get("../client/structures/" + structureType.name().toLowerCase() + "/sb-api.js"),
                Paths.get("../client/public/scripts/sb-api.js"),
                StandardCopyOption.REPLACE_EXISTING
            );
        } catch (Exception e) {
            processingEnv.getMessager().printMessage(Kind.WARNING,
                "@StackBusterData: could not move 'sb-api.js' for client");
        }
    }
}
