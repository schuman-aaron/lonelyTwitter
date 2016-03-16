package ca.ualberta.cs.lonelytwitter;

import java.util.Date;

public class NormalTweet extends Tweet implements Tweetable, Comparable {
    public NormalTweet(Date date, String message) {
        super(date, message);
    }

    public NormalTweet(String message) {
        super(message);
    }

    public Date getDate() {
        return this.date;
    }

    public String getMessage() {
        return this.message;
    }

    @Override
    public Boolean isImportant() {
        return Boolean.FALSE;
    }

    @Override
    public int compareTo(Tweet tweet) {
        return tweet.getDate().compareTo(this.getDate());
    }

    public int compareTo(Object o) {
        return 0;
    }
}
