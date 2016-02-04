/*
 * Aestheticodes recognises a different marker scheme that allows the
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

package uk.ac.horizon.aestheticodes.properties.bindings;

import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class ViewBindingFactory
{
	public static ViewBinding createBinding(View view)
	{
		if(view instanceof EditText)
		{
			return new EditTextBinding(view);
		}
		else if(view instanceof SwitchCompat)
		{
			return new SwitchBinding(view);
		}
		else if(view instanceof ImageView)
		{
			return new ImageBinding(view);
		}
		else if(view instanceof SeekBar)
		{
			return new SliderBinding(view);
		}
		else if(view instanceof TextView)
		{
			return new TextBinding(view);
		}
		return null;
	}
}