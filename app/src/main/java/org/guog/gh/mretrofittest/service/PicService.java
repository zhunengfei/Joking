package org.guog.gh.mretrofittest.service;

import org.guog.gh.mretrofittest.bean.PicInfo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by gh on 16/4/18.
 */
public interface PicService {

    @GET("/joke/img/text.from")
    Call<PicInfo> getPicInfoList(@Query("page") int page, @Query("pagesize") int pagesize,
                                 @Query("key") String key);

}
