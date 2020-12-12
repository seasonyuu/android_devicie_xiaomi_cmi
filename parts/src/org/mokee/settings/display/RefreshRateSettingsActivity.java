/*
 * Copyright (C) 2020 The Mokee Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.mokee.settings.display;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import android.annotation.NonNull;
import android.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import org.mokee.settings.R;

public class RefreshRateSettingsActivity extends Activity {
    private RefreshRateAdapter mListAdapter = new RefreshRateAdapter();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh_rate_settings);

        ListView listView = findViewById(R.id.list_view);
        listView.setDivider(null);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                float targetRefreshRate = mListAdapter.getItem(position).value;
                setRefreshRate(targetRefreshRate);

                if (targetRefreshRate == 60f) {
                    mListAdapter.setRatio(0.5f);
                } else if (targetRefreshRate == 90f) {
                    mListAdapter.setRatio(0.75f);
                } else {
                    mListAdapter.setRatio(1f);
                }
                mListAdapter.selectedIndex = position;
                mListAdapter.notifyDataSetChanged();
            }
        });
        listView.setAdapter(mListAdapter);

        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updatePreferenceView();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


    private void updatePreferenceView() {
        String[] refreshRates = getResources().getStringArray(R.array.device_refresh_rates);
        String[] refreshRateValues = getResources().getStringArray(R.array.device_refresh_rates_val);
        String[] refreshRateSummaries = getResources().getStringArray(R.array.device_refresh_rates_summary);
        List<RefreshRate> list = new ArrayList<>();
        float currentRefreshRate = Settings.System.getFloat(getContentResolver(), Settings.System.MIN_REFRESH_RATE, 0f);
        for (int i = 0; i < refreshRates.length; i++) {
            RefreshRate refreshRate = new RefreshRate();
            try {
                refreshRate.value = Float.parseFloat(refreshRateValues[i]);
            } catch (Throwable t) {

            }
            if (refreshRate.value == currentRefreshRate) {
                mListAdapter.selectedIndex = i;
            }
            refreshRate.text = refreshRates[i];
            refreshRate.summary = refreshRateSummaries[i];
            list.add(refreshRate);
        }
        mListAdapter.setData(list);
        mListAdapter.notifyDataSetChanged();
        if (currentRefreshRate == 60f) {
            mListAdapter.setRatio(0.5f);
        } else if (currentRefreshRate == 90f) {
            mListAdapter.setRatio(0.75f);
        } else {
            mListAdapter.setRatio(1f);
        }
    }

    private void setRefreshRate(float value) {
        try {
            Settings.System.putFloat(getContentResolver(), Settings.System.MIN_REFRESH_RATE, value);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private static class RefreshRate {
        public float value;
        public String text;
        public String summary;
    }

    private static class RefreshRateAdapter extends BaseAdapter {
        private List<RefreshRate> mData = new ArrayList<>();
        private float ratio = 1f;
        public int selectedIndex = 0;
        private final int[] colors = new int[]{
                Color.parseColor("#03A9F4"),
                Color.parseColor("#009688"),
                Color.parseColor("#8BC34A"),
                Color.parseColor("#FFEB3B"),
                Color.parseColor("#FF9800"),
                Color.parseColor("#795548")
        };

        public RefreshRateAdapter() {
        }

        public void setData(List<RefreshRate> data) {
            mData = data;
        }

        public void setRatio(float ratio) {
            this.ratio = ratio;
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public RefreshRate getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            Context context = parent.getContext();
            RefreshRate refreshRate = mData.get(position);
            RefreshRateViewHolder viewHolder;
            if (convertView == null) {
                view = LayoutInflater.from(context).inflate(R.layout.item_refresh_rate, parent, false);
                viewHolder = new RefreshRateViewHolder(view);
                view.setTag(viewHolder);
            } else {
                view = convertView;
                viewHolder = (RefreshRateViewHolder) view.getTag();
            }
            view.findViewById(R.id.item_content).setSelected(selectedIndex == position);
            viewHolder.setText(refreshRate.text);
            viewHolder.setSummary(refreshRate.summary);
            viewHolder.setFrameRate(refreshRate.value);
            viewHolder.setBallColor(colors[position % colors.length]);
            return view;
        }
    }

    private static class RefreshRateViewHolder {
        private TextView tvValue;
        private TextView tvSummary;
        private FrameRateAnimationView animationView;

        public RefreshRateViewHolder(View itemView) {
            tvValue = itemView.findViewById(R.id.text);
            tvSummary = itemView.findViewById(R.id.summary);
            animationView = itemView.findViewById(R.id.animation_view);
        }

        public void setFrameRate(float frameRate) {
            animationView.setFrameRate(frameRate);
            if (!animationView.isAnimating()) {
                animationView.startFrameRateAnimation();
            }
        }

        public void setValue(String value) {
            try {
                float frameRate = Float.parseFloat(value);
                animationView.setFrameRate(frameRate);
                if (!animationView.isAnimating()) {
                    animationView.startFrameRateAnimation();
                }
            } catch (Throwable t) {

            }
        }

        public void setText(String text) {
            tvValue.setText(text);
        }

        public void setSummary(String summary) {
            tvSummary.setText(summary);
        }

        public void setBallColor(int color) {
            animationView.setColor(color);
        }
    }
}
