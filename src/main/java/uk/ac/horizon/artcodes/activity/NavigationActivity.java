package uk.ac.horizon.artcodes.activity;

import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import uk.ac.horizon.artcodes.R;
import uk.ac.horizon.artcodes.databinding.NavigationBinding;
import uk.ac.horizon.artcodes.fragment.ExperienceLibraryFragment;
import uk.ac.horizon.artcodes.fragment.ExperienceSelectFragment;
import uk.ac.horizon.artcodes.fragment.ExperienceStarFragment;

public class NavigationActivity extends AppCompatActivity implements
		NavigationView.OnNavigationItemSelectedListener
{
	private static final long DRAWER_CLOSE_DELAY_MS = 250;
	private static final String NAV_ITEM_ID = "navItemId";
	private final Handler drawerActionHandler = new Handler();
	private NavigationBinding binding;
	private ActionBarDrawerToggle drawerToggle;
	private int navigationIndex;

	@Override
	public void onBackPressed()
	{
		if (binding.drawer.isDrawerOpen(GravityCompat.START))
		{
			binding.drawer.closeDrawer(GravityCompat.START);
		}
		else
		{
			super.onBackPressed();
		}
	}

	@Override
	public void onConfigurationChanged(final Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);
		drawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onNavigationItemSelected(final MenuItem menuItem)
	{
		menuItem.setChecked(true);
		navigationIndex = menuItem.getItemId();
		binding.drawer.closeDrawer(GravityCompat.START);
		drawerActionHandler.postDelayed(new Runnable()
		{
			@Override
			public void run()
			{
				navigate(menuItem);
			}
		}, DRAWER_CLOSE_DELAY_MS);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item)
	{
		if (item.getItemId() == android.support.v7.appcompat.R.id.home)
		{
			return drawerToggle.onOptionsItemSelected(item);
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		binding = DataBindingUtil.setContentView(this, R.layout.navigation);

		setSupportActionBar(binding.toolbar);

		if (savedInstanceState == null)
		{
			navigationIndex = R.id.nav_home;
		}
		else
		{
			navigationIndex = savedInstanceState.getInt(NAV_ITEM_ID);
		}

		binding.navigation.setNavigationItemSelectedListener(this);
		MenuItem item = binding.navigation.getMenu().findItem(navigationIndex);
		item.setChecked(true);

		drawerToggle = new ActionBarDrawerToggle(this, binding.drawer, binding.toolbar, R.string.open, R.string.close);
		binding.drawer.setDrawerListener(drawerToggle);
		drawerToggle.syncState();

		navigate(item);
	}

	@Override
	protected void onSaveInstanceState(final Bundle outState)
	{
		super.onSaveInstanceState(outState);
		outState.putInt(NAV_ITEM_ID, navigationIndex);
	}

	private void navigate(MenuItem item)
	{
		Log.i("", "Open " + item.getItemId());
		binding.toolbar.setTitle(item.getTitle());
		switch (item.getItemId())
		{
			case R.id.nav_home:
				getSupportFragmentManager().beginTransaction().replace(R.id.content, new ExperienceSelectFragment()).commit();
				break;

			case R.id.nav_starred:
				getSupportFragmentManager().beginTransaction().replace(R.id.content, new ExperienceStarFragment()).commit();
				break;

			case R.id.nav_library:
				getSupportFragmentManager().beginTransaction().replace(R.id.content, new ExperienceLibraryFragment()).commit();
				break;
		}
	}
}