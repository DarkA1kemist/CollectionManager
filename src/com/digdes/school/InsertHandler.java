package com.digdes.school;

import java.util.List;
import java.util.Map;

public class InsertHandler implements RequestHandler{
    public List<Map<String, Object>> applyRequest(List<Map<String, Object>> collection, String request) throws Exception {
        if(request.split(" ")[0].equalsIgnoreCase( "insert")){
            if(request.split(" ")[1].equalsIgnoreCase( "values")){
                if(!request.toLowerCase().split("values")[1].trim().equals( "")) {
                    request = request.split("(?i)values")[1];
                    collection.add(DataExecuter.getDataMap(request));
                    return collection;
                }
                else {
                    throw new Exception("Empty Request");
                }

            }
            else {
                throw new Exception("Request form is not valid");
            }
        }
        return null;
    }
}
