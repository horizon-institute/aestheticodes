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

package uk.ac.horizon.artcodes.process;

import android.content.Context;

import java.util.Map;

import uk.ac.horizon.artcodes.camera.CameraFocusControl;
import uk.ac.horizon.artcodes.detect.handler.MarkerDetectionHandler;
import uk.ac.horizon.artcodes.model.Experience;

public interface ImageProcessorFactory
{
	String getName();
	ImageProcessor create(Context context, Experience experience, MarkerDetectionHandler handler, CameraFocusControl cameraFocusControl, Map<String, String> args);
}
