package com.example.passportdemo.process;

import com.example.passportdemo.utils.HttpUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * globaltix
 * https://documenter.getpostman.com/view/3866783/globaltix-partner-api/RVnSH2gE
 * @author muxiaorui
 * @create 2018-06-29 15:16
 **/
public class Globaltix {
    private static final String GT_URL="https://uat-api.globaltix.com/api";
    private static final String AUTH_URL="/auth/login";
    private static final String LIST_COUNTRY_URL="/country/getAllListingCountry";
    private static final String TICKET_LIST_URL="/ticketType/list" ;
    private static final String ACCEPT_VERSION="1.0";
    private static final String AUTHORIZATION="Authorization";
    private static final String CRETAE_BOOKING="/transaction/create";
    private static final String CANCEL_ORDER="/transaction/revoke";
    private static final String QUERY_ORDER="/transaction/getTransactionDetailsByReferenceNumber";
    public static final String ACCESS_TOKEN="access_token";

    public static final String CREADIT_BALANCE="/credit/getCreditByReseller";
    @Resource
    private static StringRedisTemplate stringRedisTemplate;

    public static void authentication(String username, String password,String reqMethod) {
        //if (!stringRedisTemplate.hasKey(ACCESS_TOKEN)) {
            System.out.println("username:" + username + ",password:" + password);
            Map<String, String> authData = new HashMap<String, String>();
            authData.put("username", username);
            authData.put("password", password);

            String reqJson = JSONObject.fromObject(authData).toString();
            System.out.println("reqJson:" + reqJson);
            String req_url=GT_URL+reqMethod;
            String response=  HttpUtil.requestPostJson(req_url,reqJson);
            System.out.println(response);
            JSONObject rst1 = JSONObject.fromObject(response);
            System.out.println(rst1.get("success"));
            String data=rst1.getString("data");
            JSONObject dataObject = JSONObject.fromObject(data);
            String accessToken=dataObject.getString("access_token");
            String tokenType=dataObject.getString("token_type");
            Long expiresIn=dataObject.getLong("expires_in");
            System.out.println(accessToken);
            System.out.println("tokenType:"+tokenType);
            String authorization=tokenType+""+accessToken;
          //  stringRedisTemplate.opsForValue().set(ACCESS_TOKEN,accessToken,expiresIn, TimeUnit.SECONDS);
            System.out.println("放入緩存");
//        } else {
//            System.out.println("token还未失效");
//        }


    }
    public static void getAllListingCountry(String reqMethod){
        String authorization="Bearer"+"\n"+"eyJhbGciOiJIUzI1NiJ9.eyJwcmluY2lwYWwiOm51bGwsInN1YiI6InJlc2VsbGVyQGdsb2JhbHRpeC5jb20iLCJleHAiOjE1MzA2MTA4MTksImlhdCI6MTUzMDUyNDQxOSwicm9sZXMiOlsiUkVTRUxMRVJfQURNSU4iLCJSRVNFTExFUl9GSU5BTkNFIiwiUkVTRUxMRVJfT1BFUkFUSU9OUyJdfQ.dyt-vSOADTou9aPLWPT9tGZO_yHjw567YiibeAOpN2E";
        Map<String, Object> header = new HashMap<String, Object>();
        header.put("Accept-Version", ACCEPT_VERSION);
        header.put("Authorization", authorization);
        String url=GT_URL+reqMethod;
        String res=  HttpUtil.requestGetNew(url.toString(), header);
        System.out.println("" +
                "res:"+res);

    }
//    https://uat-api.globaltix.com/api/ticketType/list?attraction_id=21
    public static void getTicketType(String reqMethod,Long attractionId){
        String authorization="Bearer"+"\n"+"eyJhbGciOiJIUzI1NiJ9.eyJwcmluY2lwYWwiOm51bGwsInN1YiI6InJlc2VsbGVyQGdsb2JhbHRpeC5jb20iLCJleHAiOjE1MzA2MTA4MTksImlhdCI6MTUzMDUyNDQxOSwicm9sZXMiOlsiUkVTRUxMRVJfQURNSU4iLCJSRVNFTExFUl9GSU5BTkNFIiwiUkVTRUxMRVJfT1BFUkFUSU9OUyJdfQ.dyt-vSOADTou9aPLWPT9tGZO_yHjw567YiibeAOpN2E";
        Map<String, Object> header = new HashMap<String, Object>();
        header.put("Accept-Version", ACCEPT_VERSION);
        header.put("Authorization", authorization);
        String url=GT_URL+reqMethod+"?attraction_id="+attractionId;
        String res=  HttpUtil.requestGetNew(url.toString(), header);
        System.out.println("" +
                "res:"+res);

    }
    public static void createBooking(String reqMethod) {
        String authorization="Bearer"+"\n"+"eyJhbGciOiJIUzI1NiJ9.eyJwcmluY2lwYWwiOm51bGwsInN1YiI6InJlc2VsbGVyQGdsb2JhbHRpeC5jb20iLCJleHAiOjE1MzA2MTA4MTksImlhdCI6MTUzMDUyNDQxOSwicm9sZXMiOlsiUkVTRUxMRVJfQURNSU4iLCJSRVNFTExFUl9GSU5BTkNFIiwiUkVTRUxMRVJfT1BFUkFUSU9OUyJdfQ.dyt-vSOADTou9aPLWPT9tGZO_yHjw567YiibeAOpN2E";
        Map<String, Object> header = new HashMap<String, Object>();
        header.put("Accept-Version", ACCEPT_VERSION);
        header.put("Authorization", authorization);
        Map<String, Object> ticketTypes = new HashMap<String, Object>();
        List<Map<String, Long> > tickets=new ArrayList<Map<String, Long> >();
        Map<String, Long> ticket = new HashMap<String, Long>();
        ticket.put("index", 0l);
        ticket.put("id", 43l);
        ticket.put("fromResellerId", null);
        ticket.put("quantity", 3l);
        tickets.add(ticket);
        ticketTypes.put("ticketTypes",tickets);
        ticketTypes.put("customerName","Lvmama001");
        ticketTypes.put("email","183895908@qq.com");
        ticketTypes.put("paymentMethod","CREDIT");

        List<Map<String, String> > otherInfo=new ArrayList<Map<String, String> >();
        Map<String, String> partnerReference = new HashMap<String, String>();
        partnerReference.put("partnerReference","DS002");
        otherInfo.add(partnerReference);
        ticketTypes.put("otherInfo",otherInfo);
        String reqJson = JSONObject.fromObject(ticketTypes).toString();
        System.out.println("reqJson:" + reqJson);
        String req_url=GT_URL+reqMethod;
        String response=  HttpUtil.requestPostJsonWithHeader(req_url,reqJson,header);
        System.out.println("response"+response);
        JSONObject rst1 = JSONObject.fromObject(response);
        System.out.println(rst1.get("success"));
        String data=rst1.getString("data");
//        JSONObject dataObject = JSONObject.fromObject(data);



    }


