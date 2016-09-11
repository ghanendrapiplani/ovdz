package com.o2cinemas.o2vidz;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.o2cinemas.adapters.ViewPager_Adapter;
import com.o2cinemas.constants.Constants;
import com.o2cinemas.fragment.Category_Fragment;
import com.o2cinemas.fragment.Gp3_Fragment;
import com.o2cinemas.fragment.GroupDetailFragment;
import com.o2cinemas.fragment.Mp4_Fragment;
import com.o2cinemas.fragment.TopDownloads_Fragment;
import com.o2cinemas.fragment.TvShow_Fragment;
import com.o2cinemas.navigation.About;
import com.o2cinemas.navigation.NavigationAdapter;

import java.util.NoSuchElementException;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private AutoCompleteTextView searchView;
    private ImageView logo,sort_icon;
    private DrawerLayout drawerLayout;
    private ListView drawerListView;

    private NavigationAdapter navigationAdapter;
    private ActionBarDrawerToggle toggle;
    private Context context;
    private FrameLayout frameLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);

        setContentView(R.layout.activity_main);

        final View toolbar_view = findViewById(R.id.appbar);
        toolbar = (Toolbar) toolbar_view.findViewById(R.id.toolbar_home);

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.menu_);
        searchView = (AutoCompleteTextView) toolbar_view.findViewById(R.id.searchIcon);
        searchView.setVisibility(View.GONE);

        logo = (ImageView) toolbar_view.findViewById(R.id.logo);
        logo.setVisibility(View.VISIBLE);
        sort_icon= (ImageView) toolbar_view.findViewById(R.id.sort_icon);
        sort_icon.setVisibility(View.GONE);
        frameLayout = (FrameLayout) findViewById(R.id.frame_container);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        viewPager = (ViewPager) findViewById(R.id.viewPagerTabLayout);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager.setOffscreenPageLimit(5);
        ViewPager_Adapter pagerAdapter = new ViewPager_Adapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(pagerAdapter.getTabView(i));
        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


            }

            @Override
            public void onPageSelected(int position) {
                ViewPager_Adapter adapter = (ViewPager_Adapter) viewPager.getAdapter();
                switch (position) {
                    case 0:
                        logo.setVisibility(View.VISIBLE);
                        searchView.setVisibility(View.GONE);
                        break;
                    case 1:
                        logo.setVisibility(View.GONE);
                        searchView.setVisibility(View.VISIBLE);
                        ((Mp4_Fragment)adapter.getItem(position)).getData();
                        break;
                    case 2:
                        logo.setVisibility(View.GONE);
                        searchView.setVisibility(View.VISIBLE);
                        ((Gp3_Fragment)adapter.getItem(position)).getData();
                        break;
                    case 3:
                        logo.setVisibility(View.GONE);
                        searchView.setVisibility(View.VISIBLE);
                        ((TvShow_Fragment)adapter.getItem(position)).getData();
                        break;
                    case 4:
                        logo.setVisibility(View.GONE);
                        searchView.setVisibility(View.VISIBLE);
                        ((TopDownloads_Fragment)adapter.getItem(position)).getData();
                        break;
                }
                removeFrameLayout();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        drawerListView = (ListView) findViewById(R.id.drawer_list);

        navigationAdapter = new NavigationAdapter(this);
        drawerListView.setAdapter(navigationAdapter);


        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }


            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        drawerLayout.addDrawerListener(toggle);

        drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedItemPosition(position);
            }

            private void selectedItemPosition(int position) {
                drawerLayout.closeDrawers();

                switch (position) {
                    case 0:
                        frameLayout.setVisibility(View.VISIBLE);
                        getSupportFragmentManager().beginTransaction().add(R.id.frame_container,
                                new Category_Fragment()).commit();
                        break;
                    case 1:

                        break;
                    case 2:

                        break;
                    case 3:
                        Intent intentSetting = new Intent(MainActivity.this, SettingActivity.class);
                        startActivity(intentSetting);

                        break;
                    case 4:
//                        getSupportFragmentManager().beginTransaction().replace(R.id.drawer_container, new FeedBack()).commit();
//                        break;
                    case 5:

                        break;
                    case 6:

                        break;
                    case 7:

                        break;
                    case 8:
                        Intent intent8 = new Intent(getApplicationContext(), About.class);
                        startActivity(intent8);
                        break;

                    case 9:
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        break;
                    default:
                        viewPager.setVisibility(View.VISIBLE);

                }
            }
        });
    }



    private void scaleImage(ImageView view) throws NoSuchElementException {
        // Get bitmap from the the ImageView.
        Bitmap bitmap = null;

        try {
            Drawable drawing = view.getDrawable();
            bitmap = ((BitmapDrawable) drawing).getBitmap();
        } catch (NullPointerException e) {
            throw new NoSuchElementException("No drawable on given view");
        } catch (ClassCastException e) {
            // Check bitmap is Ion drawable
            e.printStackTrace();
        }

        // Get current dimensions AND the desired bounding box
        int width = 0;

        try {
            width = bitmap.getWidth();
        } catch (NullPointerException e) {
            throw new NoSuchElementException("Can't find bitmap on given view/drawable");
        }

        int height = bitmap.getHeight();
        int bounding = dpToPx(250);
        Log.i("Test", "original width = " + Integer.toString(width));
        Log.i("Test", "original height = " + Integer.toString(height));
        Log.i("Test", "bounding = " + Integer.toString(bounding));

        // Determine how much to scale: the dimension requiring less scaling is
        // closer to the its side. This way the image always stays inside your
        // bounding box AND either x/y axis touches it.
        float xScale = ((float) bounding) / width;
        float yScale = ((float) bounding) / height;
        float scale = (xScale <= yScale) ? xScale : yScale;
        Log.i("Test", "xScale = " + Float.toString(xScale));
        Log.i("Test", "yScale = " + Float.toString(yScale));
        Log.i("Test", "scale = " + Float.toString(scale));

        // Create a matrix for the scaling and add the scaling data
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        // Create a new bitmap and convert it to a format understood by the ImageView
        Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        width = scaledBitmap.getWidth(); // re-use
        height = scaledBitmap.getHeight(); // re-use
        BitmapDrawable result = new BitmapDrawable(scaledBitmap);
        Log.i("Test", "scaled width = " + Integer.toString(width));
        Log.i("Test", "scaled height = " + Integer.toString(height));

        // Apply the scaled bitmap
        view.setImageDrawable(result);

        // Now change ImageView's dimensions to match the scaled image
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
        params.width = width;
        params.height = height;
        view.setLayoutParams(params);

        Log.i("Test", "done");
    }

    private int dpToPx(int dp) {
        float density = getApplicationContext().getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getCurrentPositionofFragment(int adapterPosition) {
        viewPager.setCurrentItem(adapterPosition + 1);
    }


    public interface Back {
        public void addFragment(Fragment fragment, String tag);
    }

    public void goToGroupDetailsFragment(String groupType, String movieType) {
        frameLayout.setVisibility(View.VISIBLE);
        Fragment fragment = new GroupDetailFragment();
        Bundle args = new Bundle();
        args.putString(Constants.GROUP_TYPE_KEY, groupType);
        args.putString(Constants.MOVIE_TYPE_KEY, movieType);

        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().add(R.id.frame_container,
                fragment).commit();
    }

    @Override
    public void onBackPressed() {
        if (frameLayout.getChildCount() > 0) {
            removeFrameLayout();
        } else {
            super.onBackPressed();
        }
    }

    private void removeFrameLayout() {
        if (frameLayout.getChildCount() > 0) {
            frameLayout.removeAllViews();
        }
        frameLayout.setVisibility(View.GONE);
    }

    public int getCurrentTabPosition() {
        if (viewPager != null) {
            return viewPager.getCurrentItem();
        }
        return 0;
    }
}



