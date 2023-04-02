package com.digdes.school;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SelectHandler implements RequestHandler {
    public List<Map<String, Object>> applyRequest(List<Map<String, Object>> collection, String request) throws Exception {
        if(request.equalsIgnoreCase("select")){
            return collection;
        }
        if(request.split(" ")[0].equalsIgnoreCase( "select")){
            if(request.split(" ")[1].equalsIgnoreCase( "where")){
                if(!request.toLowerCase().split("where")[1].trim().equals("")) {
                    request = request.split("(?i)where")[1];
                    List<Map<String, Object>> selectedList = new ArrayList<>();
                    for(Map<String, Object> item : collection){
                        if(DataExecuter.checkCondition(request.trim(),item)){
                            selectedList.add(item);
                        }
                    }
                    return selectedList;
                }
                else {
                    throw new Exception("Empty Request");
                }

            }

        }

        return null;
    }

}
