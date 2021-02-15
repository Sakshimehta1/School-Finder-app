package com.example.user_school;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.anwarshahriar.calligrapher.Calligrapher;


public class School_page extends AppCompatActivity {

    String school_uid;
    String user_id;
    TextView textView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference documentReference_school;
    DocumentReference documentReference_user;
    ImageSlider imageSlider;
    data_schools data;
    TextView admopen,name,location,fees,grfrom,grto,board,schooltype,strength,establish;
    ChipGroup fac_group,doc_group;
    List<String> photosList;
    List<String> facilitieschipslist;
    List<String> docchipslist;
    List<SlideModel> slideModelList;
    ImageView noimg,fb,insta,ytb,linkin,web;
    RatingBar ratingBar;
    Button submitrating;
    TextView tvshowrate;
    float alreadySchoolRating;
    float finalrate;
    float previousRatingbyUser;
    FirebaseAuth mauth;
    FirebaseUser currentuser;
    boolean rated_or_not;
    String message;
    int count;
    Button submitreview;
    TextInputLayout enterReview;
    TextView showreview;
    LinearLayout layout_show_review;
    RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_page);
        admopen=findViewById(R.id.admopen);
        name=findViewById(R.id.name);
        location=findViewById(R.id.location);
        fees=findViewById(R.id.fees);
        grfrom=findViewById(R.id.grfrom);
        grto=findViewById(R.id.grto);
        board=findViewById(R.id.tvboard);
        schooltype=findViewById(R.id.tvschooltype);
        strength=findViewById(R.id.tvstrength);
        establish=findViewById(R.id.tvestab);
        fac_group=findViewById(R.id.chip_group_fac);
        doc_group=findViewById(R.id.chip_group_doc);
        noimg=findViewById(R.id.noimage);
        fb=findViewById(R.id.fb);
        insta=findViewById(R.id.insta);
        ytb=findViewById(R.id.ytb);
        linkin=findViewById(R.id.linkedin);
        web=findViewById(R.id.web);
        ratingBar=findViewById(R.id.ratingbar);
        submitrating=findViewById(R.id.submit_ratings);
        tvshowrate=findViewById(R.id.tvshowrate);
        submitreview=findViewById(R.id.submit_review);
        enterReview=findViewById(R.id.enter_review);
        showreview=findViewById(R.id.show_review);
        layout_show_review=findViewById(R.id.layout_after_review);
        recyclerView=findViewById(R.id.recycler_reviews);
        previousRatingbyUser=0;
        message=null;
        rated_or_not=false;
        count=1;
        mauth=FirebaseAuth.getInstance();
        user_id= mauth.getCurrentUser().getUid();
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "fonts/RobotoSlab-Regular.ttf", true);
        getExtras();
        documentReference_school = db.collection("schools").document(school_uid);
        documentReference_user=db.collection("users").document(user_id);
        imageSlider=findViewById(R.id.image_slider);
        photosList = new ArrayList<>();
        facilitieschipslist=new ArrayList<>();
        docchipslist=new ArrayList<>();
        slideModelList= new ArrayList<SlideModel>();
        getDocData();
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchurl(data.getFb());
            }
        });
        insta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchurl(data.getInsta());
            }
        });
        ytb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchurl(data.getYoutube());
            }
        });
        linkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchurl(data.getLinkedin());
            }
        });
        web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchurl(data.getWebsite());
            }
        });
        afterRatingShow();
        after_review();
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                int rate = (int) rating;

                    finalrate=ratingBar.getRating();
                    switch (rate){
                        case 0:
                            message="Sorry to hear that! :(";
                            break;
                        case 1:
                            message="Sorry to hear that! :(";
                            break;
                        case 2:
                            message="We always accept Suggestions.";
                            break;
                        case 3:
                            message="Good Enough!";
                            break;
                        case 4:
                            message="Great! Thank You!";
                            break;
                        case 5:
                            message="Awesome! You are the best!";
                            break;
                    }
