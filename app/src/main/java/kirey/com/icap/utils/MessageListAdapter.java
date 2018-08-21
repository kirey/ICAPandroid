package kirey.com.icap.utils;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.Spanned;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kirey.com.icap.R;
import kirey.com.icap.model.RecievedMessage;

/**
 * Created by kitanoskan on 09/06/2017.
 */

public class MessageListAdapter extends ArrayAdapter<RecievedMessage> {

    private Context context;
    private List<RecievedMessage> messages;
    private boolean deleteSection = false;
    SparseBooleanArray sparseBooleanArray;

    public MessageListAdapter(Context context, int resource, List<RecievedMessage> msgs) {
        super(context, resource);
        sparseBooleanArray = new SparseBooleanArray();
        this.messages = msgs;
        this.context = context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        ViewHolder viewHolder;

        if(convertView==null){
            convertView = inflater.inflate(R.layout.message_list_item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.msgTextView = (TextView) convertView.findViewById(R.id.msgBodyItemTextView);
            viewHolder.titleTextView = (TextView) convertView.findViewById(R.id.titleItemTextView);
            viewHolder.dateTextView = (TextView) convertView.findViewById(R.id.dateItemTextView);
            viewHolder.targetDateTextView = (TextView) convertView.findViewById(R.id.msgTargetItemTextView);
            viewHolder.idTextView = (TextView) convertView.findViewById(R.id.msgMessageIdItemTextView);
            viewHolder.locationTextView = (TextView) convertView.findViewById(R.id.msgLocationItemTextView);
            viewHolder.addressTextView = (TextView) convertView.findViewById(R.id.msgAddressItemTextView);
            viewHolder.alertLevelImageView = (ImageView)  convertView.findViewById(R.id.alertImageView);
            viewHolder.deleteSelectionLinLay = (LinearLayout) convertView.findViewById(R.id.deleteSelectionLinLay);
            viewHolder.messageBoxLinLay = (LinearLayout) convertView.findViewById(R.id.messageBoxLinLay);
            viewHolder.checkForDelete = (CheckBox) convertView.findViewById(R.id.checkForDelete);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Spanned result = Html.fromHtml(messages.get(position).getMessageText());
        viewHolder.msgTextView.setText(result);
        viewHolder.titleTextView.setText(messages.get(position).getMessageTitle());
        viewHolder.checkForDelete.setTag(position);
        viewHolder.checkForDelete.setChecked(sparseBooleanArray.get(position));

        Timestamp stamp = new Timestamp(messages.get(position).getMessageTimestamp());
        //Timestamp targetDate = new Timestamp(messages.get(position).getTargetTimestamp());
        String targetDate = messages.get(position).getTargetTimestamp();
        Date date = new Date(stamp.getTime());
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        //DateFormat df1 = new SimpleDateFormat("dd/MM/yyyy  HH");
        //df1.setTimeZone(TimeZone.getTimeZone("UTC"));

        viewHolder.dateTextView.setText(df.format(date));

        //viewHolder.targetDateTextView.setText("Alert starts at: " + targetDate);
        //viewHolder.idTextView.setText("Id: "+ String.valueOf(messages.get(position).getId()));

        /*if(messages.get(position).getLocation() != null)
            viewHolder.locationTextView.setText("Company location: "+messages.get(position).getLocation());*/

        /*f(messages.get(position).getAddress() != null)
            viewHolder.addressTextView.setText("Address: "+messages.get(position).getAddress());*/

        /*if(messages.get(position).getMessageTitle().toLowerCase().contains("red"))
            viewHolder.alertLevelImageView.setImageResource(R.drawable.alert_red);
        else  if(messages.get(position).getMessageTitle().toLowerCase().contains("orange"))
            viewHolder.alertLevelImageView.setImageResource(R.drawable.alert_orange);
        else  if(messages.get(position).getMessageTitle().toLowerCase().contains("yellow"))
            viewHolder.alertLevelImageView.setImageResource(R.drawable.alert_yellow);*/

        //za sad samo ova jedna
        Resources resources = context.getResources();
        final int resourceId = resources.getIdentifier(messages.get(position).getNotificationIcon(), "drawable",
                context.getPackageName());
        viewHolder.alertLevelImageView.setImageResource(resourceId);


        if(messages.get(position).isRead())
        {
            final int padding = viewHolder.messageBoxLinLay.getPaddingBottom();
            viewHolder.messageBoxLinLay.setBackgroundResource(R.drawable.list_item_backgound);
            viewHolder.messageBoxLinLay.setPadding(padding, padding, padding, padding);
        }
        else {
            final int padding = viewHolder.messageBoxLinLay.getPaddingBottom();
            viewHolder.messageBoxLinLay.setBackgroundResource(R.drawable.list_item_read_background);
            viewHolder.messageBoxLinLay.setPadding(padding, padding, padding, padding);
        }

        if(deleteSection)
            viewHolder.deleteSelectionLinLay.setVisibility(View.VISIBLE);
        else viewHolder.deleteSelectionLinLay.setVisibility(View.GONE);

        //checkbox delete
        viewHolder.checkForDelete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sparseBooleanArray.put((Integer) buttonView.getTag(), isChecked);
            }
        });

        return convertView;
    }

    @Override
    public int getPosition(@Nullable RecievedMessage item) {
        return super.getPosition(item);
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public RecievedMessage getItem(int position) {
        return messages.get(position);
    }

    public ArrayList<RecievedMessage> getCheckedItems() {
        ArrayList<RecievedMessage> tempArry = new ArrayList<RecievedMessage>();
        for(int i=0;i<messages.size();i++) {
            if(sparseBooleanArray.get(i)) {
                tempArry.add(messages.get(i));
            }
        }
        return tempArry;
    }

    public void uncheckAll(){
        sparseBooleanArray.clear();
    }

    public void refresh(List<RecievedMessage> items)
    {
        this.messages = items;
        notifyDataSetChanged();
    }

    public void showDeleteSection(boolean b){
        deleteSection = b;
    }

    ///////////VIEW HOLDER//////////////
    static class ViewHolder {
        int position;
        TextView msgTextView;
        TextView titleTextView;
        TextView dateTextView;
        TextView targetDateTextView ;
        TextView idTextView ;
        TextView locationTextView;
        TextView addressTextView;
        ImageView alertLevelImageView ;
        LinearLayout deleteSelectionLinLay ;
        LinearLayout messageBoxLinLay;
        CheckBox checkForDelete;

    }
}
