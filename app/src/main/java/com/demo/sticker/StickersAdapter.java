package com.demo.sticker;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.view.LayoutInflater;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;

public class StickersAdapter extends BaseAdapter {

    private final Context mContext;
    private final Sticker[] stickers;

    // 1
    public StickersAdapter(Context context, Sticker[] stickers) {
        this.mContext = context;
        this.stickers = stickers;
    }

    // 2
    @Override
    public int getCount() {
        return stickers.length;
    }

    // 3
    @Override
    public long getItemId(int position) {
        return 0;
    }

    // 4
    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Sticker sticker = stickers[position];

        // view holder pattern
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.linearlayout_sticker, null);

            final ImageView imageViewCoverArt = (ImageView)convertView.findViewById(R.id.imageview_cover_art);
            final ViewHolder viewHolder = new ViewHolder(imageViewCoverArt);
            convertView.setTag(viewHolder);
        }

        final ViewHolder viewHolder = (ViewHolder)convertView.getTag();
        Picasso.with(mContext).load(sticker.getImageResource()).into(viewHolder.imageViewCoverArt);

        if (position == HomeActivity.currentSticker) {
            convertView.setAlpha(0.5f);
        } else {
            convertView.setAlpha(1f);
        }

        return convertView;
    }

    // Your "view holder" that holds references to each subview
    private class ViewHolder {
        private final ImageView imageViewCoverArt;

        public ViewHolder(ImageView imageViewCoverArt) {
            this.imageViewCoverArt = imageViewCoverArt;
        }
    }

}