//                Toast.makeText(School_page.this, message, Toast.LENGTH_SHORT).show();
                    submitrating.setVisibility(View.VISIBLE);
            }
        });
        submitrating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitRatingToFirebase();
            }
        });
        submitreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit_review_to_user();
                submit_review_to_school();
            }
        });
    }

    private void setUpRecyclerView() {
        ArrayList<String> reviews = new ArrayList<>();
        reviews.addAll(data.getReviews());
        adapter=new Adapter_review(reviews);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void submitRatingToFirebase() {
        Map<String, Float> ratingsMap = new HashMap<>();
        ratingsMap.put("rate",finalrate);

        documentReference_user.collection("rated_schools_id").
                document(school_uid).set(ratingsMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(School_page.this, message, Toast.LENGTH_SHORT).show();
                submitrating.setVisibility(View.GONE);
                updateSchoolRating();
                afterRatingShow();

            }
        });
    }

    private void afterRatingShow() {

        documentReference_user.collection("rated_schools_id").
                document(school_uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists())
                {
                    if(documentSnapshot.get("rate").toString()!=null)
                    {
                        TextView ratetheschool = findViewById(R.id.tvratetheschool);
                        ratetheschool.setText("Your Ratings");
                        previousRatingbyUser= Float.parseFloat(documentSnapshot.get("rate").toString());
                        ratingBar.setRating((Float) previousRatingbyUser);
                        rated_or_not=true;
                        submitrating.setVisibility(View.GONE);
                    }

                }
            }
        });

    }

    private void launchurl(String uri) {
        Intent urlintent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(Intent.createChooser(urlintent,"Open Using"));
    }

    private void putPhotoinImageSlider() {
        photosList=data.getPhotos();
        for (String photo:photosList) {
            slideModelList.add(new SlideModel(photo, ScaleTypes.CENTER_CROP));
        }
        imageSlider.setImageList(slideModelList,ScaleTypes.CENTER_CROP);


    }

    private void getDocData() {
        documentReference_school.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists())
                {
                    data= documentSnapshot.toObject(data_schools.class);
                    if(documentSnapshot.contains("photos"))
                    {
                        putPhotoinImageSlider();
                    }
                    else
                    {
                        imageSlider.setVisibility(View.GONE);
                        noimg.setVisibility(View.VISIBLE);
                    }
                    if(documentSnapshot.contains("reviews"))
                        setUpRecyclerView();
                    if(documentSnapshot.contains("show_ratings"))
                        tvshowrate.setText(String.valueOf(data.getShow_ratings()));
                    showdata();

                }
            }
        });
    }

    private void showdata() {

        if(data.getAdmOpen()!=null)
        {
            if(data.getAdmOpen().equals("Yes"))
            {
                admopen.setText("Admission Open");
                admopen.setBackgroundResource(R.color.green);
            }
            else
            {
                admopen.setText("Admission Closed");
                admopen.setBackgroundResource(R.color.red);
            }
        }



        if(data.getFb()!=null)
        {
            if(data.getFb().isEmpty())
                fb.setVisibility(View.GONE);
        }
        else
            fb.setVisibility(View.GONE);

        if(data.getInsta()!=null)
        {
            if(data.getInsta().isEmpty())
                insta.setVisibility(View.GONE);
        }
        else
            insta.setVisibility(View.GONE);

        if(data.getLinkedin()!=null)
        {
            if(data.getLinkedin().isEmpty())
                linkin.setVisibility(View.GONE);
        }
        else
            linkin.setVisibility(View.GONE);
        if(data.getYoutube()!=null)
        {
            if(data.getYoutube().isEmpty())
                ytb.setVisibility(View.GONE);
        }
        else
            ytb.setVisibility(View.GONE);
        if(data.getWebsite()!=null)
        {
            if(data.getWebsite().isEmpty())
                web.setVisibility(View.GONE);
        }
        else
            web.setVisibility(View.GONE);


        name.setText(data.getName());
        String loc=data.getAddress()+", "+data.getState();
        location.setText(loc);
        fees.setText("₹ "+data.getFees());
        grfrom.setText(data.getGradesfrom());
        grto.setText(data.getGradesupto());
        board.setText(data.getBoard());
        schooltype.setText(data.getSchooltype());
        strength.setText(data.getStrength());
        establish.setText(data.getEstablishmentYear());
        if(data.getChips_facility()!=null)
        {
            facilitieschipslist.addAll(data.chips_facility);
            for (int i = 0; i < facilitieschipslist.size(); i++) {
                Chip chip=new Chip(this);
                chip.setText(facilitieschipslist.get(i));
                fac_group.addView(chip);
            }
        }
        if(data.getChips_documents()!=null)
        {
            docchipslist.addAll(data.chips_documents);
            for (int i = 0; i < docchipslist.size(); i++) {
                Chip chip=new Chip(this);
                chip.setText(docchipslist.get(i));
                doc_group.addView(chip);
        }

        }
    }

    private void getExtras() {
        Bundle extra = getIntent().getExtras();
        if (extra != null)
            school_uid = extra.getString("school_uid");
    }

    float avgRatingSchool=0;
    private void updateSchoolRating(){
        alreadySchoolRating=data.getTotal_ratings();
        if(rated_or_not==true)
            count=0;
        else
            count=1;
         count+=data.getCount_ratings();
         float total_rating=finalrate+alreadySchoolRating-previousRatingbyUser;
        avgRatingSchool=total_rating/count;
        data.setShow_ratings(avgRatingSchool);
        data.setCount_ratings(count);
        data.setTotal_ratings(total_rating);
        documentReference_school.update("show_ratings",data.getShow_ratings(),
                "count_ratings",data.getCount_ratings(),
                "total_ratings",data.getTotal_ratings())
                .addOnSuccessListener(
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        tvshowrate.setText(String.valueOf(avgRatingSchool));
                        Toast.makeText(School_page.this, "Updated School Ratings", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }
    private void submit_review_to_school()
    {
        String review=enterReview.getEditText().getText().toString();
        documentReference_school.update("reviews", FieldValue.arrayUnion(review)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(!task.isSuccessful())
                {
                    Toast.makeText(School_page.this, "Review not Uploaded.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(School_page.this, "Uploaded to school", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void after_review()
    {
        documentReference_user.collection("review_schools_id").
                document(school_uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists())
                {
                    enterReview.setVisibility(View.GONE);
                    submitreview.setVisibility(View.GONE);
                    showreview.setText(documentSnapshot.get("review").toString());
                    layout_show_review.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    private void submit_review_to_user()
    {
        String review=enterReview.getEditText().getText().toString();
        Map<String, String> reviewMap = new HashMap<>();
        reviewMap.put("review",review);
        documentReference_user.collection("review_schools_id").
                document(school_uid).set(reviewMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(School_page.this, "Uploaded review to user", Toast.LENGTH_SHORT).show();
                after_review();
            }
        });
    }
}