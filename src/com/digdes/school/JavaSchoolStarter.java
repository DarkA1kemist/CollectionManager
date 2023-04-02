package com.digdes.school;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JavaSchoolStarter {
    public List<Map<String, Object>> collection = new ArrayList<>();
    public JavaSchoolStarter(){}
    public List<Map<String, Object>> execute(String request) throws Exception{
        RequestHandler insertHandler = new InsertHandler();
        RequestHandler updateHandler = new UpdateHandler();
        RequestHandler selectHandler = new SelectHandler();
        RequestHandler deleteHandler = new DeleteHandler();

        if(request.trim().split(" ")[0].trim().equalsIgnoreCase("insert")){
            return insertHandler.applyRequest(collection,request);
        }
        else if(request.trim().split(" ")[0].trim().equalsIgnoreCase("update")){
            return updateHandler.applyRequest(collection,request);
        }
        else if(request.trim().split(" ")[0].trim().equalsIgnoreCase("select")){
            return selectHandler.applyRequest(collection,request);
        }
        else if(request.trim().split(" ")[0].trim().equalsIgnoreCase("delete")){
            return deleteHandler.applyRequest(collection,request);
        }
        else{
            throw new Exception("Invalid Request Type!!!");
        }


    }


}
