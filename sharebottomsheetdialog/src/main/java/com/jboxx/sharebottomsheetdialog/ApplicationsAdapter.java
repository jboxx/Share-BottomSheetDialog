package com.jboxx.sharebottomsheetdialog;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Rifqi @jboxxpradhana
 */

class ApplicationsAdapter extends RecyclerView.Adapter<ApplicationsAdapter.ViewHolder> {

    final Context context;
    private final PackageManager packageManager;
    private final List<ResolveInfo> resolveInfoList;
    private final ViewHolder.ViewHolderCallbacks viewHolderCallbacks;

    public ApplicationsAdapter(final Context context, final List<ResolveInfo> resolveInfoList,
                               final ApplicationsAdapterCallback applicationsAdapterCallback) {
        this.context = context;
        this.resolveInfoList = resolveInfoList;
        this.packageManager = context.getPackageManager();
        this.viewHolderCallbacks = new ViewHolder.ViewHolderCallbacks() {
            @Override
            public void onClickApp(int indexApps) {
                if(applicationsAdapterCallback != null
                        && resolveInfoList != null && resolveInfoList.size() > 0){
                    applicationsAdapterCallback.onChooseApps(resolveInfoList.get(indexApps));
                }
            }
        };
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_share_apps, parent, false);
        return new ViewHolder(view, viewHolderCallbacks);
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindView(resolveInfoList.get(position), packageManager);
    }

    @Override public int getItemCount() {
        return resolveInfoList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgApps;
        TextView nameApps;

        public ViewHolder(final View itemView, final ViewHolderCallbacks viewHolderCallbacks){
            super(itemView);
            LinearLayout containerShareApp = itemView.findViewById(R.id.containerShareApp);
            imgApps = itemView.findViewById(R.id.img_apps);
            nameApps = itemView.findViewById(R.id.name_apps);
            containerShareApp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index = getAdapterPosition();
                    if(viewHolderCallbacks != null && index != RecyclerView.NO_POSITION){
                        viewHolderCallbacks.onClickApp(getAdapterPosition());
                    }
                }
            });
        }

        public void bindView(ResolveInfo resolveInfo, PackageManager packageManager){
            imgApps.setImageDrawable(resolveInfo.loadIcon(packageManager));
            nameApps.setText(resolveInfo.loadLabel(packageManager));
        }

        public interface ViewHolderCallbacks{
            void onClickApp(int indexApps);
        }
    }

    public interface ApplicationsAdapterCallback {
        void onChooseApps(ResolveInfo resolveInfo);
    }
}
