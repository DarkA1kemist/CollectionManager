package com.digdes.school;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DeleteHandler implements RequestHandler{

    @Override
    public List<Map<String, Object>> applyRequest(List<Map<String, Object>> collection, String request) throws Exception {
        if(request.trim().equalsIgnoreCase("delete")){
            collection.clear();
            return collection;
        }
        if(request.split(" ")[0].equalsIgnoreCase( "delete")){
            List<Map<String, Object>> result = new ArrayList<>();
            if(request.split(" ")[1].equalsIgnoreCase( "where")){
                if(!request.toLowerCase().split("where")[1].trim().equals("")) {
                    request = request.split("(?i)where")[1];

                    for(Map<String, Object> item : collection){
                        if(!DataExecuter.checkCondition(request.trim(),item)){

                            result.add(item);

                        }
                    }
                    collection.clear();
                    collection.addAll(result);
                    return collection;

                }
                else {
                    throw new Exception("Empty Request");
                }

            }


        }

        return null;
    }
}
