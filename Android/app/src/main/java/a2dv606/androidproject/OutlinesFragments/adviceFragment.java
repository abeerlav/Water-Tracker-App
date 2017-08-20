package a2dv606.androidproject.OutlinesFragments;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import a2dv606.androidproject.R;


public class adviceFragment extends Fragment {

    final static String Review="review";
    final static String image="image";
     ViewGroup rootView;
     TextView tv;





    public static adviceFragment create(int review, int img) {

        adviceFragment newFragment = new adviceFragment();
        Bundle args = new Bundle();
        args.putInt(image, img);
        args.putInt(Review, review);


        newFragment.setArguments(args);
        return newFragment;
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        rootView = (ViewGroup) inflater.inflate(R.layout.activity_fragment, container, false);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();


        int bReview=0,bImage=0 ;
        Bundle args = getArguments();
        if (args != null) {

            bReview = args.getInt(Review);
            bImage = args.getInt(image);

        }
        Button share = (Button)rootView.findViewById(R.id.shareBtn);
        share.setOnClickListener(new  shareWith());
        tv= (TextView)rootView.findViewById(R.id.review_view);
        ImageView iv= (ImageView)rootView.findViewById(R.id.image_view);
        tv.setText(bReview);
        iv.setImageResource(bImage);

    }
    private class  shareWith implements View.OnClickListener {
        @Override
        public void onClick(View arg0) {

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, tv.getText());
            sendIntent.setType("text/plain");
            startActivity(sendIntent);


        }}

}

