package com.digdes.school;

import java.util.List;
import java.util.Map;

public class UpdateHandler implements RequestHandler{
    public List<Map<String, Object>> applyRequest(List<Map<String, Object>> collection, String request) throws Exception {
        if(request.split(" ")[0].equalsIgnoreCase( "update")){

            if(request.split(" ")[1].equalsIgnoreCase( "values")){

                if(!request.toLowerCase().split("values")[1].trim().equals( "")) {

                    request = request.split("(?i)values")[1];
                    if(request.indexOf("where") >= 0){

                        String collectionData = request.split("where")[0];
                        String conditionData = request.split("where")[1];

                        for(Map<String,Object> item : collection){

                            if(DataExecuter.checkCondition(conditionData,item)){

                                Map<String,Object> updatedValues = DataExecuter.getDataMap(collectionData.trim());

                                for(Map<String, Object> object : collection){
                                    for(Map.Entry<String, Object> changeField : updatedValues.entrySet()){
                                        if(!changeField.getValue().equals(""))item.put(changeField.getKey(), changeField.getValue());
                                    }
                                }
                            }
                        }
                    }
                    else{
                        Map<String,Object> updatedValues = DataExecuter.getDataMap(request);
                        for(Map<String, Object> item : collection){
                            for(Map.Entry<String, Object> changeField : updatedValues.entrySet()){
                                if(!changeField.getValue().equals(""))item.put(changeField.getKey(), changeField.getValue());
                            }
                        }
                    }
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
