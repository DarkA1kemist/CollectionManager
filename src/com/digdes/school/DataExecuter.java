package com.digdes.school;



import java.sql.Array;
import java.util.*;
import java.util.regex.Pattern;


public class DataExecuter {
    private DataExecuter(){
        //Пустой конструктор
    }
    private static String[] stringValues;
    private static String[] longValues;
    private static String[] doubleValues;
    private static String[] booleanValues;
    static{
        stringValues = new String[]{"lastName"};
        longValues = new String[]{"age", "id"};
        doubleValues = new String[]{"cost"};
        booleanValues = new String[]{"active"};
    }
    public static Map<String, Object> getDataMap(String data) throws Exception{
        Map<String,String> result = new LinkedHashMap<>();
        result.put("id",null);
        result.put("lastName",null);
        result.put("age",null);
        result.put("cost",null);
        result.put("active",null);

        for(int i = 0; i < data.split(",").length; i++) {

            String keyValue = data.split(",")[i]; // Пара ключ значение

            //Разделение на значение и ключ
            String key = keyValue.split("=")[0];
            String value = keyValue.split("=")[1];

            //Удаление кавычек
            //value = value.replace("\"", "");
            //value = value.replace("\'", "");

            //Чистка лишних пробелов
            value = value.trim();
            key = key.trim();

            //Проверка формата введенного ключа
            if ((key.startsWith("\"") || key.startsWith("\'")) || (key.endsWith("\"") || key.endsWith("\'"))) {
                key = key.replace("\'", "");
                key = key.replace("\"", "");
                if(!value.equals("null"))result.put(key, value);

            }
            else{
                    throw new Exception("Key format is need to be '" + key + "'");
            }
        }
        for(Map.Entry<String, String> item : result.entrySet()){
            if(item.getValue()!=null){
                return convertData(result);
            }
        }
        throw new Exception("Empty request exception");

    }
    private static Map<String,Object> convertData(Map<String,String> data){
        Map<String, Object> result = new LinkedHashMap<>();
        try {
            for (Map.Entry<String, String> item : data.entrySet()) {
                if(item.getValue() != null) {
                    result.put(keyConverter(item.getKey()), valueConverter(item.getKey(), item.getValue()));
                    continue;
                }
                result.putIfAbsent(item.getKey(), "");
            }

        }
        catch (NumberFormatException ex){
            throw new NumberFormatException("Invalid Request Format");
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    public static boolean checkCondition(String condition, Map<String,Object> object) throws Exception {
        //condition = condition.toLowerCase();
        List<String> andConditions = new ArrayList<>();
        List<String> orConditions = new ArrayList<>();

        if(condition.split("and").length > 1){
            return checkCondition(condition.split("and")[0].trim(),object) & checkCondition(condition.split("and")[1].trim(),object);

        }
        else if(condition.split("or").length > 1){
            return checkCondition(condition.split("or")[0].trim(),object) | checkCondition(condition.split("or")[1].trim(),object);
        }
        else{
            try {
                return conditionConverter(object,condition);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }



    }
    private static Object valueConverter(String key, String value) throws Exception{
        try{
            if(value.equals("null")) return "";
            for(int i = 0 ; i < stringValues.length; i++){
                if(key.equalsIgnoreCase(stringValues[i])){
                    if(value.contains("'")) return value.replace("'","").trim();
                    else if(value.equals("null")) return null;
                    else{
                        throw new Exception("Value " + "(" + value +")" + " need to be wraped in (') - " + "('"+value+"')");
                    }
                }
            }
            for(int i = 0 ; i < longValues.length; i++){
                if(key.equalsIgnoreCase(longValues[i])){
                    if(value.equals("null")) return null;
                    return Long.parseLong(value);
                }
            }
            for(int i = 0 ; i < doubleValues.length; i++){
                if(key.equalsIgnoreCase(doubleValues[i])){
                    if(value.equals("null")) return null;
                    return Double.parseDouble(value);
                }
            }
            for(int i = 0 ; i < booleanValues.length; i++){
                if(key.equalsIgnoreCase(booleanValues[i])){
                    if(value.equals("null")) return null;
                    return Boolean.parseBoolean(value);
                }
            }
            throw new Exception(key + ": is not a valid key");

        }
        catch (NumberFormatException ex){
            throw ex;

        }

    }
    private static String keyConverter(String key) throws Exception{
        try{
            for(int i = 0 ; i < stringValues.length; i++){
                if(key.equalsIgnoreCase(stringValues[i])){
                    return stringValues[i];
                }
            }
            for(int i = 0 ; i < longValues.length; i++){
                if(key.equalsIgnoreCase(longValues[i])){
                    return longValues[i];
                }
            }
            for(int i = 0 ; i < doubleValues.length; i++){
                if(key.equalsIgnoreCase(doubleValues[i])){
                    return doubleValues[i];
                }
            }
            for(int i = 0 ; i < booleanValues.length; i++){
                if(key.equalsIgnoreCase(booleanValues[i])){
                    return booleanValues[i];
                }
            }
            throw new Exception("Invalid request format");

        }
        catch (NumberFormatException ex){
            throw ex;

        }

    } // Преобразование регистра ключа к заданному виду
    private static boolean conditionConverter(Map<String,Object> object, String condition) throws Exception{
        Pattern patterns[] = new Pattern[]{
                Pattern.compile(">="),
                Pattern.compile("<="),
                Pattern.compile("!="),
                Pattern.compile("="),
                Pattern.compile(">"),
                Pattern.compile("<"),
                Pattern.compile("ilike"),
                Pattern.compile("like"),

                };
        //Проверка првальности расположения и использования операторов
        for(int i = 0; i < patterns.length; i++){
            if(patterns[i].split(condition).length > 1){
                for(int j = 0; j < patterns.length; j++){
                    if(patterns[j].split(patterns[i].split(condition)[1]).length > 1){
                        throw new Exception("Invalid operator format");
                    }
                }
                if((patterns[i].split(condition)[0].trim().startsWith("\'") && patterns[i].split(condition)[0].trim().endsWith("\'") ||
                        patterns[i].split(condition)[0].trim().startsWith("\"") && patterns[i].split(condition)[0].trim().endsWith("\""))){
                    //Приведение ключа и значения к нужному формату

                    String key;
                    key = patterns[i].split(condition)[0].replace("\'","");
                    key = key.replace("\"","");
                    key = key.trim();
                    key = keyConverter(key);

                    String value;

                    value = patterns[i].split(condition)[1].replace("\'","");

                    value = value.replace("\"","");
                    
                    value = value.trim();
                    value = valueConverter(key,value).toString();

                    //Проверка на существование ключа key

                    if(!object.containsKey(key)) throw new Exception("Key " + key + "not found, check key");

                    //Проверка соблюдения типа операндов
                    if(checkKey(key,patterns[i].pattern())){
                        return compareValues(object.get(key).toString().trim(),value.trim(),patterns[i].pattern());

                    }
                    else{
                        throw new Exception("Invalid condition operator");
                    }

                }
                else{
                    throw new Exception("Invalid condition format");
                }
            }
        }
        return false;
    }
    private static boolean checkKey(String key, String operator)throws Exception{ // Метод возвращающий true если значение по ключ можно использовать с оператором
        Map<String, String[][]> operators = new HashMap<>(); // Ключ - оператор, значение - массивы допустимых к нему ключей
        operators.put(">=", new String[][]{doubleValues,longValues});
        operators.put("<=", new String[][]{doubleValues,longValues});
        operators.put(">", new String[][]{doubleValues,longValues});
        operators.put("<", new String[][]{doubleValues,longValues});
        operators.put("=", new String[][]{doubleValues,longValues,booleanValues,stringValues});
        operators.put("!=", new String[][]{doubleValues,longValues,booleanValues,stringValues});
        operators.put("like", new String[][]{stringValues});
        operators.put("ilike", new String[][]{stringValues});

        //Проверка допустимости типа операнда
        for(Map.Entry<String, String[][]> item : operators.entrySet()){
            if(item.getKey().equals(operator)){
                String[][] availibleKeys = item.getValue();
                for(int i = 0; i < availibleKeys.length; i++){
                    for(int j = 0; j < availibleKeys[i].length; j++){
                        if(key.equals(availibleKeys[i][j])){ // Проверка вхождения key в массив допустимых типов

                            return true;
                        }
                    }
                }

            }
        }

        throw new Exception("Invalid condition format");

    }
    private static boolean compareValues (String value1, String value2, String operator)throws Exception{
        // Все числовые значения можно сравнить приведя к типу Float без потери точности
        if(value1.equals(""))value1 = "null";
        if(value2.equals(""))value2 = "null";
        switch (operator){
            case ">=":
                if(Double.parseDouble(value1) >= Double.parseDouble(value2))return true;
                else return false;


            case "<=":
                if(Double.parseDouble(value1) <= Double.parseDouble(value2))return true;
                else return false;

            case ">":
                if(Double.parseDouble(value1) > Double.parseDouble(value2))return true;
                else return false;

            case "<":
                if(Double.parseDouble(value1) < Double.parseDouble(value2))return true;
                else return false;

            case "!=": //Числа можно сравнить в строковом виде, учтя формат строк(Сделано ранее)
                if(!value1.equals(value2))return true;
                else return false;

            case "=":
                if(value1.equals(value2)) return true;
                else return false;

            case "like":
                if(value2.startsWith("%") && value2.endsWith("%")){
                    if(value1.contains(value2.replace("%",""))){
                        return true;
                    }
                }
                else if (value2.startsWith("%")) {
                    if(value1.endsWith(value2.replace("%",""))){
                        return true;
                    }

                }
                else if (value2.endsWith("%")) {
                    if(value1.startsWith(value2.replace("%",""))){
                        return true;
                    }
                }
                else{
                    if(value1.equals(value2)){
                        return true;
                    }

                }
                return false;


            case "ilike":
                if(value2.startsWith("%") && value2.endsWith("%")){
                    if(value1.toLowerCase().contains(value2.replace("%","").toLowerCase())){
                        return true;
                    }
                }
                else if (value2.startsWith("%")) {
                    if(value1.toLowerCase().endsWith(value2.replace("%","").toLowerCase())){
                        return true;
                    }

                }
                else if (value2.endsWith("%")) {
                    if(value1.toLowerCase().startsWith(value2.replace("%","").toLowerCase())){
                        return true;
                    }
                }
                else{
                    if(value1.equalsIgnoreCase(value2)){
                        return true;
                    }
                }
                return false;
        }
        throw new Exception("Invalid operator format");
    }

}

