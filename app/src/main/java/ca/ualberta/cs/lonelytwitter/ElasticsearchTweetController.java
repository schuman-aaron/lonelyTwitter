package ca.ualberta.cs.lonelytwitter;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

/**
 * Created by esports on 2/17/16.
 */
public class ElasticsearchTweetController {

    private static JestDroidClient client;

    //TODO: A function that gets tweets
    /*public static ArrayList<Tweet> getTweets(){
        verifyClient();
    }*/

    //TODO: A function that adds a tweet
    public static void addTweet(Tweet tweet) {
        verifyClient();

        Index index = new Index.Builder(tweet).index("testing").type("tweet").build();
        try {
            DocumentResult result = client.execute(index);
            if (result.isSucceeded()) {
                //Set the ID to tweet that elasticsearch told me it was
                tweet.setId(result.getId());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class GetTweetsTask extends AsyncTask<String, Void, ArrayList<Tweet>> {
        @Override
        protected ArrayList<Tweet> doInBackground(String... search_strings){
            verifyClient();

            //Start our initial array list (empty)
            ArrayList<Tweet> tweets = new ArrayList<Tweet>();

            //Note: I'm making a huge assumptions here, that only the first search term
            //will be used.

            Search search = new Search.Builder(search_strings[0]).addIndex("testing").addType("tweet").build();

            try {
                SearchResult execute = client.execute(search);
                if(execute.isSucceeded()){
                    //Return our list of tweets
                    List<NormalTweet> returned_tweets = execute.getSourceAsObjectList(NormalTweet.class);
                    tweets.addAll(returned_tweets);
                } else {
                    //TODO:
                    //TODO:
                    Log.i("TODO", "We actually failed here, search for tweets");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }


            return tweets;
        }
    }

    public static class SearchTweetsTask extends AsyncTask<String, Void, ArrayList<Tweet>> {

        public SearchTweetsTask(EditText bodyText) {
            doInBackground(bodyText.toString());
        }

        @Override
        protected ArrayList<Tweet> doInBackground(String... search_strings){
            verifyClient();

            //Start our initial array list (empty)
            ArrayList<Tweet> tweets = new ArrayList<Tweet>();

            //Note: I'm making a huge assumptions here, that only the first search term
            //will be used.

            Search search = new Search.Builder("my_index , my_type, { \"document\" : {\"message\" : {type\", search_strings + &size=10000").build();

            try {
                SearchResult execute = client.execute(search);
                if(execute.isSucceeded()){
                    //Return our list of tweets
                    List<NormalTweet> returned_tweets = execute.getSourceAsObjectList(NormalTweet.class);
                    tweets.addAll(returned_tweets);
                } else {
                    //TODO:
                    //TODO:
                    Log.i("TODO", "We actually failed here, search for tweets");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }


            return tweets;
        }

    }


    public static class AddTweetTask extends AsyncTask<NormalTweet, Void, Void> {
        @Override
        protected Void doInBackground(NormalTweet... tweets) {
            verifyClient();

            for (int i = 0; i < tweets.length; i++) {
                NormalTweet tweet = tweets[i];

                Index index = new Index.Builder(tweet).index("testing").type("tweet").build();
                try {
                    DocumentResult result = client.execute(index);
                    if (result.isSucceeded()) {
                        //Set the ID to tweet that elasticsearch told me it was
                        tweet.setId(result.getId());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    public static void verifyClient() {
        //1. Verify that 'client' exists
        //2. If it doesn't, make it
        if (client == null) {

            //TODO: Put this URL somewhere it makes sense (e.g. class variable)
            DroidClientConfig.Builder builder = new DroidClientConfig.Builder("http://cmput301.softwareprocess.es:8080/");
            DroidClientConfig config = builder.build();

            JestClientFactory factory = new JestClientFactory();
            factory.setDroidClientConfig(config);
            client = (JestDroidClient) factory.getObject();

        }
    }

}
