package org.guog.gh.mretrofittest.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import org.guog.gh.mretrofittest.R;
import org.guog.gh.mretrofittest.fragment.JokeFragment;
import org.guog.gh.mretrofittest.fragment.PicFragment;

public class MainActivity extends AppCompatActivity {

    boolean isJoke = true;
    FragmentManager manager;
    JokeFragment jokeFragment = new JokeFragment();
    PicFragment picFragment = new PicFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity);
        //初始化先添加一个Fragment
        //没有把数据持久化，所以每次进入Activity都要重新从网络上加载一遍
        manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(R.id.activity_container, jokeFragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menus_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Fragment被hide仍然是active状态，不会执行onPause以及之后的操作；
     * Activity会保留所有Fragment的状态，所以要慎用，这样的开销是比较大的
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.mainMenu_joke:
                if (isJoke) break;
                FragmentTransaction ft_joke = manager.beginTransaction();
                if (picFragment.isAdded()) {
                    ft_joke.hide(picFragment).show(jokeFragment).commit();
                }
                isJoke = true;
                break;
            case R.id.mainMenu_pic:
                if (!isJoke) break;
                FragmentTransaction ft_pic = manager.beginTransaction();
                if (picFragment.isAdded()) {
                    ft_pic.hide(jokeFragment).show(picFragment).commit();
                } else {
                    ft_pic.hide(jokeFragment).add(R.id.activity_container, picFragment).commit();
                }
                isJoke = false;
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