    public static void cancelOrder(String reqMethod,String referenceNumber) {
        String authorization="Bearer"+"\n"+"eyJhbGciOiJIUzI1NiJ9.eyJwcmluY2lwYWwiOm51bGwsInN1YiI6InJlc2VsbGVyQGdsb2JhbHRpeC5jb20iLCJleHAiOjE1MzA2MTA4MTksImlhdCI6MTUzMDUyNDQxOSwicm9sZXMiOlsiUkVTRUxMRVJfQURNSU4iLCJSRVNFTExFUl9GSU5BTkNFIiwiUkVTRUxMRVJfT1BFUkFUSU9OUyJdfQ.dyt-vSOADTou9aPLWPT9tGZO_yHjw567YiibeAOpN2E";
        Map<String, Object> header = new HashMap<String, Object>();
        header.put("Accept-Version", ACCEPT_VERSION);
        header.put("Authorization", authorization);

        String req_url=GT_URL+reqMethod+"?reference_number="+referenceNumber;
        System.out.println("req_url===="+req_url);
        String response=  HttpUtil.requestGetNew(req_url,header);
        System.out.println("response"+response);
        JSONObject rst1 = JSONObject.fromObject(response);
        System.out.println(rst1.get("success"));
        String data=rst1.getString("data");
    }

