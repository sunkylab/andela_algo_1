package com.andela.algotest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CountFruits {

    private static final Map<String, Map<String, Integer>> alphaCount = new HashMap<>();

    public static void main(String[] args) {
        //question 1 to 4
        String[] fruits = {"Strawberry", "Mango", "Cherry", "Lime", "Guava",
                "Papaya", "Nectarine", "Pineapple", "Lemon", "Plum", "Tangerine", "Fig",
                "Blueberry", "Grape", "Jackfruit", "Pomegranate", "Apple", "Pear",
                "Orange", "Watermelon", "Raspberry", "Banana"};

        getNumberOfOccurrencesPerCharacter(fruits);
        printResult();

        //question 5 solution
        String[] jsonResponse = getStringContent().toArray(new String[0]);

        getNumberOfOccurrencesPerCharacter(jsonResponse);
        printResult();

    }

    private static void getNumberOfOccurrencesPerCharacter(String [] args){

        alphaCount.clear();

        Arrays.asList(args).stream().forEach(arg->{

            String key = String.valueOf(arg.charAt(0));

            Map<String,Integer> keyVal = alphaCount.containsKey(key) ? alphaCount.get(key) : new HashMap();

            if(keyVal.containsKey(arg)){
                int val = keyVal.get(arg) + 1;
                keyVal.put(arg,val);
                keyVal.put(arg+val,0);//added dummy count to ensure map length reflects total count
            }else{
                keyVal.put(arg,1);
            }
            alphaCount.put(key,keyVal);
        });

    }

    private static List<String> getStringContent() {

        List<String> fruitsApiResponse = new ArrayList<>();

        try {

            CloseableHttpClient client = HttpClientBuilder.create().build();

            HttpPost get = new HttpPost("https://d1a9a19f-529f-4121-b292-bca738be20f3.mock.pstmn.io/?sent=2018/246090&AspxAutoDetectCookieSupport=1");

            HttpResponse response = client.execute(get);

            InputStream is = response.getEntity().getContent();
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(is));
            StringBuilder str = new StringBuilder();

            String line = null;

            while ((line = bufferedReader.readLine()) != null) {
                str.append(line + "\n");
            }

            fruitsApiResponse = new ObjectMapper().readValue(str.toString(),List.class);

        } catch (Exception e) {

        }

        return fruitsApiResponse;

    }

    private static void printResult(){

        char c;
        for(c = 'A'; c <= 'Z'; ++c){
            String key = String.valueOf(c);

            if(alphaCount.containsKey(key)){
                System.out.println(c + ": "+alphaCount.get(key).size());
                // this is included to cater for question 4
                alphaCount.get(key).forEach((s, integer) -> {
                    if(integer > 0){
                        System.out.println(integer+" "+s);
                    }
                });
                // end of inclusion to cater for question 4
            }else{
                System.out.println(c + ": 0");
            }
        }
    }
}

