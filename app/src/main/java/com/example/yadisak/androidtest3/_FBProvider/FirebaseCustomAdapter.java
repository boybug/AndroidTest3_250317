package com.example.yadisak.androidtest3._FBProvider;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.yadisak.androidtest3.DTO._FirebaseAttribute;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class FirebaseCustomAdapter<T> extends BaseAdapter {

    private Query ref;
    private Class<T> modelClass;
    private int layout;
    private LayoutInflater inflater;
    private List<T> models;
    private Map<String, T> modelsMap;
    private ChildEventListener listener;
    private List<T> oldmodels;


    public FirebaseCustomAdapter(Activity activity, Class<T> modelClass, int layout, Query ref) {

        this.ref = ref;
        this.modelClass = modelClass;
        this.layout = layout;
        inflater = activity.getLayoutInflater();
        models = new ArrayList<T>();
        oldmodels = new ArrayList<T>();
        modelsMap = new HashMap<String, T>();


        listener = this.ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {

                String keyId = dataSnapshot.getKey();
                T model = dataSnapshot.getValue(FirebaseCustomAdapter.this.modelClass);

                _FirebaseAttribute fbAttr = (_FirebaseAttribute) model;
                fbAttr.setFirebaseId(keyId);

                modelsMap.put(keyId, model);

                if (previousChildName == null) {
                    models.add(0, model);
                } else {
                    T previousModel = modelsMap.get(previousChildName);
                    int previousIndex = models.indexOf(previousModel);
                    int nextIndex = previousIndex + 1;
                    if (nextIndex == models.size()) {
                        models.add(model);
                    } else {
                        models.add(nextIndex, model);
                    }
                }

                notifyChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                String keyId = dataSnapshot.getKey();
                T oldModel = modelsMap.get(keyId);
                T newModel = dataSnapshot.getValue(FirebaseCustomAdapter.this.modelClass);

                _FirebaseAttribute fbAttr = (_FirebaseAttribute) newModel;
                fbAttr.setFirebaseId(keyId);

                int index = models.indexOf(oldModel);

                models.set(index, newModel);
                modelsMap.put(keyId, newModel);

                notifyChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                String keyId = dataSnapshot.getKey();
                T oldModel = modelsMap.get(keyId);
                models.remove(oldModel);
                modelsMap.remove(keyId);
                notifyChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {

                String keyId = dataSnapshot.getKey();
                T oldModel = modelsMap.get(keyId);
                T newModel = dataSnapshot.getValue(FirebaseCustomAdapter.this.modelClass);

                _FirebaseAttribute fbAttr = (_FirebaseAttribute) newModel;
                fbAttr.setFirebaseId(keyId);

                int index = models.indexOf(oldModel);
                models.remove(index);
                if (previousChildName == null) {
                    models.add(0, newModel);
                } else {
                    T previousModel = modelsMap.get(previousChildName);
                    int previousIndex = models.indexOf(previousModel);
                    int nextIndex = previousIndex + 1;
                    if (nextIndex == models.size()) {
                        models.add(newModel);
                    } else {
                        models.add(nextIndex, newModel);
                    }
                }

                notifyChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void cleanup() {
        ref.removeEventListener(listener);
        models.clear();
        modelsMap.clear();
    }

    public void notifyChanged() {
        this.models = modifyArrayAdapter(this.models);
        notifyDataSetChanged();
    }

    public List<T> getAllItems() {
        return models;
    }

    public List<T> getAllItemsold() {
        return oldmodels;
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public Object getItem(int i) {
        return models.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = inflater.inflate(layout, viewGroup, false);
        }

        T model = models.get(i);
        populateView(view, model);

        return view;
    }

    protected abstract void populateView(View v, T model);

    protected abstract List<T> modifyArrayAdapter(List<T> models); //Used for modifying the model list before populating

    public void setLayout(int layout) {
        this.layout = layout;
    }

    public void setModels(List<T> model) {
        this.models = model;
    }

    public void setOldmodels(List<T> oldmodels) {
        this.oldmodels = models;
    }
}