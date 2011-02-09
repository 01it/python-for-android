/*
 * Copyright (C) 2009 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.googlecode.pythonforandroid;

import android.content.Context;

import com.googlecode.android_scripting.interpreter.InterpreterConstants;
import com.googlecode.android_scripting.interpreter.InterpreterUtils;
import com.googlecode.android_scripting.interpreter.Sl4aHostedInterpreter;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class PythonDescriptor extends Sl4aHostedInterpreter {

  private static final String PYTHON_BIN = "bin/python";
  private static final String ENV_HOME = "PYTHONHOME";
  private static final String ENV_PATH = "PYTHONPATH";
  private static final String ENV_TEMP = "TEMP";
  private static final String ENV_LD = "LD_LIBRARY_PATH";
  private static final String BASE_URL = "http://python-for-android.googlecode.com/";
  private static final int LATEST_VERSION = 12;

  @Override
  public String getBaseInstallUrl() {
    return BASE_URL + "/files/";
  }

  public String getExtension() {
    return ".py";
  }

  public String getName() {
    return "python";
  }

  public String getNiceName() {
    return "Python 2.6.2";
  }

  public boolean hasInterpreterArchive() {
    return true;
  }

  public boolean hasExtrasArchive() {
    return true;
  }

  public boolean hasScriptsArchive() {
    return true;
  }

  private int __resolve_version(String what) {
    // try resolving latest version
    URL url;
    try {
      url = new URL(BASE_URL + "/hg/python-build/LATEST_VERSION_" + what.toUpperCase());
      BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
      return Integer.parseInt(reader.readLine().substring(1));
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return LATEST_VERSION;
    }

  }

  public int getVersion() {
    return __resolve_version("");
  }

  @Override
  public int getExtrasVersion() {
    return __resolve_version("extras");
  }

  @Override
  public int getScriptsVersion() {
    return __resolve_version("scripts");
  }

  @Override
  public File getBinary(Context context) {
    return new File(getExtrasPath(context), PYTHON_BIN);
  }

  private String getExtrasRoot() {
    return InterpreterConstants.SDCARD_ROOT + getClass().getPackage().getName()
        + InterpreterConstants.INTERPRETER_EXTRAS_ROOT;
  }

  private String getHome(Context context) {
    File file = InterpreterUtils.getInterpreterRoot(context, getName());
    return file.getAbsolutePath();
  }

  public String getExtras() {
    File file = new File(getExtrasRoot(), getName());
    return file.getAbsolutePath();
  }

  private String getTemp() {
    File tmp = new File(getExtrasRoot(), getName() + "/tmp");
    if (!tmp.isDirectory()) {
      tmp.mkdir();
    }
    return tmp.getAbsolutePath();
  }

  @Override
  public Map<String, String> getEnvironmentVariables(Context context) {
    Map<String, String> values = new HashMap<String, String>();
    values.put(ENV_HOME, getHome(context));
    values.put(ENV_LD, getHome(context) + "/lib");
    values.put(ENV_PATH, getExtras() + ":" + getHome(context) + "/lib/python2.6/lib-dynload" + ":"
        + getHome(context) + "/lib/python2.6");
    values.put(ENV_TEMP, getTemp());
    return values;
  }
}
