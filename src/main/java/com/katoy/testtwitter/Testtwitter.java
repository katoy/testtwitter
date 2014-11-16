/*
 * See https://teratail.com/questions/3443
 */
package com.katoy.testtwitter;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

/**
 * @author katoy
 */
public class Testtwitter {

    static final String DEFAULT_WORD = "アベノミクス";
    static final int MAX_PAGE = 15; // 1;

    static String word = DEFAULT_WORD;
    static int page = MAX_PAGE;
    static String from_date = null; // "2014-11-01";
    static String to_date = null;   //  "2014-11-09";

    /**
     * コマンドメイン
     *
     * @param args コマンドラインパラメータ
     */
    public static void main(String[] args) {
        parse_args(args);
        try {
            new TwitterReader().reader(Testtwitter.word,
                    Testtwitter.from_date, Testtwitter.to_date,
                    Testtwitter.page);
        } catch (TwitterException ex) {
            // TODO Auto-generated catch block
            ex.printStackTrace();
        }
    }

    static void parse_args(String[] args) {
        if (args.length > 4) {
            throw new IllegalArgumentException();
        }
        if (args.length > 3) {
            Testtwitter.to_date = args[3];
        }
        if (args.length > 2) {
            Testtwitter.from_date = args[2];
        }
        if (args.length > 1) {
            Testtwitter.page = Integer.parseInt(args[1]);
        }
        if (args.length > 0) {
            Testtwitter.word = args[0];
        }
    }
}

class TwitterReader {

    /**
     *
     * @param word 検索ワード
     * @param from_date 期間の開始 YYYY-MM-DD
     * @param to_date 期間の収容 YYYY-MM-DD
     * @param page 検索するページ数
     * @return int 検索した tweet 数。
     * @throws TwitterException
     */
    int reader(final String word, final String from_date, final String to_date, final int page) throws TwitterException {
        // 初期化
        int count = 0;  // 取得した総件数
        final Twitter twitter = new TwitterFactory().getInstance();
        Query query = new Query();

        // 検索ワードをセット
        query.setQuery(word);

        // 1 度のリクエストで取得する Tweet の数（100が最大）
        query.setCount(100);
        query.resultType(Query.RECENT);
        if (from_date != null) {
          query.since(from_date);
        }
        if (to_date != null) {
          query.until(to_date);
        }
        for (int i = 0; i < page; i++) {
            QueryResult result = twitter.search(query);
            System.out.println("ヒット数 : " + result.getTweets().size());
            System.out.println("ページ数 : " + i);

            // 検索結果を見てみる
            for (Status tweet : result.getTweets()) {
                // 本文
                String str = tweet.getText();
                java.util.Date hiduke = tweet.getCreatedAt();
                count += 1;
                System.out.println("" + count + "\t" + hiduke + str);
                // ハッシュタグ と URL の削除
            }
            if (result.hasNext()) {
                query = result.nextQuery();
            } else {
                break;
            }
        }
        return count;
    }
}
// End of File