    public static void queryOrder(String reqMethod,String referenceNumber) {
        String authorization="Bearer"+"\n"+"eyJhbGciOiJIUzI1NiJ9.eyJwcmluY2lwYWwiOm51bGwsInN1YiI6InJlc2VsbGVyQGdsb2JhbHRpeC5jb20iLCJleHAiOjE1MzA2MTA4MTksImlhdCI6MTUzMDUyNDQxOSwicm9sZXMiOlsiUkVTRUxMRVJfQURNSU4iLCJSRVNFTExFUl9GSU5BTkNFIiwiUkVTRUxMRVJfT1BFUkFUSU9OUyJdfQ.dyt-vSOADTou9aPLWPT9tGZO_yHjw567YiibeAOpN2E";
        Map<String, Object> header = new HashMap<String, Object>();
        header.put("Accept-Version", ACCEPT_VERSION);
        header.put("Authorization", authorization);

        String req_url=GT_URL+reqMethod+"?reference_number="+referenceNumber;
        System.out.println("req_url===="+req_url);
        String response=  HttpUtil.requestGetNew(req_url,header);
        System.out.println("response"+response);
        JSONObject rst1 = JSONObject.fromObject(response);
        System.out.println(rst1.get("success"));
        String data=rst1.getString("data");
        JSONObject dataObject = JSONObject.fromObject(data);
        JSONArray tickets=dataObject.getJSONArray("tickets");
        for(int i=0;i<tickets.size();i++){
            JSONObject temp=  tickets.getJSONObject(i);
            String ticketInfo=temp.getString("id")+" "+temp.getString("code");
          String status=  temp.getJSONObject("status").getString("name");
          System.out.println("ticketInfo:"+ticketInfo+",ticketStatus:"+status);
        }
    }

    public static void creditBalance(String reqMethod) {
        String authorization="Bearer"+"\n"+"eyJhbGciOiJIUzI1NiJ9.eyJwcmluY2lwYWwiOm51bGwsInN1YiI6InJlc2VsbGVyQGdsb2JhbHRpeC5jb20iLCJleHAiOjE1MzEyOTE4MDEsImlhdCI6MTUzMTIwNTQwMSwicm9sZXMiOlsiUkVTRUxMRVJfQURNSU4iLCJSRVNFTExFUl9GSU5BTkNFIiwiUkVTRUxMRVJfT1BFUkFUSU9OUyJdfQ.Tfp7sJ_9NguyCHhqPa6toqe51LFXf2wirvxU9lHqtC8";
        Map<String, Object> header = new HashMap<String, Object>();
        header.put("Accept-Version", ACCEPT_VERSION);
        header.put("Authorization", authorization);

        String req_url=GT_URL+reqMethod;
        System.out.println("req_url===="+req_url);
        String response=  HttpUtil.requestGetNew(req_url,header);
        System.out.println("response"+response);
        JSONObject rst1 = JSONObject.fromObject(response);
        System.out.println(rst1.get("success"));
        if("true".equals(rst1.getString("success"))){
            String data=rst1.getString("data");
            JSONObject dataObject = JSONObject.fromObject(data);
            Long balance=dataObject.getLong("balance");
            System.out.println("balance:"+balance);
        }


    }








    public static void main(String args[]) throws Exception{
//      authentication("reseller@globaltix.com","12345",AUTH_URL);
//        getAllListingCountry(LIST_COUNTRY_URL);
//       getTicketType(TICKET_LIST_URL,21L);
//        createBooking(CRETAE_BOOKING);
//        cancelOrder(CANCEL_ORDER,"IDYLHW3AGT");
//        queryOrder(QUERY_ORDER,"IDYLHW3AGT");

//        creditBalance(CREADIT_BALANCE);

        String req="sss";
        System.out.println(req.getBytes("UTF-8"));
        Map<String, String> authData = new HashMap<String, String>();
        authData.put("oid", null);
        authData.put("code", "2018071165957414");
        authData.put("status", "1");
        String reqJson = JSONObject.fromObject(authData).toString();
        System.out.println(reqJson);
        System.out.println(reqJson.getBytes("UTF-8"));
    }
}
