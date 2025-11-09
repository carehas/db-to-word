package org.example.utils;

import org.example.config.ExportConfig;
import org.example.exporter.DatabaseTableExporter;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Set;

/**
 * @BelongsProject: db-to-word
 * @BelongsPackage: org.example.utils
 * @CreateTime: 2025-11-09  13:30
 * @Description: 使用反射构建对象
 * @Version: 1.0
 */
public class ReflectConstruct {
    public static HashMap<String, DatabaseTableExporter> scanTableExporter(String packageName){

        HashMap<String, DatabaseTableExporter> exporterMap = new HashMap<>();
        try {
            //扫描指定路径的包
            Reflections reflections = new Reflections(packageName, Scanners.SubTypes, Scanners.TypesAnnotated);
            Set<Class<? extends DatabaseTableExporter>> classes = reflections.getSubTypesOf(DatabaseTableExporter.class);
            for (Class<? extends DatabaseTableExporter> clazz : classes){
                if(clazz.getAnnotation(ExportConfig.class) != null){
                    String databaseType = clazz.getAnnotation(ExportConfig.class).databaseType();
                    exporterMap.put(databaseType, clazz.getDeclaredConstructor().newInstance());
                }
            }
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        return exporterMap;
    }

}
