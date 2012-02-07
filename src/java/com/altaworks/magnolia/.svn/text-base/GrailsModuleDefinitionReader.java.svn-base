package com.altaworks.magnolia;

import info.magnolia.module.ModuleManagementException;
import info.magnolia.module.model.ModuleDefinition;
import info.magnolia.module.model.reader.BetwixtModuleDefinitionReader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.*;

public class GrailsModuleDefinitionReader extends BetwixtModuleDefinitionReader{
    @Override
    protected String[] findModuleDescriptors() {

        try {
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources("classpath*:META-INF/magnolia/*.xml");

            String[] strings = new String[resources.length];
            for (int i = 0; i < resources.length; i++) {
                Resource resource = resources[i];
                String file = resource.getURL().getPath();
                file = file.substring(file.indexOf('!') + 1);
                strings[i] = file;
            }
            return strings;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ModuleDefinition readFromResource(String resourcePath) throws ModuleManagementException {

        File testFile = new File(resourcePath);
        if (testFile.exists()) {
            Reader r = null;
            try {
                r = new FileReader(testFile);
                return read(r);
            } catch (FileNotFoundException e) {
            } finally {
                if(r != null) {
                    try {
                        r.close();
                    } catch (IOException e) {
                    }
                }
            }
        }

        final InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream(resourcePath));
        try {
            return read(reader);
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
            }
        }
    }

}
