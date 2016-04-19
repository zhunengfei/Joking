package org.guog.gh.mretrofittest.service;

import org.guog.gh.mretrofittest.bean.JokeInfo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by gh on 16/4/18.
 */
public interface JokeService {

    @GET("/joke/content/list.from")
    Call<JokeInfo> getJokeInfoList(@Query("sort") String sort, @Query("page") int page,
          @Query("pagesize") int pagesize, @Query("time") String time, @Query("key") String key);

}
