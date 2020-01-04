/*
 * Copyright (C) 2020-2021 sunilpaulmathew <sunil.kde@gmail.com>
 *
 * This file is part of Smart Flasher, which is a simple app aimed to make flashing
 * recovery zip files much easier. Significant amount of code for this app has been from
 * Kernel Adiutor by Willi Ye <williye97@gmail.com>.
 *
 * Smart Flasher is a free software: you can redistribute it and/or modify it under the terms
 * of the GNU General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 *
 * Smart Flasher is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Smart Flasher. If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.smartpack.smartflasher.utils;

import android.content.Context;
import android.content.pm.PackageManager;

import com.smartpack.smartflasher.BuildConfig;
import com.smartpack.smartflasher.R;
import com.smartpack.smartflasher.views.dialog.Dialog;

import java.io.File;

/**
 * Created by sunilpaulmathew <sunil.kde@gmail.com> on January 03, 2020
 */

public class UpdateCheck {

    private static final String PLAY_STORE = "com.android.vending";
    private static final String LATEST_VERSION = Utils.getInternalDataStorage() + "/.version";
    private static final String LATEST_VERSION_URL = "https://raw.githubusercontent.com/SmartPack/SmartFlasher/master/release/version?raw=true";
    private static final String DOWNLOAD_PAGE_URL = "https://github.com/SmartPack/SmartFlasher/tree/master/release";

    public static boolean isPlayStoreInstalled(Context context) {
        try {
            context.getPackageManager().getApplicationInfo(PLAY_STORE, 0);
            return true;
        } catch (PackageManager.NameNotFoundException ignored) {
            return false;
        }
    }

    private static void getVersionInfo() {
        File file = new File(Utils.getInternalDataStorage());
        if (file.exists() && file.isFile()) {
            file.delete();
        }
        file.mkdirs();
        Utils.downloadFile(LATEST_VERSION, LATEST_VERSION_URL);
    }

    private static int getLatestVersionNumber() {
        return Utils.strToInt(Utils.readFile(LATEST_VERSION));
    }

    public static boolean hasVersionInfo() {
        return Utils.existFile(LATEST_VERSION);
    }

    private static void updateAvailableDialog(Context context) {
        new Dialog(context)
                .setMessage(context.getString(R.string.update_available))
                .setCancelable(false)
                .setNegativeButton(context.getString(R.string.cancel), (dialog, id) -> {
                })
                .setPositiveButton(context.getString(R.string.get_it), (dialog, id) -> {
                    Utils.launchUrl(DOWNLOAD_PAGE_URL, context);
                })
                .show();
    }

    public static void autoUpdateCheck(Context context) {
        getVersionInfo();
        if (hasVersionInfo()) {
            if (BuildConfig.VERSION_CODE < getLatestVersionNumber()) {
                updateAvailableDialog(context);
            }
        } else {
            Utils.toast(context.getString(R.string.update_check_failed) + " " + context.getString(R.string.no_internet), context);
        }
    }

    public static void updateCheck(Context context) {
        getVersionInfo();
        if (hasVersionInfo()) {
            if (BuildConfig.VERSION_CODE < getLatestVersionNumber()) {
                updateAvailableDialog(context);
            } else {
                new Dialog(context)
                        .setMessage(context.getString(R.string.uptodate_message))
                        .setCancelable(false)
                        .setPositiveButton(context.getString(R.string.cancel), (dialog, id) -> {
                        })
                        .show();
            }
        } else {
            Utils.toast(context.getString(R.string.update_check_failed) + " " + context.getString(R.string.no_internet), context);
        }
    }

}