/*
 * Artcodes recognises a different marker scheme that allows the
 * creation of aesthetically pleasing, even beautiful, codes.
 * Copyright (C) 2013-2015  The University of Nottingham
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package uk.ac.horizon.artcodes.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.util.Calendar;
import java.util.List;

import uk.ac.horizon.artcodes.GoogleAnalytics;
import uk.ac.horizon.artcodes.R;
import uk.ac.horizon.artcodes.databinding.AvailabilityEditBinding;
import uk.ac.horizon.artcodes.databinding.ExperienceEditAvailabilitiesBinding;
import uk.ac.horizon.artcodes.model.Availability;
import uk.ac.horizon.artcodes.ui.Bindings;

public class ExperienceEditAvailabilityFragment extends ExperienceEditFragment
{
	private class AvailabilityAdapter extends RecyclerView.Adapter<AvailabilityAdapter.ViewHolder>
	{
		public class ViewHolder extends RecyclerView.ViewHolder
		{
			private AvailabilityEditBinding binding;

			public ViewHolder(AvailabilityEditBinding binding)
			{
				super(binding.getRoot());
				this.binding = binding;
			}
		}

		private List<Availability> availabilities;

		public AvailabilityAdapter(List<Availability> availabilities)
		{
			this.availabilities = availabilities;
		}

		@Override
		public int getItemCount()
		{
			return availabilities.size();
		}

		@Override
		public void onBindViewHolder(final ViewHolder holder, int position)
		{
			final Availability availability = availabilities.get(position);
			holder.binding.setAvailability(availability);
			if (availability.getEnd() == null)
			{
				if (availability.getStart() == null)
				{
					holder.binding.availabilityDesc.setText(R.string.available_always);
				}
				else
				{
					holder.binding.availabilityDesc.setText(getString(R.string.available_from, Bindings.getDate(availability.getStart())));
				}
			}
			else
			{
				if (availability.getStart() == null)
				{
					holder.binding.availabilityDesc.setText(getString(R.string.available_to, Bindings.getDate(availability.getEnd())));
				}
				else
				{

					holder.binding.availabilityDesc.setText(getString(R.string.available, Bindings.getDate(availability.getStart(), availability.getEnd())));
				}
			}

			holder.binding.availabilityStart.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					selectDate(availability.getStart(), new DatePickerDialog.OnDateSetListener()
					{
						@Override
						public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
						{
							final Calendar calendar = Calendar.getInstance();
							calendar.set(year, monthOfYear, dayOfMonth);
							availability.setStart(calendar.getTimeInMillis());
						}
					});
				}
			});
			holder.binding.availabilityEnd.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					selectDate(availability.getEnd(), new DatePickerDialog.OnDateSetListener()
					{
						@Override
						public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
						{
							final Calendar calendar = Calendar.getInstance();
							calendar.set(year, monthOfYear, dayOfMonth);
							availability.setEnd(calendar.getTimeInMillis());
						}
					});
				}
			});
			holder.binding.toggleDates.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					holder.binding.toggleDates.setVisibility(View.GONE);
					holder.binding.dateExpand.setVisibility(View.VISIBLE);
				}
			});
			holder.binding.availabilityLocation.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					try
					{
						PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
						Intent intent = builder.build(getActivity());
						intent.putExtra("availIndex", getExperience().getAvailabilities().indexOf(availability));
						startActivityForResult(intent, PLACE_PICKER_REQUEST);
					}
					catch (Exception e)
					{
						GoogleAnalytics.trackException(e);
					}
				}
			});
			holder.binding.delete.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					delete(availability);
				}
			});
		}

		@Override
		public AvailabilityAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
		{
			return new ViewHolder(AvailabilityEditBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
		}
	}

	private static final int PLACE_PICKER_REQUEST = 119;
	private ExperienceEditAvailabilitiesBinding binding;

	@Override
	public int getTitleResource()
	{
		return R.string.fragment_availability;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == PLACE_PICKER_REQUEST)
		{
			if (resultCode == Activity.RESULT_OK)
			{
				final Place place = PlacePicker.getPlace(getActivity(), data);
				final int index = data.getIntExtra("availIndex", 0);
				if (index >= 0)
				{
					final Availability availability = getExperience().getAvailabilities().get(index);
					availability.setName(place.getName().toString());
					availability.setAddress(place.getAddress().toString());
					availability.setLat(place.getLatLng().latitude);
					availability.setLon(place.getLatLng().longitude);
				}
			}
		}
	}

	@Override
	public boolean displayAddFAB()
	{
		return true;
	}

	@Override
	public void add()
	{
		if (getExperience().getAvailabilities().add(new Availability()))
		{
			updateAvailabilities();
			binding.list.getAdapter().notifyItemInserted(getExperience().getAvailabilities().size() - 1);
		}
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		binding = ExperienceEditAvailabilitiesBinding.inflate(inflater, container, false);
		binding.list.setLayoutManager(new LinearLayoutManager(getActivity()));

		ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT)
		{
			@Override
			public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder1)
			{
				return false;
			}

			@Override
			public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir)
			{
				int position = viewHolder.getAdapterPosition();
				delete(getExperience().getAvailabilities().get(position));
			}
		};
		ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
		itemTouchHelper.attachToRecyclerView(binding.list);

		return binding.getRoot();
	}

	@Override
	public void onResume()
	{
		super.onResume();
		binding.list.setAdapter(new AvailabilityAdapter(getExperience().getAvailabilities()));
		updateAvailabilities();
	}

	private void delete(final Availability availability)
	{
		final int index = getExperience().getAvailabilities().indexOf(availability);
		getExperience().getAvailabilities().remove(availability);
		binding.list.getAdapter().notifyItemRemoved(index);
		updateAvailabilities();
		Snackbar.make(binding.getRoot(), R.string.action_deleted, Snackbar.LENGTH_LONG)
				.setAction(R.string.action_delete_undo, new View.OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						getExperience().getAvailabilities().add(index, availability);
						binding.list.getAdapter().notifyItemInserted(index);
						updateAvailabilities();
					}
				})
				.setActionTextColor(ContextCompat.getColor(getActivity(), R.color.apptheme_accent_light))
				.show();
	}

	private void updateAvailabilities()
	{
		if (getExperience().getAvailabilities().isEmpty())
		{
			binding.list.setVisibility(View.GONE);
			binding.emptyView.setVisibility(View.VISIBLE);
		}
		else
		{
			binding.list.setVisibility(View.VISIBLE);
			binding.emptyView.setVisibility(View.GONE);
		}
	}

	private void selectDate(Long timestamp, DatePickerDialog.OnDateSetListener listener)
	{
		final Calendar calendar = Calendar.getInstance();
		if (timestamp != null)
		{
			calendar.setTimeInMillis(timestamp);
		}
		else
		{
			calendar.setTimeInMillis(System.currentTimeMillis());
		}
		int mYear = calendar.get(Calendar.YEAR);
		int mMonth = calendar.get(Calendar.MONTH);
		int mDay = calendar.get(Calendar.DAY_OF_MONTH);

		DatePickerDialog dialog = new DatePickerDialog(getActivity(), listener, mYear, mMonth, mDay);
		dialog.show();
	}
}
