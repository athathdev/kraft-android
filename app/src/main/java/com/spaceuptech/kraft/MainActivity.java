package com.spaceuptech.kraft;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.spaceuptech.kraft.events.EventsFragment;
import com.spaceuptech.kraft.posts.HomeFragment;
import com.spaceuptech.kraft.posts.PostActivity;
import com.spaceuptech.kraft.profile.ProfileFragment;

public class MainActivity extends AppCompatActivity {
    public static String[] colorList = {"#1976D2", "#3F51B5", "#2196F3", "#E91E63", "#009688","#4CAF50", "#FFC107", "#FF5722", "#795548", "#607D8B" };
    TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = (TabLayout) findViewById(R.id.tabLayoutMain);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPagerMain);

        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());

        Pager adapter = new Pager(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        setUpTabIcons();

    }
    private void setUpTabIcons(){
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_events);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_person);
    }

    public void createPost(View view) {
        Intent intent = new Intent(this, PostActivity.class);
        startActivity(intent);
    }
}
class Pager extends FragmentStatePagerAdapter {
    private int tabCount;

    Pager(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount= tabCount;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new HomeFragment();
            case 1:
                return new EventsFragment();
            case 2 :
                return new ProfileFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }

}
