package me.tomazwang.app.sunshine2;

/**
 * Created by Rbur on 2016/2/22.
 */

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @authour TomazWang
 */
public class ForecastFragment extends Fragment {

    private static final String TAG = ForecastFragment.class.getSimpleName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (menu.findItem(R.id.action_refresh) == null) {
            Log.v(TAG, "forecast_fragemnt menu not added");
            inflater.inflate(R.menu.forecast_fragment, menu);
        } else {
            Log.v(TAG, "forecast_fragemnt menu already added");
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            Log.v(TAG, "Select action refresh");
            FetchWeatherTask weatherTask = new FetchWeatherTask();
            weatherTask.execute("Taipei");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Create some dummy data for the ListView.  Here's a sample weekly forecast
        String[] data = {
                "Mon 6/23 - Sunny - 31/17",
                "Tue 6/24 - Foggy - 21/8",
                "Wed 6/25 - Cloudy - 22/17",
                "Thurs 6/26 - Rainy - 18/11",
                "Fri 6/27 - Foggy - 21/10",
                "Sat 6/28 - Rainy - 23/18",
                "Sun 6/29 - Sunny - 20/7"
        };


        List<String> weekForecast = new ArrayList<>(Arrays.asList(data));
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ListView mForecastList = (ListView) rootView.findViewById(R.id.listview_forecast);

        ListAdapter mForecastListAdapter = new ArrayAdapter<>(
                getActivity(),
                R.layout.list_item_forecast,
                R.id.list_item_forecast_textview,
                weekForecast
        );

        mForecastList.setAdapter(mForecastListAdapter);

        return rootView;

    }


    public class FetchWeatherTask extends AsyncTask<String, Void, Void> {


        @Override
        protected Void doInBackground(String... params) {

            if (params.length == 0) {
                Log.w(TAG, "doInBackground: no input");
                return null;
            }


            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string
            String forecastJsonStr = null;

            String format = "json";
            String units = "metric";
            int numDays = 7;
            String lang = "zh_cn";
            try {


                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are available at OWN's forecast API page,
                // at http:openweathermap.org/API#forecast

                final String FORCAST_BASE_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?";
                final String QUERY_PARAM = "q";
                final String FORMAT_PARAM = "mode";
                final String UNITS_PARAM = "units";
                final String DAYS_PARAM = "cnt";
                final String LANG_PRAM = "lang";
                final String APPID_PARAM = "APPID";


                Uri buildUri = Uri.parse(FORCAST_BASE_URL).buildUpon()
                        .appendQueryParameter(QUERY_PARAM, params[0])
                        .appendQueryParameter(FORMAT_PARAM, format)
                        .appendQueryParameter(UNITS_PARAM, units)
                        .appendQueryParameter(DAYS_PARAM, Integer.toString(numDays))
                        .appendQueryParameter(LANG_PRAM,lang)
                        .appendQueryParameter(APPID_PARAM, BuildConfig.OPEN_WEATHER_MAP_API_KEY)
                        .build();

                URL url = new URL(buildUri.toString());

                Log.i(TAG, "buildUri = " + buildUri.toString());

                // Create the request to OWM, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder buffer = new StringBuilder();
                if (inputStream == null) {
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                }


                if (buffer.length() == 0) {
                    return null;
                }

                forecastJsonStr = buffer.toString();
                Log.v(TAG, "forecastJsonStr = " + forecastJsonStr);

            } catch (IOException e) {
                Log.e(TAG, "Error ", e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }

                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(TAG, "Error closing stream", e);
                    }
                }
            }

            return null;

        }



        private String[] getWeatherDataFromJson(String forecastJsonStr, int numDays) throws JSONException{

            // These are the names of the JSON objects that need to be extracted.
            final String OWM_LIST = "list";
            final String OWM_WEATHER = "weather";
            final String OWM_TEMPERATURE = "temp";
            final String OWM_TEMP_MAX = "max";
            final String OWM_TEMP_MIN = "min";
            final String OWM_WEATHER_DESCRIPTION = "main";




            JSONObject forecastJson = new JSONObject(forecastJsonStr);
            JSONArray weatherArray = forecastJson.getJSONArray(OWM_LIST);


            Time dayTime = new Time();
            dayTime.setToNow();

            int julianStartDay = Time.getJulianDay(System.currentTimeMillis(),dayTime.gmtoff);




        }

    }
}
