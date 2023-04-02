package com.digdes.school;

import java.util.*;

public interface RequestHandler {

    public List<Map<String, Object>> applyRequest(List<Map<String, Object>> collection,String request) throws Exception;


}
