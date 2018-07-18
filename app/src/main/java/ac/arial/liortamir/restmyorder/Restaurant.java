package ac.arial.liortamir.restmyorder;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;


import java.util.HashMap;
import java.util.Map;

import ac.arial.liortamir.restmyorder.entity.Role;
import ac.arial.liortamir.restmyorder.entity.User;
import ac.arial.liortamir.restmyorder.fragment.DBAFragment;
import ac.arial.liortamir.restmyorder.fragment.HistoryFragment;
import ac.arial.liortamir.restmyorder.fragment.InventoryFragment;
import ac.arial.liortamir.restmyorder.fragment.KitchenFragment;
import ac.arial.liortamir.restmyorder.fragment.OrderFragment;
import ac.arial.liortamir.restmyorder.fragment.UserFragment;
import ac.arial.liortamir.restmyorder.persistence.DataHandler;

public class Restaurant extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
//    private SectionsPagerAdapter mSectionsPagerAdapter;
    private DemoCollectionPagerAdapter statePageAdapter;


    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private TabLayout tabLayout;

    public static final int FRAGMENT_ID_ORDER = 1;
    public static final int FRAGMENT_ID_KITCHEN = 2;
    public static final int FRAGMENT_ID_DISH = 3;
    public static final int FRAGMENT_ID_HISTORY = 4;
    public static final int FRAGMENT_ID_DBA = 5;
    public static final int FRAGMENT_ID_USER = 6;

    public class RoleAuthorization{

        private int[] pageNumToPage;

        public RoleAuthorization(int ... pageIds) {
            pageNumToPage = pageIds;
        }

        public int getPageCount(){return pageNumToPage.length;}
        public int getPageId(int pageId){
            return pageNumToPage[pageId];
        }
    }

    private Map<Role, RoleAuthorization> roleMap = new HashMap<>();
    {
        roleMap.put(new Role(1, "Waiter"),
                new RoleAuthorization(FRAGMENT_ID_ORDER,
                        FRAGMENT_ID_HISTORY));
        roleMap.put(new Role(1, "Chef"),
                new RoleAuthorization(FRAGMENT_ID_KITCHEN));
        roleMap.put(new Role(1, "Manager"),
                new RoleAuthorization(FRAGMENT_ID_HISTORY,
                        FRAGMENT_ID_DISH,
                        FRAGMENT_ID_USER));
        roleMap.put(new Role(1, "Developer"),
                new RoleAuthorization(FRAGMENT_ID_ORDER,
                        FRAGMENT_ID_KITCHEN,
                        FRAGMENT_ID_DISH,
                        FRAGMENT_ID_HISTORY,
                        FRAGMENT_ID_DBA,
                        FRAGMENT_ID_USER));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        getIntent().setExtrasClassLoader(Role.class.getClassLoader());
        User activeUser = getIntent().getParcelableExtra("user");
        DataHandler dataHandler = DataHandler.getInstance(this);
        dataHandler.setActiveUser(activeUser);

        setTitle(getResources().getString(R.string.app_name) + " - " + activeUser.getFullName());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the primary sections of the activity.
//        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        statePageAdapter = new DemoCollectionPagerAdapter(getSupportFragmentManager(), roleMap.get(activeUser.getRole()));

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
//        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setAdapter(statePageAdapter);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            Fragment fragment = null;
            switch(position)
            {
                case 0:
                    fragment = new OrderFragment();
                    break;
                case 1:
                    fragment = new KitchenFragment();
                    break;
                case 2:
                    fragment = new InventoryFragment();
                    break;
                case 3:
                    fragment = new HistoryFragment();
                    break;
                case 4:
                    fragment = new DBAFragment();
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {

            return 4;
        }
    }

    public class DemoCollectionPagerAdapter extends FragmentStatePagerAdapter {
        private final static int PAGE_COUNT = 6;
        RoleAuthorization roleAuth;

        public DemoCollectionPagerAdapter(FragmentManager fm, RoleAuthorization roleAuth) {
            super(fm);
            this.roleAuth = roleAuth;
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment = null;
            int pageId = roleAuth.getPageId(i);
            switch(pageId)
            {
                case 1:
                    fragment = new OrderFragment();
                    break;
                case 2:
                    fragment = new KitchenFragment();
                    break;
                case 3:
                    fragment = new InventoryFragment();
                    break;
                case 4:
                    fragment = new HistoryFragment();
                    break;
                case 5:
                    fragment = new DBAFragment();
                    break;
                case 6:
                    fragment = new UserFragment();
                    break;
            }

            Bundle args = new Bundle();
//            args.putInt(PlaceholderFragment.ARG_SECTION_NUMBER, i+1);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return roleAuth.getPageCount();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            int pageId = roleAuth.getPageId(position);
            switch(pageId){
                case Restaurant.FRAGMENT_ID_ORDER:
                    return getResources().getString(R.string.orderFrag_title);
                case Restaurant.FRAGMENT_ID_KITCHEN:
                    return getResources().getString(R.string.kitchenFrag_title);
                case Restaurant.FRAGMENT_ID_DISH:
                    return getResources().getString(R.string.dishFrag_title);
                case Restaurant.FRAGMENT_ID_HISTORY:
                    return getResources().getString(R.string.historyFrag_title);
                case Restaurant.FRAGMENT_ID_DBA:
                    return getResources().getString(R.string.sqliteFrag_title);
                case Restaurant.FRAGMENT_ID_USER:
                    return getResources().getString(R.string.userFrag_title);
            }
            return "ERROR " + (position);
        }
    }



}
