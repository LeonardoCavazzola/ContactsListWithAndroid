package com.example.androidt2.rv;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.example.androidt2.MainActivity;
import com.example.androidt2.R;
import com.example.androidt2.dao.ContactDao;

public class DeleteContactTouchHelperCallBack extends ItemTouchHelper.SimpleCallback {

    Context context;
    Drawable background;
    Drawable xMark;
    int xMarkMargin = 30;
    boolean initiated;

    public DeleteContactTouchHelperCallBack(Context context, int dragDirs, int swipeDirs) {
        super(dragDirs, swipeDirs);
        this.context = context;
    }

    private void init() {
        background = new ColorDrawable(Color.RED);
        xMark = ContextCompat.getDrawable(context, R.drawable.ic_clear_24);
        xMark.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        initiated = true;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public int getSwipeDirs(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        return super.getSwipeDirs(recyclerView, viewHolder);
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int swipeDir) {

        new AlertDialog.Builder(context)
                .setMessage("Tem certeza que deseja deletar esse contato?")
                .setTitle("Deletar Contato")
                .setPositiveButton("Sim", (dialog, id) -> {
                    int contactId = (int) viewHolder.itemView.getTag();

                    ContactDao contactDao = new ContactDao(context);
                    contactDao.deleteContactById(contactId);
                    ((MainActivity) context).updateRV();
                })
                .setNegativeButton("Não", (dialog, id) -> {
                    ((MainActivity) context).updateRV();
                })
                .create()
                .show();
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View itemView = viewHolder.itemView;

        if (viewHolder.getAdapterPosition() == -1) {
            return;
        }

        if (!initiated) {
            init();
        }

        // draw background
        background.setBounds(itemView.getLeft() + (int) dX, itemView.getTop(), itemView.getLeft(), itemView.getBottom());
        background.draw(c);

        // draw x mark
        int itemHeight = itemView.getBottom() - itemView.getTop();
        int intrinsicWidth = xMark.getIntrinsicWidth();
        int intrinsicHeight = xMark.getIntrinsicWidth();

        int xMarkLeft = xMarkMargin;
        int xMarkRight = intrinsicWidth + xMarkMargin;
        int xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
        int xMarkBottom = xMarkTop + intrinsicHeight;
        xMark.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);

        xMark.draw(c);

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
}
