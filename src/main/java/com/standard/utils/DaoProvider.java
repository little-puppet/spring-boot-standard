package com.standard.utils;

import org.apache.ibatis.jdbc.SQL;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class DaoProvider {

    /**
     * 生成insert的sql
     * @param o
     * @return
     */
    public String insert(Object o) {
        Class<?> clazz = o.getClass();
        String tableName = getTableName(clazz);
        SQL sql = new SQL();
        try {
            HashMap<String, String> fieldColumMap = getFieldColumMap(o);
            sql.INSERT_INTO(tableName);
            Set<Map.Entry<String, String>> entries = fieldColumMap.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                sql.VALUES(entry.getKey(), entry.getValue());
            }
            return sql.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 生成select的sql
     * @param o
     * @return
     */
    public String select(Object o) {
        Class<?> clazz = o.getClass();
        String tableName = getTableName(clazz);
        SQL sql = new SQL();
        try {
            HashMap<String, String> fieldColumMap = getFieldColumMap(o);
            sql.SELECT("*");
            sql.FROM(tableName);
            Set<Map.Entry<String, String>> entries = fieldColumMap.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                sql.WHERE(entry.getKey() + "=" + entry.getValue());
            }
            return sql.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 生成select的sql
     * @param o
     * @return
     */
    public String update(Object o) {
        Class<?> clazz = o.getClass();
        String tableName = getTableName(clazz);
        SQL sql = new SQL();
        try {
            HashMap<String, String> fieldColumMap = getFieldColumMap(o);
            HashMap<String, String> keyColumMap = getKeyColumMap(o);
            for (String s : keyColumMap.keySet()) {
                fieldColumMap.remove(s);
            }
            sql.UPDATE(tableName);
            Set<Map.Entry<String, String>> entries = fieldColumMap.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                String value = entry.getValue();
                sql.SET(entry.getKey() + "=" + entry.getValue());
            }

            Set<Map.Entry<String, String>> entries1 = keyColumMap.entrySet();
            for (Map.Entry<String, String> stringStringEntry : entries1) {
                sql.WHERE(stringStringEntry.getKey() + "=" + stringStringEntry.getValue());
            }

            return sql.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取表明有注解根据注解获取 没有注解默认类名转驼峰
     *
     * @param clazz 字节码类
     * @return 表名
     */
    private String getTableName(Class<?> clazz) {
        Table table = clazz.getAnnotation(Table.class);
        String tableName = null;
        if (table == null) {
            String name = clazz.getName();
            String[] split = name.split("\\.");
            tableName = split[split.length - 1];
            if(tableName.endsWith("VO")){
                tableName = tableName.substring(0,tableName.length()-2);
            }
            if(tableName.endsWith("PO")){
                tableName = tableName.substring(0,tableName.length()-2);
            }
            if(tableName.endsWith("Entity")){
                tableName = tableName.substring(0,tableName.length()-6);
            }
            tableName = StringUtil.underscoreName(tableName);
        } else {
            tableName = table.value();
        }
        return tableName;
    }

    /**
     * 获取非空的列和值
     * 有注解根据注解明获取  没有注解根据默认字段名转驼峰
     *
     * @param o 对象
     * @return HashMap
     */
    private HashMap<String, String> getFieldColumMap(Object o) throws IllegalAccessException, DaoProviderException, NoSuchMethodException, InvocationTargetException {
        Class<?> clazz = o.getClass();
        HashMap<String, String> columnMap = new HashMap<>();

        // 获取所有成员变量
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {

            Object value = getValue(o, field.getName());
            if (value == null){
                continue;
            }

            // 添加字段和数据库字段的对照Map
            addColumnNameMap(columnMap, field);
        }

        return columnMap;
    }

    /**
     * 获取主键
     * 有注解根据注解明获取  没有注解根据默认字段名转驼峰
     *
     * @param o 对象
     * @return HashMap
     */
    private HashMap<String, String> getKeyColumMap(Object o) throws IllegalAccessException, DaoProviderException, NoSuchMethodException, InvocationTargetException {
        Class<?> clazz = o.getClass();
        HashMap<String, String> keyMap = new HashMap<>();

        // 获取所有成员变量
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            Key key = field.getAnnotation(Key.class);
            if (key == null){
                continue;
            }

            Object value = getValue(o, field.getName());
            if (value == null){
                throw new DaoProviderException("message:主键字段不允许为空");
            }

            // 添加字段和数据库字段的对照Map
            addColumnNameMap(keyMap, field);
        }

        if (keyMap.isEmpty()){
            throw new DaoProviderException("message:没有设置主键");
        }
        return keyMap;
    }


    /**
     * 添加映射信息到Map
     * @param columnMap 列名和字段名的对应
     * @param field 字段
     */
    private void addColumnNameMap(HashMap<String, String> columnMap, Field field) {
        // 获取字段值
        Column aColumn = field.getAnnotation(Column.class);
        String columnName = null;
        if (aColumn == null) {
            columnName = StringUtil.underscoreName(field.getName());
        } else {
            columnName = aColumn.value();
        }
        String TEMPLETE = "#{%s}";
        columnMap.put(columnName, String.format(TEMPLETE, field.getName()));
    }



    public static void main(String[] args) {
        DaoProvider daoProvider = new DaoProvider();
        String insert = daoProvider.update(daoProvider);
        System.out.println(insert);
    }

    //首字母大写
    private Object getValue(Object o,String name) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, DaoProviderException {
        if (Objects.equals(name, "serialVersionUID")){
            return null;
        }
        char[] cs=name.toCharArray();
        cs[0]-=32;
        String methodName = "get" + String.valueOf(cs);
        Method method = o.getClass().getMethod(methodName);
        Object invoke = method.invoke(o);
        return  invoke;
    }

    /**
     * 注解类用于获取表名
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface Table {
        String value();
    }

    /**
     * 注解类用于获取列名
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Column {
        String value();
    }

    /**
     * 注解类用于标志主键 用于更新和删除
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Key {
    }

    /**
     * 注解类用于标志主键 用于更新和删除
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Transient {
    }

    public class DaoProviderException extends Exception{
        public DaoProviderException(String message){
            super(message);
        }
        public DaoProviderException(){}
    }

}




