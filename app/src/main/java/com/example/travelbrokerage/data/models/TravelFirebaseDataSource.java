package com.example.travelbrokerage.data.models;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.travelbrokerage.util.MyApplication;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TravelFirebaseDataSource implements ITravelDataSource {

    private static final String TAG = "Firebase";

    private final MutableLiveData<Boolean> isSuccess = new MutableLiveData<>();
    private final List<Travel> allTravelsList;

    private NotifyToTravelListListener notifyToTravelListListener;

    FirebaseOptions options = new FirebaseOptions.Builder()
            //.setApiKey("AIzaSyBRxOyIj5dJkKgAVPXRLYFkdZwh2Xxq51k") // Required for Auth.
            .setDatabaseUrl("https://travelrequestform-401c8.firebaseio.com/") // Required for RTDB.
            .build();
    FirebaseApp fireApp = FirebaseApp.initializeApp(MyApplication.getAppContext(), options, "secondary");

    // Retrieve my other app.
    FirebaseApp app = FirebaseApp.getInstance("secondary");
    // Get the database for the other app.
    FirebaseDatabase secondaryDatabase = FirebaseDatabase.getInstance(app);
    DatabaseReference travels = secondaryDatabase.getReference("ExistingTravels");

    private static TravelFirebaseDataSource instance;

    public static TravelFirebaseDataSource getInstance() {
        if (instance == null)
            instance = new TravelFirebaseDataSource();
        return instance;
    }


    private TravelFirebaseDataSource() {
        allTravelsList = new ArrayList<>();
        travels.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allTravelsList.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Travel travel = snapshot.getValue(Travel.class);
                        allTravelsList.add(travel);
                    }
                }
                if (notifyToTravelListListener != null)
                    notifyToTravelListListener.onTravelsChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "ERROR on DataBase");
            }
        });
    }

    public void setNotifyToTravelListListener(NotifyToTravelListListener l) {
        notifyToTravelListListener = l;
    }

    @Override
    public void addTravel(Travel p) {
        String id = travels.push().getKey();
        p.setTravelId(id);
        travels.child(id).setValue(p).addOnCompleteListener(task -> isSuccess.setValue(task.isSuccessful()));
    }

    public void removeTravel(String id) {
        travels.child(id).removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Log.e(TAG, "Travel Removed");
            } else {
                Log.e(TAG, "Travel not Removed");
            }
        });
    }

    @Override
    public void updateTravel(final Travel toUpdate) {
        removeTravel(toUpdate.getTravelId());
        addTravel(toUpdate);
    }

    @Override
    public List<Travel> getAllTravels() {
        return allTravelsList;
    }

    public MutableLiveData<Boolean> getIsSuccess() {
        return isSuccess;
    }
}
