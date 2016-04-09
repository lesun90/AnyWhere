package lesun.anywhere;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


/**
 * Created by youbot on 4/9/2016.
 */

class Place{
    private String name;
    private String category;
    private String rating;
    private String open;
    public double latitude;
    public double longitude;


    public Place() {
        this.name      = "";
        this.rating    = "";
        this.open      = "";
        this.latitude  = 0;
        this.longitude = 0;
        this.setCategory("");
    }

    public void setLatitude(String latitude){
        this.latitude =  Double.parseDouble(latitude);
    }

    public void setLongitude(String longitude){
        this.longitude =  Double.parseDouble(longitude);
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getName() {

        return name;
    }

    public String getCategory() {

        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getRating() {

        return rating;
    }

    public void setOpenNow(String open) {

        this.open = open;
    }

    public String getOpenNow() {

        return open;
    }
}

class GooglePlaces extends AsyncTask<String[],Void,String> {

    ArrayList<Place> placesList;
    private Context context;
    protected static final String TAG = "MainActivity";
    ArrayList<Place> apiResult;
    ArrayList<String> placeTypes;

    String destLongitude;
    String destLatitude;
    String destName;
    private MainActivity activityRef;
    Button mGetDirection;

    public GooglePlaces(MainActivity current){
        this.context = current;
        this.activityRef = current;
        mGetDirection = (Button) ((Activity)context).findViewById(R.id.get_direction);
    }

    @Override
    protected String doInBackground(String[]... params) {
        // make Call to the url
        String user_input[] = params[0];
        String latitude   = user_input[0];
        String longitude  = user_input[1];
        String radius     = user_input[2];
        String choice     = user_input[3];

        placeTypes = new ArrayList();
        if (choice.equals("Hang Out"))
        {
            String [] types = activityRef.getResources().getStringArray(R.array.HangOut);
            for (int i = 0 ; i < types.length; i++)
            {
                placeTypes.add(types[i]);
            }
            Collections.shuffle(placeTypes);
        }
        else if (choice.equals("Enjoy Nature"))
        {
            String [] types = activityRef.getResources().getStringArray(R.array.EnjoyNature);
            for (int i = 0 ; i < types.length; i++)
            {
                placeTypes.add(types[i]);
            }
            Collections.shuffle(placeTypes);
        }
        else if (choice.equals("Interesting Place"))
        {
            String [] types = activityRef.getResources().getStringArray(R.array.InterestingPlace);
            for (int i = 0 ; i < types.length; i++)
            {
                placeTypes.add(types[i]);
            }
            Collections.shuffle(placeTypes);
        }
        else if (choice.equals("Shopping"))
        {
            String [] types = activityRef.getResources().getStringArray(R.array.Shopping);
            for (int i = 0 ; i < types.length; i++)
            {
                placeTypes.add(types[i]);
            }
            Collections.shuffle(placeTypes);
        }
        else if (choice.equals("Relax"))
        {
            String [] types = activityRef.getResources().getStringArray(R.array.Relax);
            for (int i = 0 ; i < types.length; i++)
            {
                placeTypes.add(types[i]);
            }
            Collections.shuffle(placeTypes);
        }
        else
        {
            String [] types;
            types = activityRef.getResources().getStringArray(R.array.HangOut);
            for (int i = 0 ; i < types.length; i++)
            {
                placeTypes.add(types[i]);
            }
            types = activityRef.getResources().getStringArray(R.array.EnjoyNature);
            for (int i = 0 ; i < types.length; i++)
            {
                placeTypes.add(types[i]);
            }
            types = activityRef.getResources().getStringArray(R.array.InterestingPlace);
            for (int i = 0 ; i < types.length; i++)
            {
                placeTypes.add(types[i]);
            }
            types = activityRef.getResources().getStringArray(R.array.Shopping);
            for (int i = 0 ; i < types.length; i++)
            {
                placeTypes.add(types[i]);
            }
            types = activityRef.getResources().getStringArray(R.array.Relax);
            for (int i = 0 ; i < types.length; i++)
            {
                placeTypes.add(types[i]);
            }
            Collections.shuffle(placeTypes);

        }

        apiResult = new ArrayList<Place>();
        int index = 0;
        while ((apiResult.size()==0) && (index < placeTypes.size()))
        {
            String placeType = placeTypes.get(index);
            index++;
            apiResult = makeCall("https://maps.googleapis.com/maps/api/place/search/json?location=" + latitude + "," + longitude + "&radius=" + radius + "&keyword=" + placeType + "&key=" + context.getResources().getString(R.string.google_api_key));
            //System.out.println("https://maps.googleapis.com/maps/api/place/search/json?location=" + latitude + "," + longitude + "&radius=" + radius + "&keyword="+ placeType + "&key=" + context.getResources().getString(R.string.google_api_key));
        }
        System.out.println(apiResult.size());

        //print the call in the console
        return "";
    }

    @Override
    protected void onPreExecute() {
        mGetDirection.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onPostExecute(String result) {
        if ((apiResult == null) ||(apiResult.size()==0)) {
//            System.out.println("No PLACES");
            Toast.makeText(activityRef, "Can't find any places, increase range or change type.", Toast.LENGTH_SHORT).show();
        }
        else {
            // parse Google places search result
            placesList = apiResult;

            Random rand = new Random();
            int min = 0;
            int max = placesList.size()-1;
            int randomNum = rand.nextInt((max - min) + 1) + min;

            destLatitude = placesList.get(randomNum).latitude +"";
            destLongitude = placesList.get(randomNum).longitude +"";
            destName = placesList.get(randomNum).getName();

            activityRef.destLatitude = destLatitude;
            activityRef.destLongitude = destLongitude;
            activityRef.destName = destName;

            //
            mGetDirection.setVisibility(View.VISIBLE);
            mGetDirection.setText("Go to " + destName);
            Toast.makeText(activityRef, "Get a place: " + destName, Toast.LENGTH_SHORT).show();

        }
    }

    public static ArrayList<Place> makeCall(String param) {
        BufferedReader bufferedReader = null;
        try {
            URL url = new URL(param);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            StringBuilder sb = new StringBuilder();

            bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String json;
            while((json = bufferedReader.readLine())!= null){
                sb.append(json+"\n");
            }
            //System.out.println(sb.toString().trim());

            return (ArrayList<Place>) parseGoogleParse(sb.toString().trim());

        }catch(Exception e){
            return null;
        }
    }

    private static ArrayList<Place> parseGoogleParse(final String response) {

        ArrayList<Place> temp = new ArrayList<Place>();
        try {
            // make an jsonObject in order to parse the response
            JSONObject jsonObject = new JSONObject(response);

            // make an jsonObject in order to parse the response
            if (jsonObject.has("results")) {
                JSONArray jsonArray = jsonObject.getJSONArray("results");
                for (int i = 0; i < jsonArray.length(); i++) {
                    Place poi = new Place();
                    if (jsonArray.getJSONObject(i).has("geometry")) {
                        if (jsonArray.getJSONObject(i).getJSONObject("geometry").has("location")){
                            if (jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").has("lat")){
                                poi.setLatitude(jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").optString("lat"));
                            }
                            if (jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").has("lng")){
                                poi.setLongitude(jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").optString("lng"));
                            }
                        }
                    }
                    if (jsonArray.getJSONObject(i).has("name")) {
                        poi.setName(jsonArray.getJSONObject(i).optString("name"));
                        poi.setRating(jsonArray.getJSONObject(i).optString("rating", " "));

                        if (jsonArray.getJSONObject(i).has("opening_hours")) {
                            if (jsonArray.getJSONObject(i).getJSONObject("opening_hours").has("open_now")) {
                                if (jsonArray.getJSONObject(i).getJSONObject("opening_hours").getString("open_now").equals("true")) {
                                    poi.setOpenNow("YES");
                                } else {
                                    poi.setOpenNow("NO");
                                }
                            }
                        } else {
                            poi.setOpenNow("Not Known");
                        }
                        if (jsonArray.getJSONObject(i).has("types")) {
                            JSONArray typesArray = jsonArray.getJSONObject(i).getJSONArray("types");

                            for (int j = 0; j < typesArray.length(); j++) {
                                poi.setCategory(typesArray.getString(j) + ", " + poi.getCategory());
                            }
                        }
                    }
                    temp.add(poi);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<Place>();
        }

        return temp;

    }
}
