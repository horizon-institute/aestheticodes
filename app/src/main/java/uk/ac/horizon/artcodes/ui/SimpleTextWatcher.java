/*
 * Artcodes recognises a different marker scheme that allows the
 * creation of aesthetically pleasing, even beautiful, codes.
 * Copyright (C) 2013-2016  The University of Nottingham
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

package uk.ac.horizon.artcodes.ui;

import android.text.Editable;
import android.text.TextWatcher;

public abstract class SimpleTextWatcher implements TextWatcher
{
	@Override
	public void afterTextChanged(Editable editable)
	{
		onTextChanged(editable.toString());
	}

	@Override
	public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3)
	{

	}

	public abstract String getText();

	public abstract void onTextChanged(String value);

	@Override
	public void onTextChanged(CharSequence charSequence, int i, int i2, int i3)
	{
	}
}
